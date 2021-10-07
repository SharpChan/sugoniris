package com.sugon.iris.sugonservice.impl.fileServiceImpl;

import com.sugon.iris.sugoncommon.publicUtils.PublicUtils;
import com.sugon.iris.sugondata.mybaties.mapper.db2.FileDetailMapper;
import com.sugon.iris.sugondata.mybaties.mapper.db2.FileParsingFailedMapper;
import com.sugon.iris.sugondata.mybaties.mapper.db2.FileTemplateDetailMapper;
import com.sugon.iris.sugondata.mybaties.mapper.db2.FileTemplateMapper;
import com.sugon.iris.sugondata.mybaties.mapper.db4.MppMapper;
import com.sugon.iris.sugondomain.beans.baseBeans.Error;
import com.sugon.iris.sugondomain.beans.fileBeans.ExcelRow;
import com.sugon.iris.sugondomain.dtos.fileDtos.FileAttachmentDto;
import com.sugon.iris.sugondomain.dtos.fileDtos.FileCaseDto;
import com.sugon.iris.sugondomain.dtos.fileDtos.FileDetailDto;
import com.sugon.iris.sugondomain.dtos.fileDtos.FileParsingFailedDto;
import com.sugon.iris.sugondomain.entities.mybatiesEntity.db2.FileDetailEntity;
import com.sugon.iris.sugondomain.entities.mybatiesEntity.db2.FileParsingFailedEntity;
import com.sugon.iris.sugondomain.entities.mybatiesEntity.db2.FileTemplateDetailEntity;
import com.sugon.iris.sugondomain.entities.mybatiesEntity.db2.FileTemplateEntity;
import com.sugon.iris.sugonservice.service.ExcelService.ExcelService;
import com.sugon.iris.sugonservice.service.FileService.FileCaseService;
import com.sugon.iris.sugonservice.service.FileService.FileImportCountService;
import com.sugon.iris.sugonservice.service.FileService.FolderService;
import com.sun.xml.internal.messaging.saaj.util.ByteOutputStream;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@Service
public class FileImportCountServiceImpl implements FileImportCountService {

    @Resource
    private FileCaseService fileCaseServiceImpl;

    @Resource
    private FolderService folderServiceImpl;

    @Resource
    private FileDetailMapper fileDetailMapper;

    @Resource
    private FileParsingFailedMapper fileParsingFailedMapper;

    @Resource
    private FileTemplateMapper fileTemplateMapper;

    @Resource
    private FileTemplateDetailMapper fileTemplateDetailMapper;

    @Resource
    private MppMapper mppMapper;

    @Resource
    private ExcelService excelServiceImpl;



    @Override
    public List<FileCaseDto> getImportCount(FileCaseDto fileCaseDto,List<Error> errorList) throws IllegalAccessException {
        List<FileCaseDto> fileCaseDtoList = null;
        List<FileAttachmentDto> fileAttachmentDtoList = null;
        List<FileDetailDto>  fileDetailDtoList = new ArrayList<>();


        FileAttachmentDto fileAttachmentDto = new FileAttachmentDto();
        fileAttachmentDto.setUserId(fileCaseDto.getUserId());

        FileDetailEntity fileDetailEntity = new FileDetailEntity();
        fileDetailEntity.setUserId(fileCaseDto.getUserId());
        try {
            fileCaseDtoList = fileCaseServiceImpl.selectCaseList(fileCaseDto, errorList);
            fileAttachmentDtoList = folderServiceImpl.findFileAttachmentList(fileAttachmentDto,errorList);
            List<FileDetailEntity> fileDetailEntityList = fileDetailMapper.selectFileDetailList(fileDetailEntity);
            for(FileDetailEntity fileDetailEntityBean : fileDetailEntityList){
                FileDetailDto fileDetailDto = new FileDetailDto();
                PublicUtils.trans(fileDetailEntityBean ,fileDetailDto );
                fileDetailDtoList.add(fileDetailDto);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        for(FileCaseDto fileCaseDtoBean :  fileCaseDtoList){
            for(FileAttachmentDto fileAttachmentDtoBean : fileAttachmentDtoList){
                if(fileCaseDtoBean.getId().equals(fileAttachmentDtoBean.getCaseId())){
                    fileCaseDtoBean.getFileAttachmentDtoList().add(fileAttachmentDtoBean);

                    for(FileDetailDto fileDetailDtoBean : fileDetailDtoList){
                         if(fileDetailDtoBean.getFileAttachmentId().equals(fileAttachmentDtoBean.getId()) && fileDetailDtoBean.getHasImport()){
                             fileAttachmentDtoBean.getFileDetailDtoList().add(fileDetailDtoBean);
                         }else if(fileDetailDtoBean.getFileAttachmentId().equals(fileAttachmentDtoBean.getId()) && !fileDetailDtoBean.getHasImport()){
                             fileAttachmentDtoBean.getFileDetailDtoFailedList().add(fileDetailDtoBean);
                         }
                    }
                }
            }
        }
        for(FileCaseDto fileCaseDtoBean :  fileCaseDtoList){
            fileCaseDtoBean.rowCount();
        }
        return fileCaseDtoList;
    }

    @Override
    public List<FileParsingFailedDto> getFileParsingFailed(Long userId, Long fileDetailId,List<Error> errorList) throws IllegalAccessException {
        List<FileParsingFailedDto> fileParsingFailedDtoList = new ArrayList<>();
        FileParsingFailedEntity fileParsingFailedEntity = new FileParsingFailedEntity();
        fileParsingFailedEntity.setUserId(userId);
        fileParsingFailedEntity.setFileDetailId(fileDetailId);
        fileParsingFailedEntity.setMark(false);
        List<FileParsingFailedEntity> fileParsingFailedEntityList = fileParsingFailedMapper.selectFileParsingFailedList(fileParsingFailedEntity);
        for(FileParsingFailedEntity fileParsingFailedEntityBean : fileParsingFailedEntityList){
            FileParsingFailedDto fileParsingFailedDto = new FileParsingFailedDto();
            PublicUtils.trans(fileParsingFailedEntityBean,fileParsingFailedDto);
            fileParsingFailedDtoList.add(fileParsingFailedDto);
        }
        return fileParsingFailedDtoList;
    }

    @Override
    public Boolean downloadErrorsExcel(HttpServletResponse response, HttpServletRequest request,Long fileDetailId, List<Error> errorList) {

        //1.通过文件id获取模板字段信息
        FileDetailEntity fileDetailEntity = fileDetailMapper.selectByPrimaryKey(fileDetailId);
        FileTemplateDetailEntity fileTemplateDetailEntity4Sql = new FileTemplateDetailEntity();
        fileTemplateDetailEntity4Sql.setTemplateId(fileDetailEntity.getFileTemplateId());
        List<FileTemplateDetailEntity> fileTemplateDetailEntityList = fileTemplateDetailMapper.selectFileTemplateDetailList(fileTemplateDetailEntity4Sql);
        FileTemplateEntity fileTemplateEntity = fileTemplateMapper.selectFileTemplateByPrimaryKey(fileDetailEntity.getFileTemplateId());
        PublicUtils.fileTemplateDetailEntityListSort(fileTemplateDetailEntityList);
        List<ExcelRow> excelRowList = new ArrayList<>();
        //设置表头,并组装查询sql
        ExcelRow excelRowHead = new ExcelRow();
        excelRowList.add(excelRowHead);
        String sql = "select id,mppid2errorid,";
        List<String> fieldNameList = new ArrayList<>();
        fieldNameList.add("id");
        fieldNameList.add("mppid2errorid");
        int i = 0;
        for(FileTemplateDetailEntity fileTemplateDetailEntity : fileTemplateDetailEntityList){
            //设置表头
            excelRowHead.getFields().add(fileTemplateDetailEntity.getFieldKey());
            //获取值的顺序
            fieldNameList.add(fileTemplateDetailEntity.getFieldName());
            //组装sql
            if(i < fileTemplateDetailEntityList.size()-1) {
                sql += fileTemplateDetailEntity.getFieldName() + ", ";
            }else{
                sql += fileTemplateDetailEntity.getFieldName();
            }
            i++;
        }
        sql += " from "+fileDetailEntity.getTableName();
        //2.通过文件id获取失败信息
        List<Map<String,Object>>  errors = mppMapper.mppSqlExecForSearchRtMapList(sql);
        for(Map map : errors){
            ExcelRow excelRow = new ExcelRow();
            for(String str : fieldNameList){
                excelRow.getFields().add((String) map.get(str));
            }
            excelRowList.add(excelRow);
        }

        //3.组装excel
        HSSFWorkbook workbook = excelServiceImpl.getNewExcel(fileDetailEntity.getFileTemplateId(),excelRowList);
        try {
            response.setContentType("application/zip; charset=UTF-8");
            //返回客户端浏览器的版本号、类型
            String agent = request.getHeader("USER-AGENT");
            String downloadName = "校验失败数据.zip";
            //针对IE或者以IE为内核的浏览器：
            if (agent.contains("MSIE") || agent.contains("Trident")) {
                downloadName = java.net.URLEncoder.encode(downloadName, "UTF-8");
            } else {
                downloadName = new String(downloadName.getBytes("UTF-8"), "ISO-8859-1");
            }
            response.setHeader("Content-disposition", "attachment;filename=" + downloadName);

            ZipOutputStream zipOutputStream = new ZipOutputStream(response.getOutputStream());
            //多个从这里就可遍历了
            // --start
            ZipEntry entry = new ZipEntry(fileTemplateEntity.getTemplateName()+"_"+fileDetailEntity.getFileName()+"_"+fileDetailEntity.getFileType()+"_校验失败.xls");
            zipOutputStream.putNextEntry(entry);
            ByteOutputStream byteOutputStream = new ByteOutputStream();
            workbook.write(byteOutputStream);
            byteOutputStream.writeTo(zipOutputStream);
            byteOutputStream.close();
            zipOutputStream.closeEntry();
            // --end
            zipOutputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


}
