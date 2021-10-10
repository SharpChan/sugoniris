package com.sugon.iris.sugonservice.impl.fileServiceImpl;

import com.sugon.iris.sugoncommon.fileUtils.CsvExportUtil;
import com.sugon.iris.sugoncommon.publicUtils.PublicUtils;
import com.sugon.iris.sugondata.mybaties.mapper.db2.FileCaseMapper;
import com.sugon.iris.sugondata.mybaties.mapper.db2.FileTableMapper;
import com.sugon.iris.sugondata.mybaties.mapper.db2.FileTemplateDetailMapper;
import com.sugon.iris.sugondata.mybaties.mapper.db2.FileTemplateMapper;
import com.sugon.iris.sugondata.mybaties.mapper.db4.MppMapper;
import com.sugon.iris.sugondomain.beans.baseBeans.Error;
import com.sugon.iris.sugondomain.beans.fileBeans.ExcelRow;
import com.sugon.iris.sugondomain.dtos.fileDtos.*;
import com.sugon.iris.sugondomain.entities.mybatiesEntity.db2.FileCaseEntity;
import com.sugon.iris.sugondomain.entities.mybatiesEntity.db2.FileTableEntity;
import com.sugon.iris.sugondomain.entities.mybatiesEntity.db2.FileTemplateDetailEntity;
import com.sugon.iris.sugondomain.entities.mybatiesEntity.db2.FileTemplateEntity;
import com.sugon.iris.sugonservice.service.ExcelService.ExcelService;
import com.sugon.iris.sugonservice.service.FileService.FileDataMergeService;
import com.sun.xml.internal.messaging.saaj.util.ByteOutputStream;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@Service
public class FileDataMergeServiceImpl implements FileDataMergeService{

    @Resource
    private FileCaseMapper fileCaseMapper;

    @Resource
    private FileTableMapper fileTableMapper;

    @Resource
    private FileTemplateMapper fileTemplateMapper;

    @Resource
    private FileTemplateDetailMapper fileTemplateDetailMapper;

    @Resource
    private MppMapper mppMapper;

    @Resource
    private ExcelService excelServiceImpl;

    private static final String baseSql_with_rinses = " (select distinct a.id,a.*,b.* from &&_tableName_&& a left join error_info b \n" +
                 " on a.mppid2errorid = b.mppid2errorid) c where c.file_rinse_detail_id  not in(&&_rinses_&&) or c.file_rinse_detail_id is null";

    private static final String baseSql_without_rinses = " (select distinct a.id,a.*,b.* from &&_tableName_&& a left join error_info b \n" +
            " on a.mppid2errorid = b.mppid2errorid) c ";

    @Override
    public List<FileCaseDto> getCases(Long userId,List<Error> errorList) throws IllegalAccessException {

        List<FileCaseDto> fileCaseDtoList = new ArrayList<>();

        //通过userId 获取 案件信息
        FileCaseEntity fileCaseEntity4Sql = new FileCaseEntity();
        fileCaseEntity4Sql.setUserId(userId);
        List<FileCaseEntity> fileCaseEntityList = fileCaseMapper.selectFileCaseEntityList(fileCaseEntity4Sql);
        for(FileCaseEntity fileCaseEntity : fileCaseEntityList){
            FileCaseDto fileCaseDto = new FileCaseDto();
            PublicUtils.trans(fileCaseEntity,fileCaseDto);
            fileCaseDtoList.add(fileCaseDto);

            //获取表和模板信息
            FileTableEntity fileTableEntity4Sql = new FileTableEntity();
            fileTableEntity4Sql.setCaseId(fileCaseEntity.getId());
            List<FileTableEntity> fileTableEntityList = fileTableMapper.findFileTableList(fileTableEntity4Sql);
            List<FileTableDto> fileTableDtoList = new ArrayList<>();
            fileCaseDto.setFileTableDtoList(fileTableDtoList);
            for(FileTableEntity fileTableEntity : fileTableEntityList){
                FileTableDto fileTableDto = new FileTableDto();
                PublicUtils.trans(fileTableEntity ,fileTableDto);
                fileTableDtoList.add(fileTableDto);
                //获取对应的模板信息
                FileTemplateEntity fileTemplateEntity = fileTemplateMapper.selectFileTemplateByPrimaryKey(fileTableEntity.getFileTemplateId());
                FileTemplateDto fileTemplateDto = new FileTemplateDto();
                PublicUtils.trans(fileTemplateEntity,fileTemplateDto);
                fileTableDto.setFileTemplateDto(fileTemplateDto);
            }
        }
        return fileCaseDtoList;
    }

    /**
     *
     *  tableName：表名
     *  fileTemlateId：模板id
     *  perSize：一次查询的数量
     *  offset：查询开始行
     *  errorList：错误信息
     *  checkFields：勾选的清洗字段，不满足不显示
     * @return
     */
    @Override
    public MppTableDto getTableRecord(MppSearchDto mppSearchDto, List<Error> errorList) {
        MppTableDto mppTableDto = new MppTableDto();
        //通过模板id获取表字段
        List<FileTemplateDetailEntity> fileTemplateDetailEntityList = getFileTemplateDetailEntities(mppSearchDto);
        //设置表头，组装sql语句
        String sqlSelect = getString(mppTableDto, fileTemplateDetailEntityList);

        String rinses = "";
        for(String str : mppSearchDto.getCheckFields()){
            rinses += str+",";
        }
        if(StringUtils.isNotEmpty(rinses)) {
            rinses = rinses.substring(0, rinses.length() - 1);
            sqlSelect += " from " + baseSql_with_rinses + " LIMIT " + mppSearchDto.getPerSize() + " OFFSET " + mppSearchDto.getOffset();
            sqlSelect = sqlSelect.replace("&&_rinses_&&", rinses);
        }else{
            sqlSelect += " from " + baseSql_without_rinses + " LIMIT " + mppSearchDto.getPerSize() + " OFFSET " + mppSearchDto.getOffset();
        }
        sqlSelect = sqlSelect.replace("&&_tableName_&&",mppSearchDto.getTableName());
        List<Map<String,Object>> resultList =  mppMapper.mppSqlExecForSearchRtMapList(sqlSelect);
        mppTableDto.setRows(resultList);
        return mppTableDto;
    }

    @Override
    public Integer getTableRecordQuantity(MppSearchDto mppSearchDto, List<Error> errorList) {
        String rinses = "";
        for(String str : mppSearchDto.getCheckFields()){
            rinses += str+",";
        }
        String sql = "";
        if(StringUtils.isNotEmpty(rinses)) {
            rinses = rinses.substring(0, rinses.length() - 1);
            sql = "select count(*) from  " + baseSql_with_rinses;
            sql = sql.replace("&&_rinses_&&", rinses);
        }else{
            sql = "select count(*) from  " + baseSql_without_rinses;
        }
        sql = sql.replace("&&_tableName_&&",mppSearchDto.getTableName());
        String  res = mppMapper.mppSqlExecForSearch(sql).get(0);
        return Integer.parseInt(res);
    }

    @Override
    public void getCsv(MppSearchDto mppSearchDto, HttpServletResponse response) throws Exception {
        MppTableDto mppTableDto = new MppTableDto();
        List<FileTemplateDetailEntity> fileTemplateDetailEntityList = getFileTemplateDetailEntities(mppSearchDto);
        String sqlSelect = getString(mppTableDto, fileTemplateDetailEntityList);

        String rinses = "";
        for(String str : mppSearchDto.getCheckFields()){
            rinses += str+",";
        }
        if(StringUtils.isNotEmpty(rinses)) {
            rinses = rinses.substring(0, rinses.length() - 1);
            sqlSelect += " from " + baseSql_with_rinses ;
            sqlSelect = sqlSelect.replace("&&_rinses_&&", rinses);
        }else{
            sqlSelect += " from " + baseSql_without_rinses ;
        }
        sqlSelect = sqlSelect.replace("&&_tableName_&&",mppSearchDto.getTableName());
        List<Map<String,Object>> resultList =  mppMapper.mppSqlExecForSearchRtMapList(sqlSelect);
        mppTableDto.setRows(resultList);
        String fName = mppSearchDto.getCaseName()+"_"+mppSearchDto.getTemplateName();
        OutputStream os = response.getOutputStream();
        CsvExportUtil.responseSetProperties(fName, response);
        CsvExportUtil.doExport(mppTableDto,os);
    }

    @Override
    public void mergeExport(Long caseId, HttpServletResponse response) throws IOException {
        //获取案件下面的表信息
        FileTableEntity fileTableEntity = new FileTableEntity();
        fileTableEntity.setCaseId(caseId);
        List<FileTableEntity> fileTableEntityList = fileTableMapper.findFileTableList(fileTableEntity);

        ZipOutputStream zipOutputStream = new ZipOutputStream(response.getOutputStream());
        //组装excel
        for(FileTableEntity fileTableEntityBean : fileTableEntityList){
            FileTemplateDetailEntity fileTemplateDetailEntity4Sql = new FileTemplateDetailEntity();
            fileTemplateDetailEntity4Sql.setTemplateId(fileTableEntityBean.getFileTemplateId());
            List<FileTemplateDetailEntity> fileTemplateDetailEntityList = fileTemplateDetailMapper.selectFileTemplateDetailList(fileTemplateDetailEntity4Sql);
            PublicUtils.fileTemplateDetailEntityListSort(fileTemplateDetailEntityList);
            //获取总数据量
            String sqlCount = "select count(*)  "+" from "+fileTableEntityBean.getTableName() + " where mppid2errorid = '0'";
            int count = Integer.parseInt(mppMapper.mppSqlExecForSearch(sqlCount).get(0));
            int perSize = Integer.parseInt(PublicUtils.getConfigMap().get("mergeExport").replaceAll("\\s*",""));
            int times = count/perSize;
            if(count % perSize >0){
                times++;
            }
            for(int j =0;j< times;j++) {
                int offSet = j*perSize;
                List<ExcelRow> excelRowList = new ArrayList<>();
                //设置表头,并组装查询sql
                ExcelRow excelRowHead = new ExcelRow();
                excelRowList.add(excelRowHead);
                String sql = "select  ";
                List<String> fieldNameList = new ArrayList<>();
                int i = 0;
                for (FileTemplateDetailEntity fileTemplateDetailEntity : fileTemplateDetailEntityList) {
                    //设置表头
                    excelRowHead.getFields().add(fileTemplateDetailEntity.getFieldKey());
                    //获取值的顺序
                    fieldNameList.add(fileTemplateDetailEntity.getFieldName());
                    //组装sql
                    if (i < fileTemplateDetailEntityList.size() - 1) {
                        sql += fileTemplateDetailEntity.getFieldName() + ", ";
                    } else {
                        sql += fileTemplateDetailEntity.getFieldName();
                    }
                    i++;
                }
                sql += " from " + fileTableEntityBean.getTableName() + " where mppid2errorid = '0' "+" LIMIT " + perSize + " OFFSET " + offSet;
                //2.通过文件id获取数据
                List<Map<String, Object>> records = mppMapper.mppSqlExecForSearchRtMapList(sql);
                for (Map map : records) {
                    ExcelRow excelRow = new ExcelRow();
                    for (String str : fieldNameList) {
                        excelRow.getFields().add(map.get(str) + "");
                    }
                    excelRowList.add(excelRow);
                }
                //3.组装excel
                HSSFWorkbook workbook = excelServiceImpl.getNewExcel(fileTableEntityBean.getTitle(), excelRowList);
                ByteOutputStream byteOutputStream = new ByteOutputStream();
                workbook.write(byteOutputStream);
                ZipEntry entry = new ZipEntry(fileTableEntityBean.getTitle()+"_"+(j+1)+ ".xlsx");
                zipOutputStream.putNextEntry(entry);
                byteOutputStream.writeTo(zipOutputStream);
                byteOutputStream.close();
                zipOutputStream.closeEntry();
            }
        }
        zipOutputStream.close();
    }

    private String getString(MppTableDto mppTableDto, List<FileTemplateDetailEntity> fileTemplateDetailEntityList) {
        //设置表头，组装sql语句
        List<FieldDto> cols = new ArrayList<>();
        mppTableDto.setCols(cols);
        String sqlSelect = "select ";
        for (FileTemplateDetailEntity fileTemplateDetailEntity : fileTemplateDetailEntityList) {
            FieldDto fieldDto = new FieldDto();
            fieldDto.setFieldKey(fileTemplateDetailEntity.getFieldKey());
            fieldDto.setFieldName(fileTemplateDetailEntity.getFieldName());
            cols.add(fieldDto);
            sqlSelect += fileTemplateDetailEntity.getFieldName() + ",";
        }
        sqlSelect = sqlSelect.substring(0, sqlSelect.length() - 1);
        return sqlSelect;
    }

    private List<FileTemplateDetailEntity> getFileTemplateDetailEntities(MppSearchDto mppSearchDto) {
        //通过模板id获取表字段
        FileTemplateDetailEntity fileTemplateDetailEntity4Sql = new FileTemplateDetailEntity();
        fileTemplateDetailEntity4Sql.setTemplateId(mppSearchDto.getFileTemlateId());
        List<FileTemplateDetailEntity> fileTemplateDetailEntityList = fileTemplateDetailMapper.selectFileTemplateDetailList(fileTemplateDetailEntity4Sql);
        //进行排序
        PublicUtils.fileTemplateDetailEntityListSort(fileTemplateDetailEntityList);
        return fileTemplateDetailEntityList;
    }
}
