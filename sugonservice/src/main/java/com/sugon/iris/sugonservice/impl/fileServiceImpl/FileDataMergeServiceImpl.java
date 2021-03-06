package com.sugon.iris.sugonservice.impl.fileServiceImpl;

import com.sugon.iris.sugoncommon.fileUtils.CsvExportUtil;
import com.sugon.iris.sugoncommon.publicUtils.PublicUtils;
import com.sugon.iris.sugondata.mybaties.mapper.db2.*;
import com.sugon.iris.sugondata.mybaties.mapper.db4.MppMapper;
import com.sugon.iris.sugondomain.beans.baseBeans.Error;
import com.sugon.iris.sugondomain.beans.fileBeans.ExcelRow;
import com.sugon.iris.sugondomain.dtos.fileDtos.*;
import com.sugon.iris.sugondomain.entities.mybatiesEntity.db2.*;
import com.sugon.iris.sugonservice.service.excelService.ExcelService;
import com.sugon.iris.sugonservice.service.fileService.FileDataMergeService;
import com.sugon.iris.sugonservice.service.fileService.FileParsingService;
import com.sun.xml.internal.messaging.saaj.util.ByteOutputStream;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@Slf4j
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

    @Resource
    private FileParsingService fileParsingServiceImpl;

    @Resource
    private RinseBusinessIpMapper rinseBusinessIpMapper;

    @Resource
    private RinseBusinessPhoneMapper rinseBusinessPhoneMapper;

    @Resource
    private RinseBusinessIdCardNoMapper rinseBusinessIdCardNoMapper;

    private static final String baseSql_with_rinses = " (select row_number() OVER(PARTITION BY a.id ) AS rownum,a.*,b.*  from &&_tableName_&& a left join error_info b \n" +
            " on a.mppid2errorid = b.mppid2errorid) c where c.rownum = 1 and c.file_rinse_detail_id  not in(&&_rinses_&&) or c.file_rinse_detail_id is null";

    private static final String baseSql_without_rinses = " (select row_number() OVER(PARTITION BY a.id ) AS rownum,a.*,b.*  from &&_tableName_&& a left join error_info b \n" +
            " on a.mppid2errorid = b.mppid2errorid) c ";

    @Override
    public List<FileCaseDto> getCases(Long userId,List<Error> errorList) throws IllegalAccessException {

        List<FileCaseDto> fileCaseDtoList = new ArrayList<>();

        //??????userId ?????? ????????????
        FileCaseEntity fileCaseEntity4Sql = new FileCaseEntity();
        fileCaseEntity4Sql.setUserId(userId);
        List<FileCaseEntity> fileCaseEntityList = fileCaseMapper.selectFileCaseEntityList(fileCaseEntity4Sql);
        for(FileCaseEntity fileCaseEntity : fileCaseEntityList){
            FileCaseDto fileCaseDto = new FileCaseDto();
            PublicUtils.trans(fileCaseEntity,fileCaseDto);
            fileCaseDtoList.add(fileCaseDto);

            //????????????????????????
            FileTableEntity fileTableEntity4Sql = new FileTableEntity();
            fileTableEntity4Sql.setCaseId(fileCaseEntity.getId());
            List<FileTableEntity> fileTableEntityList = fileTableMapper.findFileTableList(fileTableEntity4Sql);
            List<FileTableDto> fileTableDtoList = new ArrayList<>();
            fileCaseDto.setFileTableDtoList(fileTableDtoList);
            for(FileTableEntity fileTableEntity : fileTableEntityList){
                FileTableDto fileTableDto = new FileTableDto();
                PublicUtils.trans(fileTableEntity ,fileTableDto);
                fileTableDtoList.add(fileTableDto);
                //???????????????????????????
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
     *  tableName?????????
     *  fileTemlateId?????????id
     *  perSize????????????????????????
     *  offset??????????????????
     *  errorList???????????????
     *  checkFields?????????????????????????????????????????????
     * @return
     */
    @Override
    public MppTableDto getTableRecord(MppSearchDto mppSearchDto, List<Error> errorList) {
        MppTableDto mppTableDto = new MppTableDto();
        //????????????id???????????????
        List<FileTemplateDetailEntity> fileTemplateDetailEntityList = getFileTemplateDetailEntities(mppSearchDto);

        //???????????????ip????????????
        List<RinseBusinessIpEntity> rinseBusinessIpEntityList =  rinseBusinessIpMapper.getRinseBusinessIpListByTemplateId(mppSearchDto.getFileTemlateId());
        if(!CollectionUtils.isEmpty(rinseBusinessIpEntityList)){
            for(RinseBusinessIpEntity rinseBusinessIpEntity : rinseBusinessIpEntityList) {
                FileTemplateDetailEntity fileTemplateDetailEntity =  fileTemplateDetailMapper.selectFileTemplateDetailByPrimary(rinseBusinessIpEntity.getFileTemplateDetailId());

                FileTemplateDetailEntity fileTemplateDetailEntityCountry = new FileTemplateDetailEntity();
                fileTemplateDetailEntityCountry.setFieldName(fileTemplateDetailEntity.getFieldName()+"_country");
                fileTemplateDetailEntityCountry.setFieldKey(fileTemplateDetailEntity.getFieldKey()+"_??????");
                FileTemplateDetailEntity fileTemplateDetailEntityArea = new FileTemplateDetailEntity();
                fileTemplateDetailEntityArea.setFieldName(fileTemplateDetailEntity.getFieldName()+"_area");
                fileTemplateDetailEntityArea.setFieldKey(fileTemplateDetailEntity.getFieldKey()+"_?????????");

                fileTemplateDetailEntityList.add(fileTemplateDetailEntityCountry);
                fileTemplateDetailEntityList.add(fileTemplateDetailEntityArea);
            }
        }
        //?????????????????????????????????
        List<RinseBusinessPhoneEntity> rinseBusinessPhoneEntityList = rinseBusinessPhoneMapper.getRinseBusinessPhoneListByTemplateId(mppSearchDto.getFileTemlateId());
        if(!CollectionUtils.isEmpty(rinseBusinessPhoneEntityList)){
            for(RinseBusinessPhoneEntity rinseBusinessPhoneEntity : rinseBusinessPhoneEntityList){
                FileTemplateDetailEntity fileTemplateDetailEntity =  fileTemplateDetailMapper.selectFileTemplateDetailByPrimary(rinseBusinessPhoneEntity.getFileTemplateDetailId());
                FileTemplateDetailEntity fileTemplateDetailEntityPhoneInfo = new FileTemplateDetailEntity();
                fileTemplateDetailEntityPhoneInfo.setFieldKey(fileTemplateDetailEntity.getFieldKey()+"_??????????????????");
                fileTemplateDetailEntityPhoneInfo.setFieldName(fileTemplateDetailEntity.getFieldName()+"_description");
                fileTemplateDetailEntityList.add(fileTemplateDetailEntityPhoneInfo);
            }
        }

        //???????????????????????????????????????
        List<RinseBusinessIdCardNoEntity> rinseBusinessIdCardNoEntityList = rinseBusinessIdCardNoMapper.getRinseBusinessIdCardNoListByTemplateId(mppSearchDto.getFileTemlateId());
        if(!CollectionUtils.isEmpty(rinseBusinessIdCardNoEntityList)){
            for(RinseBusinessIdCardNoEntity rinseBusinessIdCardNoEntity : rinseBusinessIdCardNoEntityList){
                FileTemplateDetailEntity fileTemplateDetailEntity =  fileTemplateDetailMapper.selectFileTemplateDetailByPrimary(rinseBusinessIdCardNoEntity.getFileTemplateDetailId());
                FileTemplateDetailEntity fileTemplateDetailEntityIdCardNoProvince = new FileTemplateDetailEntity();
                fileTemplateDetailEntityIdCardNoProvince.setFieldKey(fileTemplateDetailEntity.getFieldKey()+"_???");
                fileTemplateDetailEntityIdCardNoProvince.setFieldName(fileTemplateDetailEntity.getFieldName()+"_province");
                fileTemplateDetailEntityList.add(fileTemplateDetailEntityIdCardNoProvince);

                FileTemplateDetailEntity fileTemplateDetailEntityIdCardNoCity = new FileTemplateDetailEntity();
                fileTemplateDetailEntityIdCardNoCity.setFieldKey(fileTemplateDetailEntity.getFieldKey()+"_???");
                fileTemplateDetailEntityIdCardNoCity.setFieldName(fileTemplateDetailEntity.getFieldName()+"_city");
                fileTemplateDetailEntityList.add(fileTemplateDetailEntityIdCardNoCity);

                FileTemplateDetailEntity fileTemplateDetailEntityIdCardNoRegion = new FileTemplateDetailEntity();
                fileTemplateDetailEntityIdCardNoRegion.setFieldKey(fileTemplateDetailEntity.getFieldKey()+"_??????");
                fileTemplateDetailEntityIdCardNoRegion.setFieldName(fileTemplateDetailEntity.getFieldName()+"_region");
                fileTemplateDetailEntityList.add(fileTemplateDetailEntityIdCardNoRegion);

                FileTemplateDetailEntity fileTemplateDetailEntityIdCardNoBirthday = new FileTemplateDetailEntity();
                fileTemplateDetailEntityIdCardNoBirthday.setFieldKey(fileTemplateDetailEntity.getFieldKey()+"_????????????");
                fileTemplateDetailEntityIdCardNoBirthday.setFieldName(fileTemplateDetailEntity.getFieldName()+"_birthday");
                fileTemplateDetailEntityList.add(fileTemplateDetailEntityIdCardNoBirthday);

                FileTemplateDetailEntity fileTemplateDetailEntityIdCardNoGender = new FileTemplateDetailEntity();
                fileTemplateDetailEntityIdCardNoGender.setFieldKey(fileTemplateDetailEntity.getFieldKey()+"_??????");
                fileTemplateDetailEntityIdCardNoGender.setFieldName(fileTemplateDetailEntity.getFieldName()+"_gender");
                fileTemplateDetailEntityList.add(fileTemplateDetailEntityIdCardNoGender);
            }
        }


        //?????????????????????sql??????
        String sqlSelect = getString(mppTableDto, fileTemplateDetailEntityList);
        String rinses = "";
        for(String str : mppSearchDto.getCheckFields()){
            rinses += str+",";
        }
        if(StringUtils.isNotEmpty(rinses)) {
            rinses = rinses.substring(0, rinses.length() - 1);
            sqlSelect += " from " + baseSql_with_rinses + "  LIMIT " + mppSearchDto.getPerSize() + " OFFSET " + mppSearchDto.getOffset();
            sqlSelect = sqlSelect.replace("&&_rinses_&&", rinses);
        }else{
            sqlSelect += " from " + baseSql_without_rinses + " where c.rownum = 1 LIMIT " + mppSearchDto.getPerSize() + " OFFSET " + mppSearchDto.getOffset();
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
            sql = "select count(*) from  " + baseSql_without_rinses +" where c.rownum =1";
        }
        sql = sql.replace("&&_tableName_&&",mppSearchDto.getTableName());
        String  res = mppMapper.mppSqlExecForSearch(sql).get(0);
        return Integer.parseInt(res);
    }

    @Override
    public void doUserDefinedRinse(Long caseId, Long userId, List<Error> errorList) {
        fileParsingServiceImpl.doUserDefinedRinse(caseId,userId,errorList);
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
            sqlSelect += " from " + baseSql_without_rinses +" where c.rownum = 1 ";
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
        //??????????????????????????????
        FileTableEntity fileTableEntity = new FileTableEntity();
        fileTableEntity.setCaseId(caseId);
        List<FileTableEntity> fileTableEntityList = fileTableMapper.findFileTableList(fileTableEntity);

        ZipOutputStream zipOutputStream = new ZipOutputStream(response.getOutputStream());
        //??????excel
        for(FileTableEntity fileTableEntityBean : fileTableEntityList){
            FileTemplateDetailEntity fileTemplateDetailEntity4Sql = new FileTemplateDetailEntity();
            fileTemplateDetailEntity4Sql.setTemplateId(fileTableEntityBean.getFileTemplateId());
            List<FileTemplateDetailEntity> fileTemplateDetailEntityList = fileTemplateDetailMapper.selectFileTemplateDetailList(fileTemplateDetailEntity4Sql);
            PublicUtils.fileTemplateDetailEntityListSort(fileTemplateDetailEntityList);
            //??????????????????
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
                //????????????,???????????????sql
                ExcelRow excelRowHead = new ExcelRow();
                excelRowList.add(excelRowHead);
                String sql = "select  ";
                List<String> fieldNameList = new ArrayList<>();
                int i = 0;
                for (FileTemplateDetailEntity fileTemplateDetailEntity : fileTemplateDetailEntityList) {
                    //????????????
                    excelRowHead.getFields().add(fileTemplateDetailEntity.getFieldKey());
                    //??????????????????
                    fieldNameList.add(fileTemplateDetailEntity.getFieldName());
                    //??????sql
                    if (i < fileTemplateDetailEntityList.size() - 1) {
                        sql += fileTemplateDetailEntity.getFieldName() + ", ";
                    } else {
                        sql += fileTemplateDetailEntity.getFieldName();
                    }
                    i++;
                }
                sql += " from " + fileTableEntityBean.getTableName() + " where mppid2errorid = '0' "+" LIMIT " + perSize + " OFFSET " + offSet;
                //2.????????????id????????????
                List<Map<String, Object>> records = mppMapper.mppSqlExecForSearchRtMapList(sql);
                for (Map map : records) {
                    ExcelRow excelRow = new ExcelRow();
                    for (String str : fieldNameList) {
                        excelRow.getFields().add(map.get(str) + "");
                    }
                    excelRowList.add(excelRow);
                }
                //3.??????excel
                XSSFWorkbook workbook = excelServiceImpl.getNewExcelX(fileTableEntityBean.getTitle(), excelRowList);
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


    @Override
    public void mergeExportAsync(Long caseId, HttpServletResponse response) throws IOException, InterruptedException, ExecutionException {

        int perSize =Integer.parseInt(PublicUtils.getConfigMap().get("executorServiceOutput").replaceAll("\\s*",""));
        int excelSize = Integer.parseInt(PublicUtils.getConfigMap().get("mergeExport").replaceAll("\\s*",""));
        ZipOutputStream zipOutputStream = new ZipOutputStream(response.getOutputStream());

        //1.??????????????????????????????
        FileTableEntity fileTableEntity = new FileTableEntity();
        fileTableEntity.setCaseId(caseId);
        List<FileTableEntity> fileTableEntityList = fileTableMapper.findFileTableList(fileTableEntity);

        //2.??????????????????????????????????????????
        for(FileTableEntity fileTableEntityBean : fileTableEntityList){
            //2.1 ?????????????????????
            FileTemplateDetailEntity fileTemplateDetailEntity4Sql = new FileTemplateDetailEntity();
            fileTemplateDetailEntity4Sql.setTemplateId(fileTableEntityBean.getFileTemplateId());
            List<FileTemplateDetailEntity> fileTemplateDetailEntityList = fileTemplateDetailMapper.selectFileTemplateDetailList(fileTemplateDetailEntity4Sql);
            PublicUtils.fileTemplateDetailEntityListSort(fileTemplateDetailEntityList);

            //2.2??????????????????????????????????????????????????????
            //????????????,???????????????sql
            List<ExcelRow> excelRowList = new ArrayList<>();
            ExcelRow excelRowHead = new ExcelRow();
            String sql = "select  ";
            List<String> fieldNameList = new ArrayList<>();
            int i = 0;
            for (FileTemplateDetailEntity fileTemplateDetailEntity : fileTemplateDetailEntityList) {
                //????????????
                excelRowHead.getFields().add(fileTemplateDetailEntity.getFieldKey());
                //??????????????????
                fieldNameList.add(fileTemplateDetailEntity.getFieldName());
                //??????sql
                if (i < fileTemplateDetailEntityList.size() - 1) {
                    sql += fileTemplateDetailEntity.getFieldName() + ", ";
                } else {
                    sql += fileTemplateDetailEntity.getFieldName();
                }
                i++;
            }

            //2.3???????????????????????????
            String sqlCount = "select count(*)  "+" from "+fileTableEntityBean.getTableName();
            int count = Integer.parseInt(mppMapper.mppSqlExecForSearch(sqlCount).get(0));
            if(0==count){
                continue;
            }
            //???????????????????????????

            Integer execTimes = count%perSize > 0 ? count/perSize+1:count/perSize;

            //2.4
            ExecutorService executorService = Executors.newFixedThreadPool(execTimes > 5 ? 5:execTimes);
            List<Callable<List<Map<String, Object>>>> cList = new ArrayList<>();  //???????????????????????????
            Callable<List<Map<String, Object>>> task = null;  //??????????????????
            StringBuffer sbSql = new StringBuffer(sql);
            StringBuffer sbTableName = new StringBuffer(fileTableEntityBean.getTableName());

            for(int g = 0 ;g < execTimes ; g++){
                int offSet = g*perSize;
                task = new Callable<List<Map<String, Object>>>(){
                    @Override
                    public List<Map<String, Object>> call() throws Exception {
                        String sbStr = sbSql.toString();
                        sbStr += " from " + sbTableName.toString() +" LIMIT " + perSize + " OFFSET " + offSet;
                        List<Map<String, Object>> records = mppMapper.mppSqlExecForSearchRtMapList(sbStr);
                        return records;
                    }
                };
                cList.add(task);
            }

            List<Future<List<Map<String, Object>>>> results = executorService.invokeAll(cList,30, TimeUnit.MINUTES); //?????????????????????????????????????????????????????????????????????????????????????????????

            List<Map<String, Object>> records = new ArrayList<Map<String, Object>>();
            for(Future<List<Map<String, Object>>> recordPer:results){  //???????????????
                if(!CollectionUtils.isEmpty(recordPer.get())){
                    records.addAll(recordPer.get());
                }
            }
            executorService.shutdown();

            for (Map map : records) {
                if(null == map){
                    continue;
                }
                ExcelRow excelRow = new ExcelRow();
                for (String str : fieldNameList) {
                    excelRow.getFields().add("null".equals(map.get(str) + "") ? "":map.get(str) + "");
                }
                excelRowList.add(excelRow);
            }

            //2.5??????excel???count>20000???excelSize> 20000???????????????
            int excelTimes = count % excelSize > 0 ? count / excelSize + 1 : count / excelSize;
            if(count>20000 && excelSize> 20000){
                ExecutorService executorExcelService = Executors.newFixedThreadPool(excelTimes > 4 ? 4:excelTimes);
                List<Callable<Boolean>> cExcelList = new ArrayList<>();
                Callable<Boolean> excelTask = null;

                for (int k = 0; k < excelTimes; k++) {
                    List excelPerRoeList = new ArrayList();
                    excelPerRoeList.add(excelRowHead);
                    if (k < excelTimes - 1) {
                        excelPerRoeList.addAll(excelRowList.subList(k * excelSize, (k + 1) * excelSize));
                    } else {
                        excelPerRoeList.addAll(excelRowList.subList(k * excelSize, count));
                    }

                    Vector<ExcelRow> vector = new Vector<ExcelRow>(excelPerRoeList);

                    AtomicInteger kAtomic = new AtomicInteger(k);

                    excelTask = new Callable<Boolean>(){
                        @Override
                        public Boolean call() throws Exception {
                            //3.??????excel
                            XSSFWorkbook workbook = excelServiceImpl.getNewExcelX(fileTableEntityBean.getTitle(), vector);
                            ByteOutputStream byteOutputStream = new ByteOutputStream();
                            workbook.write(byteOutputStream);
                            ZipEntry entry = new ZipEntry(fileTableEntityBean.getTitle() + "_" + kAtomic.incrementAndGet() + ".xlsx");
                            zipOutputStream.putNextEntry(entry);
                            byteOutputStream.writeTo(zipOutputStream);
                            byteOutputStream.close();
                            zipOutputStream.closeEntry();
                            return true;
                        }
                    };
                    cExcelList.add(excelTask);
                }
                List<Future<Boolean>> excelResults = executorExcelService.invokeAll(cExcelList,1, TimeUnit.MINUTES);
                executorExcelService.shutdown();

            }else {
                for (int k = 0; k < excelTimes; k++) {
                    List excelPerRoeList = new ArrayList();
                    excelPerRoeList.add(excelRowHead);
                    if (k < excelTimes - 1) {
                        excelPerRoeList.addAll(excelRowList.subList(k * excelSize, (k + 1) * excelSize)) ;
                    } else {
                        excelPerRoeList.addAll(excelRowList.subList(k * excelSize, count));
                    }
                    //3.??????excel
                    XSSFWorkbook workbook = excelServiceImpl.getNewExcelX(fileTableEntityBean.getTitle(), excelPerRoeList);
                    ByteOutputStream byteOutputStream = new ByteOutputStream();
                    workbook.write(byteOutputStream);
                    ZipEntry entry = new ZipEntry(fileTableEntityBean.getTitle() + "_" + (k + 1) + ".xlsx");
                    zipOutputStream.putNextEntry(entry);
                    byteOutputStream.writeTo(zipOutputStream);
                    byteOutputStream.close();
                    zipOutputStream.closeEntry();
                }
            }
        }
        zipOutputStream.close();
    }

    @Override
    public void mergeExportAsyncForSheet(Long caseId, HttpServletResponse response) throws IOException, InterruptedException, ExecutionException {

        int perSize =Integer.parseInt(PublicUtils.getConfigMap().get("executorServiceOutput").replaceAll("\\s*",""));
        int excelSize = Integer.parseInt(PublicUtils.getConfigMap().get("mergeExport").replaceAll("\\s*",""));
        ZipOutputStream zipOutputStream = new ZipOutputStream(response.getOutputStream());

        //1.??????????????????????????????
        FileTableEntity fileTableEntity = new FileTableEntity();
        fileTableEntity.setCaseId(caseId);
        List<FileTableEntity> fileTableEntityList = fileTableMapper.findFileTableList(fileTableEntity);

        //2.??????????????????????????????????????????
        for(FileTableEntity fileTableEntityBean : fileTableEntityList){
            //2.1 ?????????????????????
            FileTemplateDetailEntity fileTemplateDetailEntity4Sql = new FileTemplateDetailEntity();
            fileTemplateDetailEntity4Sql.setTemplateId(fileTableEntityBean.getFileTemplateId());
            List<FileTemplateDetailEntity> fileTemplateDetailEntityList = fileTemplateDetailMapper.selectFileTemplateDetailList(fileTemplateDetailEntity4Sql);
            PublicUtils.fileTemplateDetailEntityListSort(fileTemplateDetailEntityList);

            //2.2??????????????????????????????????????????????????????
            //????????????,???????????????sql
            List<ExcelRow> excelRowList = new ArrayList<>();
            ExcelRow excelRowHead = new ExcelRow();
            String sql = "select  ";
            List<String> fieldNameList = new ArrayList<>();
            int i = 0;
            for (FileTemplateDetailEntity fileTemplateDetailEntity : fileTemplateDetailEntityList) {
                //????????????
                excelRowHead.getFields().add(fileTemplateDetailEntity.getFieldKey());
                //??????????????????
                fieldNameList.add(fileTemplateDetailEntity.getFieldName());
                //??????sql
                if (i < fileTemplateDetailEntityList.size() - 1) {
                    sql += fileTemplateDetailEntity.getFieldName() + ", ";
                } else {
                    sql += fileTemplateDetailEntity.getFieldName();
                }
                i++;
            }

            //2.3???????????????????????????
            String sqlCount = "select count(*)  "+" from "+fileTableEntityBean.getTableName();
            int count = Integer.parseInt(mppMapper.mppSqlExecForSearch(sqlCount).get(0));
            if(0==count){
                continue;
            }
            //???????????????????????????

            Integer execTimes = count%perSize > 0 ? count/perSize+1:count/perSize;

            //2.4
            ExecutorService executorService = Executors.newFixedThreadPool(execTimes > 5 ? 5:execTimes);
            List<Callable<List<Map<String, Object>>>> cList = new ArrayList<>();  //???????????????????????????
            Callable<List<Map<String, Object>>> task = null;  //??????????????????
            StringBuffer sbSql = new StringBuffer(sql);
            StringBuffer sbTableName = new StringBuffer(fileTableEntityBean.getTableName());

            for(int g = 0 ;g < execTimes ; g++){
                int offSet = g*perSize;
                task = new Callable<List<Map<String, Object>>>(){
                    @Override
                    public List<Map<String, Object>> call() throws Exception {
                        String sbStr = sbSql.toString();
                        sbStr += " from " + sbTableName.toString() +" LIMIT " + perSize + " OFFSET " + offSet;
                        List<Map<String, Object>> records = mppMapper.mppSqlExecForSearchRtMapList(sbStr);
                        return records;
                    }
                };
                cList.add(task);
            }

            List<Future<List<Map<String, Object>>>> results = executorService.invokeAll(cList,30, TimeUnit.MINUTES); //?????????????????????????????????????????????????????????????????????????????????????????????

            List<Map<String, Object>> records = new ArrayList<Map<String, Object>>();
            for(Future<List<Map<String, Object>>> recordPer:results){  //???????????????
                if(!CollectionUtils.isEmpty(recordPer.get())){
                    records.addAll(recordPer.get());
                }
            }
            executorService.shutdown();

            for (Map map : records) {
                if(null == map){
                    continue;
                }
                ExcelRow excelRow = new ExcelRow();
                for (String str : fieldNameList) {
                    excelRow.getFields().add("null".equals(map.get(str) + "") ? "":map.get(str) + "");
                }
                excelRowList.add(excelRow);
            }

            //2.5??????excel
            int excelTimes = count % excelSize > 0 ? count / excelSize + 1 : count / excelSize;
            XSSFWorkbook workbook = new XSSFWorkbook();
            for (int k = 0; k < excelTimes; k++) {
                List excelPerRoeList = new ArrayList();
                excelPerRoeList.add(excelRowHead);
                if (k < excelTimes - 1) {
                    excelPerRoeList.addAll(excelRowList.subList(k * excelSize, (k + 1) * excelSize)) ;
                } else {
                    excelPerRoeList.addAll(excelRowList.subList(k * excelSize, count));
                }
                //3.??????excel
                excelServiceImpl.getNewExcelXForSheet(workbook,(k+1)+"", excelPerRoeList);

            }
            ByteOutputStream byteOutputStream = new ByteOutputStream();
            workbook.write(byteOutputStream);
            ZipEntry entry = new ZipEntry(fileTableEntityBean.getTitle() + ".xlsx");
            zipOutputStream.putNextEntry(entry);
            byteOutputStream.writeTo(zipOutputStream);
            byteOutputStream.close();
            zipOutputStream.closeEntry();
        }
        zipOutputStream.close();
    }


    private String getString(MppTableDto mppTableDto, List<FileTemplateDetailEntity> fileTemplateDetailEntityList) {
        //?????????????????????sql??????
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
        //????????????id???????????????
        FileTemplateDetailEntity fileTemplateDetailEntity4Sql = new FileTemplateDetailEntity();
        fileTemplateDetailEntity4Sql.setTemplateId(mppSearchDto.getFileTemlateId());
        List<FileTemplateDetailEntity> fileTemplateDetailEntityList = fileTemplateDetailMapper.selectFileTemplateDetailList(fileTemplateDetailEntity4Sql);
        //????????????
        PublicUtils.fileTemplateDetailEntityListSort(fileTemplateDetailEntityList);
        return fileTemplateDetailEntityList;
    }
}
