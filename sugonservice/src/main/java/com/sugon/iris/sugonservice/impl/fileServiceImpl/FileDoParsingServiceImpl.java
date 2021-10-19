package com.sugon.iris.sugonservice.impl.fileServiceImpl;

import com.sugon.iris.sugoncommon.publicUtils.PublicUtils;
import com.sugon.iris.sugondata.mybaties.mapper.db2.FileDetailMapper;
import com.sugon.iris.sugondata.mybaties.mapper.db2.FileParsingFailedMapper;
import com.sugon.iris.sugondata.mybaties.mapper.db2.FileTemplateDetailMapper;
import com.sugon.iris.sugondata.mybaties.mapper.db4.MppErrorInfoMapper;
import com.sugon.iris.sugondata.mybaties.mapper.db4.MppMapper;
import com.sugon.iris.sugondata.mybaties.mapper.db4.TableMapper;
import com.sugon.iris.sugondomain.beans.baseBeans.Error;
import com.sugon.iris.sugondomain.beans.fileBeans.ExcelRow;
import com.sugon.iris.sugondomain.dtos.fileDtos.FileRinseDetailDto;
import com.sugon.iris.sugondomain.dtos.fileDtos.FileTemplateDetailDto;
import com.sugon.iris.sugondomain.dtos.fileDtos.FileTemplateDto;
import com.sugon.iris.sugondomain.dtos.regularDtos.RegularDetailDto;
import com.sugon.iris.sugondomain.dtos.rinseBusinessDto.RinseBusinessNullDto;
import com.sugon.iris.sugondomain.dtos.rinseBusinessDto.RinseBusinessRepeatDto;
import com.sugon.iris.sugondomain.dtos.rinseBusinessDto.RinseBusinessReplaceDto;
import com.sugon.iris.sugondomain.dtos.rinseBusinessDto.RinseBusinessSuffixDto;
import com.sugon.iris.sugondomain.entities.mybatiesEntity.db2.FileDetailEntity;
import com.sugon.iris.sugondomain.entities.mybatiesEntity.db2.FileParsingFailedEntity;
import com.sugon.iris.sugondomain.entities.mybatiesEntity.db2.FileTemplateDetailEntity;
import com.sugon.iris.sugondomain.entities.mybatiesEntity.db4.MppErrorInfoEntity;
import com.sugon.iris.sugondomain.enums.ErrorCode_Enum;
import com.sugon.iris.sugonservice.service.fileService.FileDoParsingService;
import com.sugon.iris.sugonservice.service.rinseBusinessService.RinseBusinessService;
import de.siegmar.fastcsv.reader.CsvContainer;
import de.siegmar.fastcsv.reader.CsvReader;
import de.siegmar.fastcsv.reader.CsvRow;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import javax.annotation.Resource;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Slf4j
@Service
public class FileDoParsingServiceImpl implements FileDoParsingService {

    @Resource
    private MppErrorInfoMapper mppErrorInfoMapper;

    @Resource
    private TableMapper tableMapper;

    @Resource
    private FileParsingFailedMapper fileParsingFailedMapper;

    @Resource
    private MppMapper mppMapper;

    @Resource
    private FileDetailMapper fileDetailMapper;

    @Resource
    private RinseBusinessService rinseBusinessServiceImpl;

    @Resource
    private FileTemplateDetailMapper fileTemplateDetailMapper;

    /**
     *
     * @param caeId : 案件编号
     * @param fileTemplateDto : 模板
     * @param file  :文件
     * @param tableInfos : 要写入的mpp表信息[0]:tableName ;[1]: tableId
     * @param insertSql : 插入表的sql语句
     * @param regularMap : key:模板字段id编号；value：正则表达式列表；用于导入前数据校验
     * @param errorList
     * @throws IOException
     */
    @Override
    public void doParsingCsv(Long userId,Long caeId, FileTemplateDto fileTemplateDto, File file, Object[] tableInfos,
                             String insertSql,Map<Long, FileRinseDetailDto>  regularMap, Long fileSeq, Long fileAttachmentId,List<Error> errorList) throws IOException {

        boolean hasImport = true;
        String fileType = "csv";
        //数据总数
        Integer rowCount = 0;
        //导入数据总数
        Integer importRowCount = 0;

        //key:模板字段id，value:csv列索引
        Map<Long, Integer> feildRefIndexMap = new HashMap();
        CsvReader csvReader = new CsvReader();
        CsvContainer csvGbk = csvReader.read(file, Charset.forName("GBK"));
        List<CsvRow> rows = csvGbk.getRows();
        rowCount = rows.size() - 1;
        CsvRow csvRowHead = rows.get(0);
        //获取字段列表
        List<String> headList = csvRowHead.getFields();
        boolean flag = getFeildRefIndex(fileTemplateDto.getFileTemplateDetailDtoList(), feildRefIndexMap, headList);
        //如果没有匹配到数据,utf8-也解析一次。满足的情况下进行覆盖
        if (!flag) {
            CsvContainer csvUtf8 = csvReader.read(file, Charset.forName("UTF-8"));
            rows = csvUtf8.getRows();
            getFeildRefIndex(fileTemplateDto.getFileTemplateDetailDtoList(), feildRefIndexMap, headList);
        }

        //校验不通过列表,存入mysql
        List<FileParsingFailedEntity>  fileParsingFailedEntityListSql = new ArrayList<>();

        //存入mpp
        List<MppErrorInfoEntity> mppErrorInfoEntityList = new ArrayList<>();

        //对模板字段和csv列索引的对应关系进行遍历
        String mpp_insert_package_quantity = PublicUtils.getConfigMap().get("mpp_insert_package_quantity");

        //创建多线程，一个模板创建一个线程,在子线程内分别入库
        //ExecutorService executorServiceSqlInsert = Executors.newFixedThreadPool(20);
        String sqlInsertPackage = "";
        //对csv行进行遍历
        int k = 0;
        for(int i=1;i<rows.size();i++){
            if (!CollectionUtils.isEmpty(feildRefIndexMap)) {
                boolean mppId2ErrorId_flag = true;
                String sqlInsertExec = insertSql;
                for (Map.Entry<Long, Integer> entry : feildRefIndexMap.entrySet()) {
                    FileRinseDetailDto fileRinseDetailDto  =  regularMap.get(entry.getKey());
                    //任意一个满足
                    List<RegularDetailDto> regularDetailDtoListY = null;
                    //任意一个进行排除
                    List<RegularDetailDto> regularDetailDtoListN = null;
                    if(null != fileRinseDetailDto) {

                        regularDetailDtoListY = fileRinseDetailDto.getRegularDetailDtoListY();

                        regularDetailDtoListN = fileRinseDetailDto.getRegularDetailDtoListN();
                    }

                    /*
                     *feildRefIndexMap(key:模板字段，value：csv列序号)
                     * regularMap(key:模板字段，value：清洗字段)
                     * 清洗字段下挂了清洗正则表达式
                     * 这样csv的单元格数据与正则表达式进行对应
                     */
                    boolean checkRegular = false;
                    boolean checkRegularY = false;
                    if(!CollectionUtils.isEmpty(regularDetailDtoListY)){
                        for(RegularDetailDto regularDetailDto : regularDetailDtoListY){
                            if (rows.get(i).getField(entry.getValue()).replaceAll("\\s*", "").matches(regularDetailDto.getRegularValue().trim())) {
                                checkRegularY = true;
                                break;//只要匹配一个就跳出
                            }
                        }
                    }else{
                        checkRegularY = true;
                    }

                    boolean checkRegularN = true;
                    if(!CollectionUtils.isEmpty(regularDetailDtoListN)){
                        for (RegularDetailDto regularDetailDto : regularDetailDtoListN) {
                            if (rows.get(i).getField(entry.getValue()).replaceAll("\\s*", "").matches(regularDetailDto.getRegularValue().trim())) {
                                checkRegularN = false;
                                break;//只要匹配一个就跳出
                            }
                        }
                    }
                    checkRegular = checkRegularY && checkRegularN;
                    mppId2ErrorId_flag = checkRegular && mppId2ErrorId_flag;

                    //基础sql
                    sqlInsertExec = sqlInsertExec.replace("&&"+entry.getKey()+"&&",entry.getValue() == null?"": rows.get(i).getField(entry.getValue()).replaceAll("\\s*", ""));

                    //校验不通过
                   if(!checkRegular){
                       //检查excel的行号是否已经有了，有了用之前的mppid2errorid
                       //检查excel的行号是否已经有了，有了用之前的mppid2errorid
                       Long seq = null;
                       if(CollectionUtils.isEmpty(fileParsingFailedEntityListSql)){
                           seq = mppErrorInfoMapper.selectErrorSeq();
                       }else{
                           for(FileParsingFailedEntity fileParsingFailedEntityBean : fileParsingFailedEntityListSql){
                               if(fileParsingFailedEntityBean.getRowNumber().equals(String.valueOf(i))){
                                   seq = fileParsingFailedEntityBean.getMppId2ErrorId();
                                   break;
                               }
                           }
                       }
                       if(null == seq){
                           seq = mppErrorInfoMapper.selectErrorSeq();
                       }


                        FileParsingFailedEntity fileParsingFailedEntitySql = new FileParsingFailedEntity();
                        fileParsingFailedEntitySql.setRowNumber(String.valueOf(i));
                        fileParsingFailedEntitySql.setFileDetailId(fileSeq);
                        fileParsingFailedEntitySql.setFileTemplateId(fileTemplateDto.getId());
                        fileParsingFailedEntitySql.setFileTemplateDetailId(entry.getKey());
                        fileParsingFailedEntitySql.setContent(rows.get(i).getField(entry.getValue()));
                        fileParsingFailedEntitySql.setCaseId(caeId);
                        //mpp表名是唯一的
                        fileParsingFailedEntitySql.setTableName((String) tableInfos[0]);
                        fileParsingFailedEntitySql.setUserId(userId);
                        fileParsingFailedEntitySql.setMark(false);
                        fileParsingFailedEntitySql.setMppId2ErrorId(seq);
                        fileParsingFailedEntityListSql.add(fileParsingFailedEntitySql);

                       MppErrorInfoEntity mppErrorInfoEntity = new MppErrorInfoEntity();
                       mppErrorInfoEntity.setFileAttachmentId(fileAttachmentId);
                       mppErrorInfoEntity.setFileDetailId(fileSeq);
                       mppErrorInfoEntity.setFileRinseDetailId(fileRinseDetailDto.getId());
                       mppErrorInfoEntity.setMppid2errorid(seq);
                       mppErrorInfoEntity.setFileCaseId(caeId);
                       mppErrorInfoEntityList.add(mppErrorInfoEntity);
                       sqlInsertExec = sqlInsertExec.replace("&&xx_mppId2ErrorId_xx&&",String.valueOf(seq));
                    }
                }
                sqlInsertExec = sqlInsertExec.replace("&&xx_file_detail_id_xx&&",String.valueOf(fileSeq));
                //通过校验没有对应的错误信息 赋空值
                if(mppId2ErrorId_flag){
                    sqlInsertExec = sqlInsertExec.replace("&&xx_mppId2ErrorId_xx&&","0");
                    importRowCount ++;
                }
                if(false) {
                    sqlInsertPackage += sqlInsertExec;
                    if (++k == Integer.parseInt(mpp_insert_package_quantity) || i == rows.size() - 1) {

                        String execStr = new String(sqlInsertPackage);
                        /*
                        executorServiceSqlInsert.execute(new Runnable() {
                            @Override
                            public void run() {
                                mppMapper.mppSqlExec(execStr);
                            }
                        });
                         */
                        sqlInsertPackage = "";
                        k = 0;
                    }
                }else{
                    mppMapper.mppSqlExec(sqlInsertExec);
                    /*
                    String execStr = new String(sqlInsertExec) ;
                    executorServiceSqlInsert.execute(new Runnable() {
                        @Override
                        public void run() {
                            mppMapper.mppSqlExec(execStr);
                        }
                    });
                    */
                }
            }
        }

        //没有不满足的行，则返回
        if(!CollectionUtils.isEmpty(fileParsingFailedEntityListSql)){
            dealWithfailed(fileParsingFailedEntityListSql, mppErrorInfoEntityList);
        }
        //处理校验不满足数据
        saveFileDetail(userId, caeId, fileTemplateDto, file, tableInfos, fileSeq, fileAttachmentId, hasImport, fileType, rowCount, importRowCount);
    }

    /**
     * 解析 excel
     * @param caeId
     * @param fileTemplateDto
     * @param file
     * @param tableInfos
     * @param errorList
     */
    @Override
    public void doParsingExcel(Long userId,Long caeId, FileTemplateDto fileTemplateDto, File file, Object[] tableInfos,
                               String insertSql,Map<Long, FileRinseDetailDto>  regularMap, Long fileSeq, Long fileAttachmentId,List<Error> errorList) throws IOException, InvalidFormatException {
        boolean hasImport = true;
        String fileType = "excel";
        //数据总数
        Integer rowCount = 0;
        //导入数据总数
        Integer importRowCount = 0;

        //key:模板字段id，value:excel列索引
        Map<Long, Integer> feildRefIndexMap = new HashMap();

        //解析excel
        Workbook wb = null;
        if(file.getName().contains(".xlsx")){
            wb = new XSSFWorkbook(file);
        }else if(file.getName().contains(".xls")){
            FileInputStream fis = new FileInputStream(file);   //文件流对象
            wb = new HSSFWorkbook(fis);
        }else {
            errorList.add(new Error(ErrorCode_Enum.FILE_01_003.getCode(), ErrorCode_Enum.FILE_01_003.getMessage()));
            return;
        }
        //读取sheet0
        Sheet sheet = wb.getSheetAt(0);
        Row rowHead = sheet.getRow(0);
        List<String> headList = new ArrayList<>();
        //第一行是列名，所以不读
        int firstRowIndex = sheet.getFirstRowNum()+1;
        int lastRowIndex = sheet.getLastRowNum()+1;
        int firstCellIndex = rowHead.getFirstCellNum();
        int lastCellIndex = rowHead.getLastCellNum();
        rowCount = sheet.getLastRowNum();
        for(int i = firstCellIndex;i<lastCellIndex;i++){
            Cell cell = rowHead.getCell(i);
            headList.add(cell.getStringCellValue().replaceAll("\\s*", ""));
        }
        boolean flag = getFeildRefIndex(fileTemplateDto.getFileTemplateDetailDtoList(), feildRefIndexMap, headList);

        //校验不通过列表,存入mysql
        List<FileParsingFailedEntity>  fileParsingFailedEntityListSql = new ArrayList<>();

        //存入mpp
        List<MppErrorInfoEntity> mppErrorInfoEntityList = new ArrayList<>();

        //创建多线程，一个模板创建一个线程,在子线程内分别入库
        //ExecutorService executorServiceSqlInsert = Executors.newFixedThreadPool(20);
        //对excel行进行遍历,获取list集合行列表
        List<ExcelRow> excelRowList = null;
        long aa_02 = System.currentTimeMillis();
        for(int i = firstRowIndex;i<lastRowIndex;i++){
            if (!CollectionUtils.isEmpty(feildRefIndexMap)) {
                boolean mppId2ErrorId_flag = true;
                String sqlInsertExec = insertSql;
                for (Map.Entry<Long, Integer> entry : feildRefIndexMap.entrySet()) {
                    long aa_03 = System.currentTimeMillis();
                    FileRinseDetailDto fileRinseDetailDto  =  regularMap.get(entry.getKey());
                    //任意一个满足
                    List<RegularDetailDto> regularDetailDtoListY = null;
                    //任意一个进行排除
                    List<RegularDetailDto> regularDetailDtoListN = null;
                    if(null != fileRinseDetailDto) {

                        regularDetailDtoListY = fileRinseDetailDto.getRegularDetailDtoListY();

                        regularDetailDtoListN = fileRinseDetailDto.getRegularDetailDtoListN();
                    }

                    /*
                     *feildRefIndexMap(key:模板字段，value：csv列序号)
                     * regularMap(key:模板字段，value：清洗字段)
                     * 清洗字段下挂了清洗正则表达式
                     * 这样csv的单元格数据与正则表达式进行对应
                     */
                    boolean checkRegular = false;
                    //把所有的单元格变为string类型
                    sheet.getRow(i).getCell(entry.getValue()).setCellType(Cell.CELL_TYPE_STRING);
                    long bb_05 = System.currentTimeMillis();
                    long cc_05 = bb_05-aa_03;
                    log.info("cc_05="+cc_05+"");
                    boolean checkRegularY = false;
                    if(!CollectionUtils.isEmpty(regularDetailDtoListY)){
                        for(RegularDetailDto regularDetailDto : regularDetailDtoListY){

                            if (sheet.getRow(i).getCell(entry.getValue()).getStringCellValue().replaceAll("\\s*", "").matches(regularDetailDto.getRegularValue().trim())) {
                                checkRegularY = true;
                                break;//只要匹配一个就跳出
                            }
                        }
                    }else{
                        checkRegularY = true;
                    }

                    boolean checkRegularN = true;
                    if(!CollectionUtils.isEmpty(regularDetailDtoListN)){
                        for (RegularDetailDto regularDetailDto : regularDetailDtoListN) {
                            if (sheet.getRow(i).getCell(entry.getValue()).getStringCellValue().replaceAll("\\s*", "").matches(regularDetailDto.getRegularValue().trim())) {
                                checkRegularN = false;
                                break;//只要匹配一个就跳出
                            }
                        }
                    }


                    checkRegular = checkRegularY && checkRegularN;
                    mppId2ErrorId_flag = checkRegular && mppId2ErrorId_flag;

                    //基础sql
                    sqlInsertExec = sqlInsertExec.replace("&&"+entry.getKey()+"&&",entry.getValue() == null?"": sheet.getRow(i).getCell(entry.getValue()).getStringCellValue().replaceAll("\\s*", ""));

                    long bb_06 = System.currentTimeMillis();
                    long cc_06 = bb_06-bb_05;
                    log.info("cc_06="+cc_06+"");

                    //校验不通过
                    if(!checkRegular){

                        long aa_09 = System.currentTimeMillis();

                        //检查excel的行号是否已经有了，有了用之前的mppid2errorid
                        Long seq = null;
                        if(CollectionUtils.isEmpty(fileParsingFailedEntityListSql)){
                            seq = mppErrorInfoMapper.selectErrorSeq();
                        }else{
                            for(FileParsingFailedEntity fileParsingFailedEntityBean : fileParsingFailedEntityListSql){
                                if(fileParsingFailedEntityBean.getRowNumber().equals(String.valueOf(i))){
                                    seq = fileParsingFailedEntityBean.getMppId2ErrorId();
                                    break;
                                }
                            }
                        }

                        if(null == seq){
                            seq = mppErrorInfoMapper.selectErrorSeq();
                        }
                        FileParsingFailedEntity fileParsingFailedEntitySql = new FileParsingFailedEntity();
                        fileParsingFailedEntitySql.setRowNumber(String.valueOf(i));
                        fileParsingFailedEntitySql.setFileDetailId(fileSeq);
                        fileParsingFailedEntitySql.setFileTemplateId(fileTemplateDto.getId());
                        fileParsingFailedEntitySql.setFileTemplateDetailId(entry.getKey());

                        long bb_09 = System.currentTimeMillis();
                        long cc_09 = bb_09-aa_09;
                        log.info("cc_09="+cc_09+"");

                        long aa_08 = System.currentTimeMillis();
                        fileParsingFailedEntitySql.setContent(sheet.getRow(i).getCell(entry.getValue()).getStringCellValue());
                        long bb_08 = System.currentTimeMillis();
                        long cc_08 = bb_08-aa_08;
                        log.info("cc_08="+cc_08+"");

                        fileParsingFailedEntitySql.setCaseId(caeId);
                        //mpp表名是唯一的
                        fileParsingFailedEntitySql.setTableName((String) tableInfos[0]);
                        fileParsingFailedEntitySql.setUserId(userId);
                        fileParsingFailedEntitySql.setMark(false);
                        fileParsingFailedEntitySql.setMppId2ErrorId(seq);
                        fileParsingFailedEntityListSql.add(fileParsingFailedEntitySql);

                        MppErrorInfoEntity mppErrorInfoEntity = new MppErrorInfoEntity();
                        mppErrorInfoEntity.setFileAttachmentId(fileAttachmentId);
                        mppErrorInfoEntity.setFileDetailId(fileSeq);
                        mppErrorInfoEntity.setFileRinseDetailId(fileRinseDetailDto.getId());
                        mppErrorInfoEntity.setMppid2errorid(seq);
                        mppErrorInfoEntity.setFileCaseId(caeId);
                        mppErrorInfoEntityList.add(mppErrorInfoEntity);
                        sqlInsertExec = sqlInsertExec.replace("&&xx_mppId2ErrorId_xx&&",String.valueOf(seq));
                    }

                    long bb_07 = System.currentTimeMillis();
                    long cc_07 = bb_07-bb_06;
                    log.info("cc_07="+cc_07+"");
                }
                sqlInsertExec = sqlInsertExec.replace("&&xx_file_detail_id_xx&&",String.valueOf(fileSeq));
                //通过校验没有对应的错误信息 赋空值
                if(mppId2ErrorId_flag){
                    sqlInsertExec = sqlInsertExec.replace("&&xx_mppId2ErrorId_xx&&","0");
                    importRowCount ++;
                }


                long aa_04 = System.currentTimeMillis();
                mppMapper.mppSqlExec(sqlInsertExec);
                long bb_04 = System.currentTimeMillis();
                long cc_04 = bb_04-aa_04;
                log.info("cc_04="+cc_04+"");
                /*
                String execStr = new String(sqlInsertExec) ;
                executorServiceSqlInsert.execute(new Runnable() {
                    @Override
                    public void run() {
                        mppMapper.mppSqlExec(execStr);
                    }
                });
                */
            }
        }
        long bb_02= System.currentTimeMillis();
        long cc_02 = bb_02-aa_02;
        log.info("cc_02="+cc_02+"");
        //没有不满足的行，则返回
        if(!CollectionUtils.isEmpty(fileParsingFailedEntityListSql)){
            //处理校验不满足数据
            dealWithfailed(fileParsingFailedEntityListSql, mppErrorInfoEntityList);
        }
        //保存文件信息
        saveFileDetail(userId, caeId, fileTemplateDto, file, tableInfos, fileSeq, fileAttachmentId, hasImport, fileType, rowCount, importRowCount);

        long bb_01= System.currentTimeMillis();
        long cc_01 = bb_01-bb_02;
        log.info("cc_01="+cc_01+"");
    }


    @Override
    public void doRinse(FileTemplateDto fileTemplateDto, Object[] tableInfos , Long fileSeq , List<Error> errorList) throws IllegalAccessException {
        //通过模板id获取清洗信息
        List<RinseBusinessNullDto> rinseBusinessNullDtoList = rinseBusinessServiceImpl.getNullBussList(fileTemplateDto.getId(),errorList);
        List<RinseBusinessRepeatDto> rinseBusinessRepeatDtoList = rinseBusinessServiceImpl.getRepetBussList(fileTemplateDto.getId(),errorList);
        List<RinseBusinessReplaceDto> rinseBusinessReplaceDtoList = rinseBusinessServiceImpl.getReplaceBussList(fileTemplateDto.getId(),errorList);
        List<RinseBusinessSuffixDto>  rinseBusinessSuffixDtoList = rinseBusinessServiceImpl.getSuffixBussList(fileTemplateDto.getId(),errorList);

        //放入子线程中删除
        List<Long> mppid2erroridDeleteList = new ArrayList<>();

        //进行null替换
        String sql = "update "+tableInfos[0] +" _&condition&_ ";
        String nullSql = "";
        String whereSql = "";
        if(!CollectionUtils.isEmpty(rinseBusinessNullDtoList)){
              for(int i =0 ;i< rinseBusinessNullDtoList.size();i++){
                    //获取需要更新的字段
                  FileTemplateDetailEntity fileTemplateDetailEntity =  fileTemplateDetailMapper.selectFileTemplateDetailByPrimary(rinseBusinessNullDtoList.get(i).getFileTemplateDetailId());
                  nullSql = "set " + fileTemplateDetailEntity.getFieldName() + "='" + rinseBusinessNullDtoList.get(i).getValue() + "' ";
                  whereSql  = fileTemplateDetailEntity.getFieldName() +" is null or "+
                                  fileTemplateDetailEntity.getFieldName()+"= 'null' or  trim("+
                                  fileTemplateDetailEntity.getFieldName()+")='' and  file_detail_id ="+fileSeq;
                  sql = sql.replace("_&condition&_",nullSql + " where  "+whereSql);
                  mppMapper.mppSqlExec(sql);
              }
        }

        //去重
        String repeatSql = "select id,mppid2errorid  from " +
                "(select row_number() OVER(PARTITION BY _&condition&_) AS rownum,id,mppid2errorid " +
                "  from "+tableInfos[0]+"  where file_detail_id = "+fileSeq+")a where a.rownum > 1";
        String repeatCondition = "";
        if(!CollectionUtils.isEmpty(rinseBusinessRepeatDtoList)){
            for(RinseBusinessRepeatDto rinseBusinessRepeatDto : rinseBusinessRepeatDtoList) {
                //通过id获取字段名
                String fields = rinseBusinessRepeatDto.getFields();
                String[] fieldArr = fields.split(",");
                for(int i =0 ;i < fieldArr.length;i++) {
                    //通过id获取字段名
                    FileTemplateDetailEntity fileTemplateDetailEntity = fileTemplateDetailMapper.selectFileTemplateDetailByPrimary(Long.parseLong(fieldArr[i]));
                    if(i < fieldArr.length -1) {
                        repeatCondition += fileTemplateDetailEntity.getFieldName()+",";
                    }else{
                        repeatCondition += fileTemplateDetailEntity.getFieldName();
                    }
                }
                repeatSql = repeatSql.replace("_&condition&_",repeatCondition);
                List<Map<String,Object>> mapList =  mppMapper.mppSqlExecForSearchRtMapList(repeatSql);
                if(!CollectionUtils.isEmpty(mapList)){
                    for(Map map : mapList) {
                        String idStr = map.get("id")+"";
                        Long id = Long.parseLong(idStr);
                        String deleteSql = "DELETE FROM "+tableInfos[0]+" WHERE id = "+id;
                        mppMapper.mppSqlExec(deleteSql);
                        Long mppid2errorid = (Long)map.get("mppid2errorid");
                        if(null != mppid2errorid && mppid2errorid != 0){
                            mppErrorInfoMapper.deleteMppErrorInfoByMppid2errorid(mppid2errorid);
                            mppid2erroridDeleteList.add(mppid2errorid);
                            //fileParsingFailedMapper.deleteFileParsingFailedByMppid2errorid(mppid2errorid);
                        }
                    }
                }
            }
        }

        //字段关键字进行替换
        String replaceSql = "update "+tableInfos[0] +" set _&condition&_";
        if(!CollectionUtils.isEmpty(rinseBusinessReplaceDtoList)){
            for(RinseBusinessReplaceDto rinseBusinessReplaceDto : rinseBusinessReplaceDtoList){
                FileTemplateDetailEntity fileTemplateDetailEntity = fileTemplateDetailMapper.selectFileTemplateDetailByPrimary(rinseBusinessReplaceDto.getFileTemplateDetailId());
                String condition = fileTemplateDetailEntity.getFieldName() + "=regexp_replace("+fileTemplateDetailEntity.getFieldName()+",'"+rinseBusinessReplaceDto.getKey()+"','"+rinseBusinessReplaceDto.getValue()+"')" +
                                   "where "+fileTemplateDetailEntity.getFieldName()+" ~ '" +rinseBusinessReplaceDto.getKey()+"' and file_detail_id="+fileSeq;
                replaceSql = replaceSql.replace("_&condition&_",condition);
                mppMapper.mppSqlExec(replaceSql);
            }
        }

        //去除后缀
        String suffixSql = "update "+tableInfos[0] +" set _&condition&_";
        if(!CollectionUtils.isEmpty(rinseBusinessSuffixDtoList)){
            for(RinseBusinessSuffixDto rinseBusinessSuffixDto : rinseBusinessSuffixDtoList){
                FileTemplateDetailEntity fileTemplateDetailEntity = fileTemplateDetailMapper.selectFileTemplateDetailByPrimary(rinseBusinessSuffixDto.getFileTemplateDetailId());
                String condition = fileTemplateDetailEntity.getFieldName() + "=  reverse(substr(reverse("+fileTemplateDetailEntity.getFieldName()+"),position('"+rinseBusinessSuffixDto.getSuffix()+"' in reverse("+fileTemplateDetailEntity.getFieldName()+"))+1))"+
                                   " where "+fileTemplateDetailEntity.getFieldName()+" like '%" +rinseBusinessSuffixDto.getSuffix()+"%' and file_detail_id="+fileSeq;
                suffixSql = suffixSql.replace("_&condition&_",condition);
                mppMapper.mppSqlExec(suffixSql);
            }
        }

        //mysql数据集中多线程统一删除
        ExecutorService executorServiceSqlDelete = Executors.newFixedThreadPool(3);
        executorServiceSqlDelete.execute(new Runnable() {
            @Override
            public void run() {
                for(Long mppid2errorid :mppid2erroridDeleteList) {
                    fileParsingFailedMapper.deleteFileParsingFailedByMppid2errorid(mppid2errorid);
                }
            }
        });

    }

    private void saveFileDetail(Long userId, Long caeId, FileTemplateDto fileTemplateDto, File file, Object[] tableInfos, Long fileSeq, Long fileAttachmentId, boolean hasImport, String fileType, Integer rowCount, Integer importRowCount) {
        //把文件信息存入文件信息表
        FileDetailEntity fileDetailEntityfSql = new FileDetailEntity();
        fileDetailEntityfSql.setId(fileSeq);
        fileDetailEntityfSql.setFileTemplateId(fileTemplateDto.getId());
        fileDetailEntityfSql.setUserId(userId);
        fileDetailEntityfSql.setCaseId(caeId);
        fileDetailEntityfSql.setFileAttachmentId(fileAttachmentId);
        fileDetailEntityfSql.setFileName(file.getName());
        fileDetailEntityfSql.setFilePath(file.getAbsolutePath());
        fileDetailEntityfSql.setFileType(fileType);
        fileDetailEntityfSql.setHasImport(hasImport);
        fileDetailEntityfSql.setImportRowCount(importRowCount);
        fileDetailEntityfSql.setRowCount(rowCount);
        fileDetailEntityfSql.setTableName((String) tableInfos[0]);
        fileDetailEntityfSql.setFileTableId((Long) tableInfos[1]);
        //把信息存入文件信息表
        fileDetailMapper.fileDetailInsert(fileDetailEntityfSql);
    }

    private void dealWithfailed(List<FileParsingFailedEntity> fileParsingFailedEntityListSql, List<MppErrorInfoEntity> mppErrorInfoEntityList) {
        ExecutorService fileParsingFailedInsert = Executors.newFixedThreadPool(5);
        int errorPackage = 0;
        //进行分包存库
        List<FileParsingFailedEntity> fileParsingFailedEntityPackage = new ArrayList<>();
        for(int i =0 ;i<fileParsingFailedEntityListSql.size();i++){
            fileParsingFailedEntityPackage.add(fileParsingFailedEntityListSql.get(i));
            if((++errorPackage == 10) || (i== fileParsingFailedEntityListSql.size()-1)){
                //多线程避免脏读
                List<FileParsingFailedEntity> fileParsingFailedEntityinsertList  = new ArrayList<>();
                fileParsingFailedEntityinsertList.addAll(fileParsingFailedEntityPackage);
                fileParsingFailedInsert.execute(new Runnable() {
                    @Override
                    public void run() {
                       fileParsingFailedMapper.fileParsingFailedInsert(fileParsingFailedEntityinsertList);
                    }
                });
                fileParsingFailedEntityPackage = new ArrayList<>();
            }
        }


        //进行分包存库
        List<MppErrorInfoEntity> mppErrorInfoEntityListPackage = new ArrayList<>();
        for(int i =0 ;i<mppErrorInfoEntityList.size();i++){
            mppErrorInfoEntityListPackage.add(mppErrorInfoEntityList.get(i));
            if((++errorPackage == 10) || (i== fileParsingFailedEntityListSql.size()-1)){
                //多线程避免脏读
                List<MppErrorInfoEntity> mppErrorInfoEntityInserttList  = new ArrayList<>();
                mppErrorInfoEntityInserttList.addAll(mppErrorInfoEntityListPackage);
                fileParsingFailedInsert.execute(new Runnable() {
                    @Override
                    public void run() {
                        mppErrorInfoMapper.errorInfoListInsert(mppErrorInfoEntityInserttList);
                    }
                });
                mppErrorInfoEntityListPackage = new ArrayList<>();
            }
        }
    }

    //获取csv和排序字段的对应关系
    private boolean getFeildRefIndex(List<FileTemplateDetailDto> fileTemplateDetailDtoList, Map<Long, Integer> feildRefIndex, List<String> headList) {
        boolean result = false;
        if(!CollectionUtils.isEmpty(fileTemplateDetailDtoList) && !CollectionUtils.isEmpty(headList) ){
            for(FileTemplateDetailDto feild : fileTemplateDetailDtoList){
                feildRefIndex.put(feild.getId(),null);//没有对应的表头也要保留到map中，为insertsql保留所有字段
                String[] excludeList =feild.getExclude().split("&&") ;
                String[] keyList = feild.getFieldKey().split("&&");
                breakFor:  for(int i=0;i<headList.size();i++){
                    //关键字排除
                    if(null != excludeList && excludeList.length>0 ){
                        for(String exclude:excludeList){
                            if(StringUtils.isNotEmpty(exclude) && headList.get(i).contains(exclude.trim())){
                                break  breakFor;
                            }
                        }
                    }
                    //关键字匹配
                    if(null != keyList && keyList.length>0 ){
                        for(String key:keyList){
                            if(StringUtils.isNotEmpty(key) && headList.get(i).contains(key.trim())){
                                feildRefIndex.put(feild.getId(),i);
                                result = true;
                                break ;
                            }
                        }
                    }
                }
            }
        }
        return result;
    }
}
