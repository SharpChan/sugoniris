package com.sugon.iris.sugonservice.impl.fileServiceImpl;

import com.sugon.iris.sugoncommon.publicUtils.PublicUtils;
import com.sugon.iris.sugondata.mybaties.mapper.db2.*;
import com.sugon.iris.sugondata.mybaties.mapper.db4.MppMapper;
import com.sugon.iris.sugondomain.beans.baseBeans.Error;
import com.sugon.iris.sugondomain.beans.fileBeans.ExcelRow;
import com.sugon.iris.sugondomain.dtos.fileDtos.*;
import com.sugon.iris.sugondomain.dtos.regularDtos.RegularDetailDto;
import com.sugon.iris.sugondomain.entities.mybatiesEntity.db2.*;
import com.sugon.iris.sugondomain.enums.ErrorCode_Enum;
import com.sugon.iris.sugonservice.service.excelService.ExcelService;
import com.sugon.iris.sugonservice.service.fileService.FileCaseService;
import com.sugon.iris.sugonservice.service.fileService.FileImportCountService;
import com.sugon.iris.sugonservice.service.fileService.FolderService;
import com.sun.xml.internal.messaging.saaj.util.ByteOutputStream;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;
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

    @Resource
    private FileRinseGroupMapper fileRinseGroupMapper;

    @Resource
    private FileRinseDetailMapper fileRinseDetailMapper;

    @Resource
    private FileRinseRegularMapper fileRinseRegularMapper;

    @Resource
    private RegularDetailMapper regularDetailMapper;


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
                //查mpp数据库，获取已经导入的记录数
                //1.获取模板信息


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
    public Boolean downloadErrorsExcelZip(HttpServletResponse response, HttpServletRequest request,Long fileDetailId) {

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
        excelRowHead.getFields().add("id");
        excelRowHead.getFields().add("mppid2errorid");
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
        sql += " from "+fileDetailEntity.getTableName() + " where mppid2errorid != '0'";
        //2.通过文件id获取失败信息
        List<Map<String,Object>>  errors = mppMapper.mppSqlExecForSearchRtMapList(sql);
        for(Map map : errors){
            ExcelRow excelRow = new ExcelRow();
            for(String str : fieldNameList){
                excelRow.getFields().add(map.get(str)+"");
            }
            excelRowList.add(excelRow);
        }

        //3.组装excel
        HSSFWorkbook workbook = excelServiceImpl.getNewExcel(fileDetailEntity.getCaseId()+"_"+fileDetailEntity.getId()+"_"+fileDetailEntity.getFileTemplateId(),excelRowList);
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
            ZipEntry entry = new ZipEntry(fileTemplateEntity.getTemplateName()+"_"+fileDetailEntity.getFileName().substring(0,fileDetailEntity.getFileName().lastIndexOf("."))+"_"+fileDetailEntity.getFileType()+"_校验失败.xls");
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
            return false;
        }
        return true;
    }

    @Override
    public void downloadErrorsExcel(HttpServletResponse response, HttpServletRequest request, Long fileDetailId) throws IOException {
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
        excelRowHead.getFields().add("id");
        excelRowHead.getFields().add("mppid2errorid");
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
        sql += " from "+fileDetailEntity.getTableName() + " where mppid2errorid != '0' and file_detail_id ='"+fileDetailId+"'";
        //2.通过文件id获取失败信息
        List<Map<String,Object>>  errors = mppMapper.mppSqlExecForSearchRtMapList(sql);
        for(Map map : errors){
            ExcelRow excelRow = new ExcelRow();
            for(String str : fieldNameList){
                excelRow.getFields().add(map.get(str)+"");
            }
            excelRowList.add(excelRow);
        }
        //3.组装excel
        HSSFWorkbook workbook = excelServiceImpl.getNewExcel(fileDetailEntity.getCaseId()+"_"+fileDetailEntity.getId()+"_"+fileDetailEntity.getFileTemplateId(),excelRowList);
        response.setContentType("application/vnd.ms-excel");
        response.setHeader("Content-disposition", "attachment;filename=" + fileTemplateEntity.getTemplateName()+"_"+fileDetailEntity.getFileName().substring(0,fileDetailEntity.getFileName().lastIndexOf("."))+"_"+fileDetailEntity.getFileType()+"_校验失败.xls");
        response.setCharacterEncoding("UTF-8");
        response.flushBuffer();
        workbook.write(response.getOutputStream());

    }

    @Override
    public Integer dataAmendment(List<MultipartFile> files, Long  fileDetailId,List<Error> errorList) throws IOException {
        int c = 0;
        for(MultipartFile file : files){
            InputStream inputStream = file.getInputStream();
            Workbook workbook = null;
            try {
                workbook = WorkbookFactory.create(inputStream);
            }catch (Exception e){
                e.printStackTrace();
            }
            inputStream.close();
            //工作表对象
            Sheet sheet = workbook.getSheetAt(0);
            if(StringUtils.isEmpty(sheet.getSheetName())){
                errorList.add(new Error(ErrorCode_Enum.FILE_01_004.getCode(), ErrorCode_Enum.FILE_01_004.getMessage()));
                return 0;
            }
            String[] ids = sheet.getSheetName().replaceAll("\\s*","").split("_");
            if(null == ids || ids.length<3){
                errorList.add(new Error(ErrorCode_Enum.FILE_01_005.getCode(), ErrorCode_Enum.FILE_01_005.getMessage()));
                return 0;
            }
            String caseId = ids[0];
            String fileDetailIdExcel = ids[1];
            String fileTemplateId = ids[2];

            if(!String.valueOf(fileDetailId).equals(fileDetailIdExcel)){
                errorList.add(new Error(ErrorCode_Enum.FILE_01_006.getCode(), ErrorCode_Enum.FILE_01_006.getMessage()));
                return 0;
            }

            Row rowHead = sheet.getRow(0);
            //第一数据行索引
            int firstRowIndex = sheet.getFirstRowNum()+1;
            int lastRowIndex = sheet.getLastRowNum()+1;
            int firstCellIndex = rowHead.getFirstCellNum();
            int lastCellIndex = rowHead.getLastCellNum();
            if(lastRowIndex < 2){
                errorList.add(new Error(ErrorCode_Enum.FILE_01_006.getCode(), ErrorCode_Enum.FILE_01_006.getMessage()));
                return 0;
            }
            //获取数据模板,模板字段，清洗正则表达式
            FileTemplateDto fileTemplateDto = assembleFileTemplate(fileTemplateId,errorList);

            //获取mpp表名
            FileDetailEntity fileDetailEntity = fileDetailMapper.selectByPrimaryKey(fileDetailId);

            //组装插入语句
            String insertSql = getInsertSql(Long.parseLong(caseId),fileDetailEntity.getFileAttachmentId(),fileDetailId,fileTemplateDto.getFileTemplateDetailDtoList(),fileDetailEntity.getTableName(),fileTemplateDto.getId());

            //获取excel表头
            List<String> headList = new ArrayList<>();
            int mppid2errorid_index = 0;
            for(int i = firstCellIndex;i<lastCellIndex;i++){
                Cell cell = rowHead.getCell(i);

                if("mppid2errorid".equals(cell.getStringCellValue().replaceAll("\\s*", ""))){
                    mppid2errorid_index = i;
                }
                //排除id和mppid2errorid
                if("id".equals(cell.getStringCellValue().replaceAll("\\s*", ""))||"mppid2errorid".equals(cell.getStringCellValue().replaceAll("\\s*", ""))){
                         continue;
                }
                headList.add(cell.getStringCellValue().replaceAll("\\s*", ""));
            }


            List<String> filedList = new ArrayList<>();
            for(FileTemplateDetailDto fileTemplateDetailDto : fileTemplateDto.getFileTemplateDetailDtoList()){
                filedList.add(fileTemplateDetailDto.getFieldKey());
            }
            //校验excel表头是否满足要求
            for(String field : headList){
                if(!filedList.contains(field)){
                    errorList.add(new Error(ErrorCode_Enum.FILE_01_009.getCode(), ErrorCode_Enum.FILE_01_009.getMessage()));
                    return 0;
                }
            }

            //获取模板字段和excel字段的对应关系
            //key:excel列索引;value:模板字段
            Map<Integer,FileTemplateDetailDto>  excelColum2TemplateDetailMap = new HashMap<>();
            //key:模板字段id，value:excel列索引
            Map<Long, Integer>  feildIdRefIndexMap = new HashMap<>();
            for(FileTemplateDetailDto fileTemplateDetailDto : fileTemplateDto.getFileTemplateDetailDtoList()){
                for(int i = firstCellIndex;i<lastCellIndex;i++){
                    Cell cell = rowHead.getCell(i);
                    if(fileTemplateDetailDto.getFieldKey().replaceAll("\\s*", "").equals(cell.getStringCellValue().replaceAll("\\s*", ""))){
                        excelColum2TemplateDetailMap.put(i,fileTemplateDetailDto);
                        feildIdRefIndexMap.put(fileTemplateDetailDto.getId(),i);
                    }
                }
            }

            //对excel行进行校验
            int k = 0;
            Set<String> mppid2errorid_index_set = new HashSet<>();
             for(int i = firstRowIndex;i<lastRowIndex;i++){
                Row row = sheet.getRow(i);
                 retry:  for(int j = firstCellIndex;j<lastCellIndex;j++){
                    Cell cell = row.getCell(j);
                    Cell cellHead = rowHead.getCell(j);
                     //排除id和mppid2errorid
                    if("id".equals(cellHead.getStringCellValue().replaceAll("\\s*", ""))||"mppid2errorid".equals(cellHead.getStringCellValue().replaceAll("\\s*", ""))){
                        continue;
                    }
                    //进行正则校验
                    List<RegularDetailDto> regularDetailDtoListY = null;
                    List<RegularDetailDto> regularDetailDtoListN = null;
                    if(null != excelColum2TemplateDetailMap.get(j).getFileRinseDetailDto()){
                        regularDetailDtoListY = excelColum2TemplateDetailMap.get(j).getFileRinseDetailDto().getRegularDetailDtoListY();
                        regularDetailDtoListN = excelColum2TemplateDetailMap.get(j).getFileRinseDetailDto().getRegularDetailDtoListN();
                    }

                    //boolean checkRegular = false;
                    //不需要匹配，则跳到下一个
                    if(CollectionUtils.isEmpty(regularDetailDtoListY) && CollectionUtils.isEmpty(regularDetailDtoListN)){
                       continue;
                    }

                    //把所有的单元格变为string类型
                    cell.setCellType(Cell.CELL_TYPE_STRING);

                    //只要有一个匹配就满足要求
                    boolean checkRegularY = false;
                    if(!CollectionUtils.isEmpty(regularDetailDtoListY)){
                        for(RegularDetailDto regularDetailDto : regularDetailDtoListY){
                            if (cell.getStringCellValue().replaceAll("\\s*", "").matches(regularDetailDto.getRegularValue().trim())) {
                                checkRegularY = true;
                                break;//只要匹配一个就跳出
                            }
                        }
                    }else{
                        checkRegularY = true;
                    }

                    //只要有一个匹配就不满足要求
                    boolean checkRegularN = true;
                    if(!CollectionUtils.isEmpty(regularDetailDtoListN)){
                        for (RegularDetailDto regularDetailDto : regularDetailDtoListN) {
                            if (cell.getStringCellValue().replaceAll("\\s*", "").matches(regularDetailDto.getRegularValue().trim())) {
                                checkRegularN = false;
                                break;//只要匹配一个就跳出
                            }
                        }
                    }

                    //这行中有一个字段不满足就跳过
                    if(!(checkRegularY && checkRegularN)){
                        break retry;
                    }
                }
                for(int h = firstCellIndex;h<lastCellIndex;h++){
                    if(h == mppid2errorid_index){
                        mppid2errorid_index_set.add(row.getCell(h).getStringCellValue().replaceAll("\\s*", ""));
                    }

                }
                //都满足，插入这行
                String insertSqlExec = insertSql;
                for(Map.Entry<Long, Integer> entry : feildIdRefIndexMap.entrySet()){
                    //把所有的单元格变为string类型
                    row.getCell(entry.getValue()).setCellType(Cell.CELL_TYPE_STRING);
                    insertSqlExec = insertSqlExec.replace("&&"+entry.getKey()+"&&",row.getCell(entry.getValue()).getStringCellValue().replaceAll("\\s*", ""));
                }
                mppMapper.mppSqlExec(insertSqlExec);
                k++;
                c++ ;
            }

            if(k != 0) {
                fileDetailEntity.setImportRowCount(fileDetailEntity.getImportRowCount() + k);
                fileDetailMapper.updateByPrimaryKey(fileDetailEntity);
            }

            //进行删除
            if(!CollectionUtils.isEmpty(mppid2errorid_index_set)){
                for(String str : mppid2errorid_index_set){
                    String deleteMppRecord = "delete from  " + fileDetailEntity.getTableName() + " where mppid2errorid ='"+str+ "'";
                    mppMapper.mppSqlExec(deleteMppRecord);
                    //删除mysql  file_parsing_failed表
                    fileParsingFailedMapper.deleteFileParsingFailedByMppid2errorid(Long.parseLong(str));
                    String errorInfoRecord = "delete from  error_info where mppid2errorid ='"+str+ "'";
                    //删除mpp  error_info表
                    mppMapper.mppSqlExec(errorInfoRecord);
                }
            }

        }
        return c;
    }


    //组装插入数据语句
    private String getInsertSql(Long caseId,Long fileAttachmentId, Long fileDetailId,List<FileTemplateDetailDto> fileTemplateDetailDtoList, String tableName,Long fileTemplateId) {
        //组装insertSql语句
        String sqlInsert = "insert into "+tableName+"(";
        String  sqlValues = "";
        for(FileTemplateDetailDto fileTemplateDetailDto : fileTemplateDetailDtoList){
            sqlInsert += fileTemplateDetailDto.getFieldName()+",";
            sqlValues += "'&&"+fileTemplateDetailDto.getId()+"&&',";
        }
        sqlInsert += "file_template_id,case_id,file_attachment_id,file_detail_id,mppId2ErrorId";
        sqlValues += "'"+fileTemplateId+"','"+caseId+"','"+fileAttachmentId+"','"+fileDetailId+"',"+"'0'";
        sqlInsert +=") values(" +sqlValues+");";
        return sqlInsert;
    }

    private FileTemplateDto assembleFileTemplate(String fileTemplateId,List<Error> errorList){
        FileTemplateDto fileTemplateDto = new FileTemplateDto();
        FileTemplateEntity fileTemplateEntity = fileTemplateMapper.selectFileTemplateByPrimaryKey(Long.parseLong(fileTemplateId));
        try {
            PublicUtils.trans(fileTemplateEntity,fileTemplateDto);
        }catch (Exception e){
            e.printStackTrace();
        }


        //获取清洗字段组
        FileRinseGroupEntity fileRinseGroupEntity = fileRinseGroupMapper.selectByPrimaryKey(fileTemplateDto.getFileRinseGroupId());
        FileRinseGroupDto fileRinseGroupDto = new FileRinseGroupDto();
        try{
            PublicUtils.trans(fileRinseGroupEntity,fileRinseGroupDto);
        }catch (Exception e){
            e.printStackTrace();
        }

        fileTemplateDto.setFileRinseGroupDto(fileRinseGroupDto);

        //获取该模板字段信息
        List<FileTemplateDetailDto> fileTemplateDetailDtoList = new ArrayList<>();
        fileTemplateDto.setFileTemplateDetailDtoList(fileTemplateDetailDtoList);
        FileTemplateDetailEntity fileTemplateDetailEntity4Sql = new FileTemplateDetailEntity();
        fileTemplateDetailEntity4Sql.setTemplateId(Long.parseLong(fileTemplateId));
        List<FileTemplateDetailEntity> fileTemplateDetailEntityList =  fileTemplateDetailMapper.selectFileTemplateDetailList(fileTemplateDetailEntity4Sql);
        if(CollectionUtils.isEmpty(fileTemplateDetailEntityList)){
            errorList.add(new Error(ErrorCode_Enum.FILE_01_007.getCode(), ErrorCode_Enum.FILE_01_007.getMessage()));
            return fileTemplateDto;
        }
        for(FileTemplateDetailEntity fileTemplateDetailEntity : fileTemplateDetailEntityList){
            FileTemplateDetailDto fileTemplateDetailDto = new FileTemplateDetailDto();
            try {
                PublicUtils.trans(fileTemplateDetailEntity, fileTemplateDetailDto);
            }catch (Exception e){
                e.printStackTrace();
            }
            fileTemplateDetailDtoList.add(fileTemplateDetailDto);

            //如果没有配置清洗字段则跳过
            if(null == fileTemplateDetailDto.getFileRinseDetailId()){
                continue;
            }

            //获取清洗字段
            FileRinseDetailEntity fileRinseDetailEntity = fileRinseDetailMapper.selectByPrimaryKey(fileTemplateDetailDto.getFileRinseDetailId());
            FileRinseDetailDto fileRinseDetailDto = new FileRinseDetailDto();
            try {
                PublicUtils.trans(fileRinseDetailEntity,fileRinseDetailDto);
            }catch (Exception e){
                e.printStackTrace();
            }
            fileTemplateDetailDto.setFileRinseDetailDto(fileRinseDetailDto);
            //获取正则表达式
            List<RegularDetailDto>  regularDetailDtoListY = new ArrayList<>();
            List<RegularDetailDto>  regularDetailDtoListN = new ArrayList<>();
            //获取正则表达式
            //获取清洗字段与正则表达式的对应关系
            List<FileRinseRegularEntity> fileRinseRegularEntityList = fileRinseRegularMapper.selectByFileRinseDetailId(fileRinseDetailEntity.getId());

            if(!CollectionUtils.isEmpty(fileRinseRegularEntityList)) {
                for (FileRinseRegularEntity fileRinseRegularEntity : fileRinseRegularEntityList) {
                    if ("1".equals(fileRinseRegularEntity.getType())) {
                        RegularDetailEntity regularDetailEntityY = regularDetailMapper.selectByPrimaryKey(fileRinseRegularEntity.getRegularDetailId());
                        RegularDetailDto regularDetailDtoY = new RegularDetailDto();
                        try{
                            PublicUtils.trans(regularDetailEntityY, regularDetailDtoY);
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                        regularDetailDtoListY.add(regularDetailDtoY);
                    }
                    if ("2".equals(fileRinseRegularEntity.getType())) {
                        RegularDetailEntity regularDetailEntityN = regularDetailMapper.selectByPrimaryKey(fileRinseRegularEntity.getRegularDetailId());
                        RegularDetailDto regularDetailDtoN = new RegularDetailDto();
                        try{
                            PublicUtils.trans(regularDetailEntityN, regularDetailDtoN);
                        }catch (Exception e){
                            e.printStackTrace();
                        }

                        regularDetailDtoListY.add(regularDetailDtoN);
                    }
                }
                fileRinseDetailDto.setRegularDetailDtoListY(regularDetailDtoListY);
                fileRinseDetailDto.setRegularDetailDtoListN(regularDetailDtoListN);
            }
        }
        return fileTemplateDto;
    }
}
