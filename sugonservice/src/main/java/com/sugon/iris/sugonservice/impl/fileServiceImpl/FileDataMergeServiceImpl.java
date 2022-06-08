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

        //表字段新增ip解析字段
        List<RinseBusinessIpEntity> rinseBusinessIpEntityList =  rinseBusinessIpMapper.getRinseBusinessIpListByTemplateId(mppSearchDto.getFileTemlateId());
        if(!CollectionUtils.isEmpty(rinseBusinessIpEntityList)){
            for(RinseBusinessIpEntity rinseBusinessIpEntity : rinseBusinessIpEntityList) {
                FileTemplateDetailEntity fileTemplateDetailEntity =  fileTemplateDetailMapper.selectFileTemplateDetailByPrimary(rinseBusinessIpEntity.getFileTemplateDetailId());

                FileTemplateDetailEntity fileTemplateDetailEntityCountry = new FileTemplateDetailEntity();
                fileTemplateDetailEntityCountry.setFieldName(fileTemplateDetailEntity.getFieldName()+"_country");
                fileTemplateDetailEntityCountry.setFieldKey(fileTemplateDetailEntity.getFieldKey()+"_省市");
                FileTemplateDetailEntity fileTemplateDetailEntityArea = new FileTemplateDetailEntity();
                fileTemplateDetailEntityArea.setFieldName(fileTemplateDetailEntity.getFieldName()+"_area");
                fileTemplateDetailEntityArea.setFieldKey(fileTemplateDetailEntity.getFieldKey()+"_运营商");

                fileTemplateDetailEntityList.add(fileTemplateDetailEntityCountry);
                fileTemplateDetailEntityList.add(fileTemplateDetailEntityArea);
            }
        }
        //表字段新增电话解析字段
        List<RinseBusinessPhoneEntity> rinseBusinessPhoneEntityList = rinseBusinessPhoneMapper.getRinseBusinessPhoneListByTemplateId(mppSearchDto.getFileTemlateId());
        if(!CollectionUtils.isEmpty(rinseBusinessPhoneEntityList)){
            for(RinseBusinessPhoneEntity rinseBusinessPhoneEntity : rinseBusinessPhoneEntityList){
                FileTemplateDetailEntity fileTemplateDetailEntity =  fileTemplateDetailMapper.selectFileTemplateDetailByPrimary(rinseBusinessPhoneEntity.getFileTemplateDetailId());
                FileTemplateDetailEntity fileTemplateDetailEntityPhoneInfo = new FileTemplateDetailEntity();
                fileTemplateDetailEntityPhoneInfo.setFieldKey(fileTemplateDetailEntity.getFieldKey()+"_电话号码信息");
                fileTemplateDetailEntityPhoneInfo.setFieldName(fileTemplateDetailEntity.getFieldName()+"_description");
                fileTemplateDetailEntityList.add(fileTemplateDetailEntityPhoneInfo);
            }
        }

        //表字段新增身份证号解析字段
        List<RinseBusinessIdCardNoEntity> rinseBusinessIdCardNoEntityList = rinseBusinessIdCardNoMapper.getRinseBusinessIdCardNoListByTemplateId(mppSearchDto.getFileTemlateId());
        if(!CollectionUtils.isEmpty(rinseBusinessIdCardNoEntityList)){
            for(RinseBusinessIdCardNoEntity rinseBusinessIdCardNoEntity : rinseBusinessIdCardNoEntityList){
                FileTemplateDetailEntity fileTemplateDetailEntity =  fileTemplateDetailMapper.selectFileTemplateDetailByPrimary(rinseBusinessIdCardNoEntity.getFileTemplateDetailId());
                FileTemplateDetailEntity fileTemplateDetailEntityIdCardNoProvince = new FileTemplateDetailEntity();
                fileTemplateDetailEntityIdCardNoProvince.setFieldKey(fileTemplateDetailEntity.getFieldKey()+"_省");
                fileTemplateDetailEntityIdCardNoProvince.setFieldName(fileTemplateDetailEntity.getFieldName()+"_province");
                fileTemplateDetailEntityList.add(fileTemplateDetailEntityIdCardNoProvince);

                FileTemplateDetailEntity fileTemplateDetailEntityIdCardNoCity = new FileTemplateDetailEntity();
                fileTemplateDetailEntityIdCardNoCity.setFieldKey(fileTemplateDetailEntity.getFieldKey()+"_市");
                fileTemplateDetailEntityIdCardNoCity.setFieldName(fileTemplateDetailEntity.getFieldName()+"_city");
                fileTemplateDetailEntityList.add(fileTemplateDetailEntityIdCardNoCity);

                FileTemplateDetailEntity fileTemplateDetailEntityIdCardNoRegion = new FileTemplateDetailEntity();
                fileTemplateDetailEntityIdCardNoRegion.setFieldKey(fileTemplateDetailEntity.getFieldKey()+"_区县");
                fileTemplateDetailEntityIdCardNoRegion.setFieldName(fileTemplateDetailEntity.getFieldName()+"_region");
                fileTemplateDetailEntityList.add(fileTemplateDetailEntityIdCardNoRegion);

                FileTemplateDetailEntity fileTemplateDetailEntityIdCardNoBirthday = new FileTemplateDetailEntity();
                fileTemplateDetailEntityIdCardNoBirthday.setFieldKey(fileTemplateDetailEntity.getFieldKey()+"_出生日期");
                fileTemplateDetailEntityIdCardNoBirthday.setFieldName(fileTemplateDetailEntity.getFieldName()+"_birthday");
                fileTemplateDetailEntityList.add(fileTemplateDetailEntityIdCardNoBirthday);

                FileTemplateDetailEntity fileTemplateDetailEntityIdCardNoGender = new FileTemplateDetailEntity();
                fileTemplateDetailEntityIdCardNoGender.setFieldKey(fileTemplateDetailEntity.getFieldKey()+"_性别");
                fileTemplateDetailEntityIdCardNoGender.setFieldName(fileTemplateDetailEntity.getFieldName()+"_gender");
                fileTemplateDetailEntityList.add(fileTemplateDetailEntityIdCardNoGender);
            }
        }


        //设置表头，组装sql语句
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

        //1.获取案件下面的表信息
        FileTableEntity fileTableEntity = new FileTableEntity();
        fileTableEntity.setCaseId(caseId);
        List<FileTableEntity> fileTableEntityList = fileTableMapper.findFileTableList(fileTableEntity);

        //2.遍历表信息，并获取对应的数据
        for(FileTableEntity fileTableEntityBean : fileTableEntityList){
            //2.1 获取表字段信息
            FileTemplateDetailEntity fileTemplateDetailEntity4Sql = new FileTemplateDetailEntity();
            fileTemplateDetailEntity4Sql.setTemplateId(fileTableEntityBean.getFileTemplateId());
            List<FileTemplateDetailEntity> fileTemplateDetailEntityList = fileTemplateDetailMapper.selectFileTemplateDetailList(fileTemplateDetailEntity4Sql);
            PublicUtils.fileTemplateDetailEntityListSort(fileTemplateDetailEntityList);

            //2.2设置表头，并且组装查询需要查询的字段
            //设置表头,并组装查询sql
            List<ExcelRow> excelRowList = new ArrayList<>();
            ExcelRow excelRowHead = new ExcelRow();
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

            //2.3获取该表的数据总量
            String sqlCount = "select count(*)  "+" from "+fileTableEntityBean.getTableName();
            int count = Integer.parseInt(mppMapper.mppSqlExecForSearch(sqlCount).get(0));
            if(0==count){
                continue;
            }
            //需要分表查询的次数

            Integer execTimes = count%perSize > 0 ? count/perSize+1:count/perSize;

            //2.4
            ExecutorService executorService = Executors.newFixedThreadPool(execTimes > 5 ? 5:execTimes);
            List<Callable<List<Map<String, Object>>>> cList = new ArrayList<>();  //定义添加线程的集合
            Callable<List<Map<String, Object>>> task = null;  //创建单个线程
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

            List<Future<List<Map<String, Object>>>> results = executorService.invokeAll(cList,30, TimeUnit.MINUTES); //执行所有创建的线程，并获取返回值（会把所有线程的返回值都返回）

            List<Map<String, Object>> records = new ArrayList<Map<String, Object>>();
            for(Future<List<Map<String, Object>>> recordPer:results){  //打印返回值
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

            //2.5组装excel，count>20000且excelSize> 20000采用多线程
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
                            //3.组装excel
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
                    //3.组装excel
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

        //1.获取案件下面的表信息
        FileTableEntity fileTableEntity = new FileTableEntity();
        fileTableEntity.setCaseId(caseId);
        List<FileTableEntity> fileTableEntityList = fileTableMapper.findFileTableList(fileTableEntity);

        //2.遍历表信息，并获取对应的数据
        for(FileTableEntity fileTableEntityBean : fileTableEntityList){
            //2.1 获取表字段信息
            FileTemplateDetailEntity fileTemplateDetailEntity4Sql = new FileTemplateDetailEntity();
            fileTemplateDetailEntity4Sql.setTemplateId(fileTableEntityBean.getFileTemplateId());
            List<FileTemplateDetailEntity> fileTemplateDetailEntityList = fileTemplateDetailMapper.selectFileTemplateDetailList(fileTemplateDetailEntity4Sql);
            PublicUtils.fileTemplateDetailEntityListSort(fileTemplateDetailEntityList);

            //2.2设置表头，并且组装查询需要查询的字段
            //设置表头,并组装查询sql
            List<ExcelRow> excelRowList = new ArrayList<>();
            ExcelRow excelRowHead = new ExcelRow();
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

            //2.3获取该表的数据总量
            String sqlCount = "select count(*)  "+" from "+fileTableEntityBean.getTableName();
            int count = Integer.parseInt(mppMapper.mppSqlExecForSearch(sqlCount).get(0));
            if(0==count){
                continue;
            }
            //需要分表查询的次数

            Integer execTimes = count%perSize > 0 ? count/perSize+1:count/perSize;

            //2.4
            ExecutorService executorService = Executors.newFixedThreadPool(execTimes > 5 ? 5:execTimes);
            List<Callable<List<Map<String, Object>>>> cList = new ArrayList<>();  //定义添加线程的集合
            Callable<List<Map<String, Object>>> task = null;  //创建单个线程
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

            List<Future<List<Map<String, Object>>>> results = executorService.invokeAll(cList,30, TimeUnit.MINUTES); //执行所有创建的线程，并获取返回值（会把所有线程的返回值都返回）

            List<Map<String, Object>> records = new ArrayList<Map<String, Object>>();
            for(Future<List<Map<String, Object>>> recordPer:results){  //打印返回值
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

            //2.5组装excel
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
                //3.组装excel
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
