package com.sugon.iris.sugonservice.impl.fileServiceImpl;

import org.postgresql.copy.CopyManager;
import org.postgresql.core.BaseConnection;
import com.sugon.iris.sugondata.config.GaussDBConfig.GaussConnection;
import com.sugon.iris.sugondata.mybaties.mapper.db2.FileDetailMapper;
import com.sugon.iris.sugondata.mybaties.mapper.db2.FileFieldCompleteMapper;
import com.sugon.iris.sugondata.mybaties.mapper.db2.FileParsingFailedMapper;
import com.sugon.iris.sugondata.mybaties.mapper.db2.FileTemplateDetailMapper;
import com.sugon.iris.sugondata.mybaties.mapper.db4.MppErrorInfoMapper;
import com.sugon.iris.sugondata.mybaties.mapper.db4.MppMapper;
import com.sugon.iris.sugondomain.beans.baseBeans.Error;
import com.sugon.iris.sugondomain.dtos.fileDtos.FileRinseDetailDto;
import com.sugon.iris.sugondomain.dtos.fileDtos.FileTemplateDetailDto;
import com.sugon.iris.sugondomain.dtos.fileDtos.FileTemplateDto;
import com.sugon.iris.sugondomain.dtos.regularDtos.RegularDetailDto;
import com.sugon.iris.sugondomain.dtos.rinseBusinessDto.*;
import com.sugon.iris.sugondomain.entities.mybatiesEntity.db2.FileDetailEntity;
import com.sugon.iris.sugondomain.entities.mybatiesEntity.db2.FileFieldCompleteEntity;
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
import java.io.*;
import java.nio.charset.Charset;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.*;
import java.util.concurrent.*;

@Slf4j
@Service
public class FileDoParsingServiceImpl implements FileDoParsingService {

    public static final String quote = "^";
    public static final String delimter = "|";

    @Resource
    private MppErrorInfoMapper mppErrorInfoMapper;

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

    @Resource
    private FileFieldCompleteMapper fileFieldCompleteMapper;



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
    public void doParsingCsv( Long userId,Long caeId, FileTemplateDto fileTemplateDto, File file, Object[] tableInfos,
                             String insertSql,Map<Long, FileRinseDetailDto>  regularMap, Long fileSeq, Long fileAttachmentId,List<Error> errorList) throws IOException {
        try {
            //获取模板的目标补全字段
      Set<Long> fieldDestList = getFieldDestList(fileTemplateDto);


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

      //判断文件头是否有重复,或者字段头是空的，有重复不进行存库,并把错误信息存入文件信息表
      if (isHeadRepeat(userId, caeId, headList, file, fileSeq, fileAttachmentId, errorList)) {
          return;
      }

      boolean flag = getFeildRefIndex(fileTemplateDto.getFileTemplateDetailDtoList(), feildRefIndexMap, headList);

      //校验列数是否匹配
      if (checkFieldSize(userId, caeId, fileTemplateDto, file, tableInfos, fileSeq, fileAttachmentId, errorList, fileType, feildRefIndexMap, rows)) {
          return;
      }

      //如果没有匹配到数据,utf8-也解析一次。满足的情况下进行覆盖
      if (!flag) {
          CsvContainer csvUtf8 = csvReader.read(file, Charset.forName("UTF-8"));
          rows = csvUtf8.getRows();
          getFeildRefIndex(fileTemplateDto.getFileTemplateDetailDtoList(), feildRefIndexMap, headList);
      }

      //校验不通过列表,存入mysql
      List<FileParsingFailedEntity> fileParsingFailedEntityListSql = new ArrayList<>();


            //创建多线程，一个模板创建一个线程,在子线程内分别入库
      int thread = 1;
      if(rowCount > 4000){
          thread = 2;
      }else if(rowCount > 12000){
          thread = 5;
            }
      else if(rowCount > 50000){
          thread = 8;
      }

      StringBuffer errorBuffer = new StringBuffer();
      //不开起多线程
     if(thread == 1){
          importRowCount = singleThreadedParsingCsv(userId, caeId, fileTemplateDto, tableInfos, insertSql, regularMap, fileSeq, fileAttachmentId, fieldDestList, importRowCount, feildRefIndexMap, rows, fileParsingFailedEntityListSql, errorBuffer);
      }
      //多线程
      else{
          importRowCount = multithreadedParsing(thread,userId, caeId, fileTemplateDto, tableInfos, insertSql, regularMap, fileSeq, fileAttachmentId, fieldDestList, importRowCount, feildRefIndexMap, rows, fileParsingFailedEntityListSql, errorBuffer);

      }


      //有不满足的行，则保存不满足的信息
      if (!CollectionUtils.isEmpty(fileParsingFailedEntityListSql)) {
          dealWithfailed(fileParsingFailedEntityListSql, errorBuffer);
      }
      //保存文件信息
      saveFileDetail(userId, caeId, fileTemplateDto, file, tableInfos, fileSeq, fileAttachmentId, true, fileType, rowCount, importRowCount);

      }catch (Exception e){
        errorList.add(new Error(ErrorCode_Enum.FILE_01_016.getCode(), ErrorCode_Enum.FILE_01_016.getMessage(),e.toString()));
        e.printStackTrace();
     }
    }

    private Integer multithreadedParsing(int thread,Long userId, Long caeId, FileTemplateDto fileTemplateDto, Object[] tableInfos, String insertSql, Map<Long, FileRinseDetailDto> regularMap, Long fileSeq, Long fileAttachmentId, Set<Long> fieldDestList, Integer importRowCount, Map<Long, Integer> feildRefIndexMap, List<CsvRow> rows, List<FileParsingFailedEntity> fileParsingFailedEntityListSql, StringBuffer errorBuffer) throws SQLException, IOException, InterruptedException, ExecutionException {

        ExecutorService executorService = Executors.newFixedThreadPool(thread);

        List<Callable<Long>> cList = new ArrayList<>();  //定义添加线程的集合

        Callable<Long> task = null;  //创建单个线程

        StringBuffer tuples = new StringBuffer();

        //对csv行进行遍历
        int k = 0;
        for (int i = 1; i < rows.size(); i++) {
            if(CollectionUtils.isEmpty(rows.get(i).getFields())){
                continue;
            }
            if (!CollectionUtils.isEmpty(feildRefIndexMap)) {
                boolean mppId2ErrorId_flag = true;
                String sqlInsertExec = insertSql;
                for (Map.Entry<Long, Integer> entry : feildRefIndexMap.entrySet()) {
                    FileRinseDetailDto fileRinseDetailDto = regularMap.get(entry.getKey());
                    //任意一个满足
                    List<RegularDetailDto> regularDetailDtoListY = null;
                    //任意一个进行排除
                    List<RegularDetailDto> regularDetailDtoListN = null;
                    if (null != fileRinseDetailDto && !fieldDestList.contains(entry.getKey())) {//如果是补全配置的 目标 字段，则不在这里进行正则校验

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
                    if (!CollectionUtils.isEmpty(regularDetailDtoListY)) {
                        for (RegularDetailDto regularDetailDto : regularDetailDtoListY) {
                            if (rows.get(i).getField(entry.getValue()).replaceAll("\\s*", "").matches(regularDetailDto.getRegularValue().trim())) {
                                checkRegularY = true;
                                break;//只要匹配一个就跳出
                            }
                        }
                    } else {
                        checkRegularY = true;
                    }

                    boolean checkRegularN = true;
                    if (!CollectionUtils.isEmpty(regularDetailDtoListN)) {
                        for (RegularDetailDto regularDetailDto : regularDetailDtoListN) {
                            if (rows.get(i).getField(entry.getValue()).replaceAll("\\s*", "").matches(regularDetailDto.getRegularValue().trim())) {
                                checkRegularN = false;
                                break;//只要匹配一个就跳出
                            }
                        }
                    }
                    checkRegular = checkRegularY && checkRegularN;
                    mppId2ErrorId_flag = checkRegular && mppId2ErrorId_flag;
                    sqlInsertExec = sqlInsertExec.replace("&&" + entry.getKey() + "&&", (null == entry.getValue() || null == rows.get(i).getField(entry.getValue())) ? "" : rows.get(i).getField(entry.getValue()).replaceAll("\\s*", ""));

                    //校验不通过
                    if (!checkRegular) {
                        //检查excel的行号是否已经有了，有了用之前的mppid2errorid
                        //检查excel的行号是否已经有了，有了用之前的mppid2errorid
                        Long seq = null;
                        if (CollectionUtils.isEmpty(fileParsingFailedEntityListSql)) {
                            seq = mppErrorInfoMapper.selectErrorSeq();
                        } else {
                            for (FileParsingFailedEntity fileParsingFailedEntityBean : fileParsingFailedEntityListSql) {
                                if (fileParsingFailedEntityBean.getRowNumber().equals(String.valueOf(i))) {
                                    seq = fileParsingFailedEntityBean.getMppId2ErrorId();
                                    break;
                                }
                            }
                        }
                        if (null == seq) {
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
                        fileParsingFailedEntitySql.setFileAttachmentId(fileAttachmentId);
                        fileParsingFailedEntityListSql.add(fileParsingFailedEntitySql);

                        MppErrorInfoEntity mppErrorInfoEntity = new MppErrorInfoEntity();
                        mppErrorInfoEntity.setFileAttachmentId(fileAttachmentId);
                        mppErrorInfoEntity.setFileDetailId(fileSeq);
                        mppErrorInfoEntity.setFileRinseDetailId(fileRinseDetailDto.getId());
                        mppErrorInfoEntity.setMppid2errorid(seq);
                        mppErrorInfoEntity.setFileCaseId(caeId);
                        mppErrorInfoEntity.setMppTableName((String)tableInfos[0]);
                        //mppErrorInfoEntityList.add(mppErrorInfoEntity);
                        Long idSeq = mppMapper.selectSeq("error_info_id_seq");
                        mppErrorInfoEntity.setId(idSeq);
                        errorBuffer.append(mppErrorInfoEntity.toString());
                        sqlInsertExec = sqlInsertExec.replace("&&xx_mppId2ErrorId_xx&&", String.valueOf(seq));
                    }
                }
                sqlInsertExec = sqlInsertExec.replace("&&xx_file_detail_id_xx&&", String.valueOf(fileSeq));
                //通过校验没有对应的错误信息 赋空值
                if (mppId2ErrorId_flag) {
                    sqlInsertExec = sqlInsertExec.replace("&&xx_mppId2ErrorId_xx&&", "0");
                    importRowCount++;
                }

                //塞id值
                Long id = mppMapper.selectSeq((String)tableInfos[0]+"_id_seq");
                sqlInsertExec = sqlInsertExec.replace("&&xx_id_xx&&", String.valueOf(id));
                tuples.append(sqlInsertExec);
                k++;
                if(k == 4000 || i == rows.size()-1) {
                    k =0 ;
                    StringBuffer tuplesBak = new StringBuffer(tuples);
                    tuples = new StringBuffer();
                    task = new Callable<Long>() {
                        @Override
                        public Long call() throws Exception {
                            //数据入mpp库
                         Long records =   saveToGaussdb(tuplesBak, (String) tableInfos[0]);
                         return records;
                        }
                    };
                    cList.add(task);

                }
            }
        }
        List<Future<Long>> results = executorService.invokeAll(cList, 30, TimeUnit.MINUTES); //执行所有创建的线程，并获取返回值（会把所有线程的返回值都返回）
        for (Future<Long> str : results) {  //打印返回值
            log.info(String.valueOf(str.get()));
        }
        executorService.shutdown();
        return importRowCount;
    }

    private Integer singleThreadedParsingCsv(Long userId, Long caeId, FileTemplateDto fileTemplateDto, Object[] tableInfos, String insertSql, Map<Long, FileRinseDetailDto> regularMap, Long fileSeq, Long fileAttachmentId, Set<Long> fieldDestList, Integer importRowCount, Map<Long, Integer> feildRefIndexMap, List<CsvRow> rows, List<FileParsingFailedEntity> fileParsingFailedEntityListSql, StringBuffer errorBuffer) throws SQLException, IOException {
        //对csv行进行遍历
        StringBuffer tuples = new StringBuffer();
        for (int i = 1; i < rows.size(); i++) {
            if(CollectionUtils.isEmpty(rows.get(i).getFields())){
              continue;
            }
            if (!CollectionUtils.isEmpty(feildRefIndexMap)) {
                boolean mppId2ErrorId_flag = true;
                String sqlInsertExec = insertSql;
                for (Map.Entry<Long, Integer> entry : feildRefIndexMap.entrySet()) {
                    FileRinseDetailDto fileRinseDetailDto = regularMap.get(entry.getKey());
                    //任意一个满足
                    List<RegularDetailDto> regularDetailDtoListY = null;
                    //任意一个进行排除
                    List<RegularDetailDto> regularDetailDtoListN = null;
                    if (null != fileRinseDetailDto && !fieldDestList.contains(entry.getKey())) {//如果是补全配置的 目标 字段，则不在这里进行正则校验

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
                    if (!CollectionUtils.isEmpty(regularDetailDtoListY)) {
                        for (RegularDetailDto regularDetailDto : regularDetailDtoListY) {
                            if (rows.get(i).getField(entry.getValue()).replaceAll("\\s*", "").matches(regularDetailDto.getRegularValue().trim())) {
                                checkRegularY = true;
                                break;//只要匹配一个就跳出
                            }
                        }
                    } else {
                        checkRegularY = true;
                    }

                    boolean checkRegularN = true;
                    if (!CollectionUtils.isEmpty(regularDetailDtoListN)) {
                        for (RegularDetailDto regularDetailDto : regularDetailDtoListN) {
                            if (rows.get(i).getField(entry.getValue()).replaceAll("\\s*", "").matches(regularDetailDto.getRegularValue().trim())) {
                                checkRegularN = false;
                                break;//只要匹配一个就跳出
                            }
                        }
                    }
                    checkRegular = checkRegularY && checkRegularN;
                    mppId2ErrorId_flag = checkRegular && mppId2ErrorId_flag;
                    sqlInsertExec = sqlInsertExec.replace("&&" + entry.getKey() + "&&", (null == entry.getValue() || null == rows.get(i).getField(entry.getValue())) ? "" : rows.get(i).getField(entry.getValue()).replaceAll("\\s*", ""));

                    //校验不通过
                    if (!checkRegular) {
                        //检查excel的行号是否已经有了，有了用之前的mppid2errorid
                        //检查excel的行号是否已经有了，有了用之前的mppid2errorid
                        Long seq = null;
                        if (CollectionUtils.isEmpty(fileParsingFailedEntityListSql)) {
                            seq = mppErrorInfoMapper.selectErrorSeq();
                        } else {
                            for (FileParsingFailedEntity fileParsingFailedEntityBean : fileParsingFailedEntityListSql) {
                                if (fileParsingFailedEntityBean.getRowNumber().equals(String.valueOf(i))) {
                                    seq = fileParsingFailedEntityBean.getMppId2ErrorId();
                                    break;
                                }
                            }
                        }
                        if (null == seq) {
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
                        fileParsingFailedEntitySql.setFileAttachmentId(fileAttachmentId);
                        fileParsingFailedEntityListSql.add(fileParsingFailedEntitySql);

                        MppErrorInfoEntity mppErrorInfoEntity = new MppErrorInfoEntity();
                        mppErrorInfoEntity.setFileAttachmentId(fileAttachmentId);
                        mppErrorInfoEntity.setFileDetailId(fileSeq);
                        mppErrorInfoEntity.setFileRinseDetailId(fileRinseDetailDto.getId());
                        mppErrorInfoEntity.setMppid2errorid(seq);
                        mppErrorInfoEntity.setFileCaseId(caeId);
                        mppErrorInfoEntity.setMppTableName((String)tableInfos[0]);
                        //mppErrorInfoEntityList.add(mppErrorInfoEntity);
                        Long idSeq = mppMapper.selectSeq("error_info_id_seq");
                        mppErrorInfoEntity.setId(idSeq);
                        errorBuffer.append(mppErrorInfoEntity.toString());
                        sqlInsertExec = sqlInsertExec.replace("&&xx_mppId2ErrorId_xx&&", String.valueOf(seq));
                    }
                }
                sqlInsertExec = sqlInsertExec.replace("&&xx_file_detail_id_xx&&", String.valueOf(fileSeq));
                //通过校验没有对应的错误信息 赋空值
                if (mppId2ErrorId_flag) {
                    sqlInsertExec = sqlInsertExec.replace("&&xx_mppId2ErrorId_xx&&", "0");
                    importRowCount++;
                }

                 //塞id值
                 Long id = mppMapper.selectSeq((String)tableInfos[0]+"_id_seq");
                 sqlInsertExec = sqlInsertExec.replace("&&xx_id_xx&&", String.valueOf(id));
                 tuples.append(sqlInsertExec);
            }

        }
        //数据入mpp库
        saveToGaussdb(tuples, (String) tableInfos[0]);
        return importRowCount;
    }

    private Set<Long> getFieldDestList(FileTemplateDto fileTemplateDto) {
        //通过模板，获取补全目标取值字段
        Set<Long>  fieldDestList = new HashSet<>();//目标字段集合
        FileFieldCompleteEntity fileFieldCompleteEntity4Sql = new FileFieldCompleteEntity();
        fileFieldCompleteEntity4Sql.setDestFileTemplateId(fileTemplateDto.getId());
        List<FileFieldCompleteEntity> fileFieldCompleteEntityList = fileFieldCompleteMapper.selectFileFieldCompleteList(fileFieldCompleteEntity4Sql);
        if(!CollectionUtils.isEmpty(fileFieldCompleteEntityList)){
            for(FileFieldCompleteEntity fileFieldCompleteEntityBean : fileFieldCompleteEntityList){
                fieldDestList.add(fileFieldCompleteEntityBean.getFieldDest());
            }
        }
        return fieldDestList;
    }

    private boolean checkFieldSize(Long userId, Long caeId, FileTemplateDto fileTemplateDto, File file, Object[] tableInfos, Long fileSeq, Long fileAttachmentId, List<Error> errorList, String fileType, Map<Long, Integer> feildRefIndexMap, List<CsvRow> rows) {
        int fieldSize = 0;
        for(Map.Entry<Long, Integer> entry : feildRefIndexMap.entrySet()){
            Integer csvIndex = entry.getValue();
            if(null != csvIndex){
                fieldSize ++;
            }
        }
        String formatError = "";
        for(int i = 1 ; i < rows.size() ; i++) {
            if (!CollectionUtils.isEmpty(rows.get(i).getFields()) && rows.get(i).getFields().size() < fieldSize){
                  formatError += i+"行格式不满足要求;\n";
            }
        }
        if(StringUtils.isNotEmpty(formatError)){
            saveFilefailedDetail(userId, caeId, fileTemplateDto, file, tableInfos, fileSeq, fileAttachmentId , fileType,formatError);
            errorList.add(new Error(ErrorCode_Enum.FILE_01_015.getCode(), ErrorCode_Enum.FILE_01_015.getMessage()));
            return true;
        }
        return false;
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
        try {

            //获取模板的目标补全字段
            Set<Long> fieldDestList = getFieldDestList(fileTemplateDto);

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
            if (file.getName().contains(".xlsx")) {
                wb = new XSSFWorkbook(file);
            } else if (file.getName().contains(".xls")) {
                FileInputStream fis = new FileInputStream(file);   //文件流对象
                wb = new HSSFWorkbook(fis);
            } else {
                errorList.add(new Error(ErrorCode_Enum.FILE_01_003.getCode(), ErrorCode_Enum.FILE_01_003.getMessage()));
                return;
            }
            //读取sheet0
            Sheet sheet = wb.getSheetAt(0);
            Row rowHead = sheet.getRow(0);
            List<String> headList = new ArrayList<>();

            //判断文件头是否有重复,或者字段头是空的，有重复不进行存库,并把错误信息存入文件信息表
            if (isHeadRepeat(userId, caeId, headList, file, fileSeq, fileAttachmentId, errorList)) {
                return;
            }

            //第一行是列名，所以不读
            int firstRowIndex = sheet.getFirstRowNum() + 1;
            int lastRowIndex = sheet.getLastRowNum() + 1;
            int firstCellIndex = rowHead.getFirstCellNum();
            int lastCellIndex = rowHead.getLastCellNum();
            rowCount = sheet.getLastRowNum();
            for (int i = firstCellIndex; i < lastCellIndex; i++) {
                Cell cell = rowHead.getCell(i);
                headList.add(cell.getStringCellValue().replaceAll("\\s*", ""));
            }
            boolean flag = getFeildRefIndex(fileTemplateDto.getFileTemplateDetailDtoList(), feildRefIndexMap, headList);

            //校验不通过列表,存入mysql
            List<FileParsingFailedEntity> fileParsingFailedEntityListSql = new ArrayList<>();

            //存入mpp
            //List<MppErrorInfoEntity> mppErrorInfoEntityList = new ArrayList<>();
            StringBuffer errorBuffer = new StringBuffer();

            //对excel行进行遍历,获取字节流
            StringBuffer tuples = new StringBuffer();
            for (int i = firstRowIndex; i < lastRowIndex; i++) {
                if (!CollectionUtils.isEmpty(feildRefIndexMap)) {
                    boolean mppId2ErrorId_flag = true;
                    String sqlInsertExec = insertSql;
                    for (Map.Entry<Long, Integer> entry : feildRefIndexMap.entrySet()) {
                        FileRinseDetailDto fileRinseDetailDto = regularMap.get(entry.getKey());
                        //任意一个满足
                        List<RegularDetailDto> regularDetailDtoListY = null;
                        //任意一个进行排除
                        List<RegularDetailDto> regularDetailDtoListN = null;
                        if (null != fileRinseDetailDto  && !fieldDestList.contains(entry.getKey())) { //如果是补全配置的 目标 字段，则不在这里进行正则校验

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
                        if (null != sheet && null != sheet.getRow(i) && null != entry.getValue() && null != sheet.getRow(i).getCell(entry.getValue())) {
                            sheet.getRow(i).getCell(entry.getValue()).setCellType(Cell.CELL_TYPE_STRING);
                        }
                        boolean checkRegularY = false;
                        if (!CollectionUtils.isEmpty(regularDetailDtoListY) && null != entry.getValue()) {
                            for (RegularDetailDto regularDetailDto : regularDetailDtoListY) {

                                if (sheet.getRow(i).getCell(entry.getValue()).getStringCellValue().replaceAll("\\s*", "").matches(regularDetailDto.getRegularValue().trim())) {
                                    checkRegularY = true;
                                    break;//只要匹配一个就跳出
                                }
                            }
                        } else {
                            checkRegularY = true;
                        }

                        boolean checkRegularN = true;
                        if (!CollectionUtils.isEmpty(regularDetailDtoListN) && null != entry.getValue()) {
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
                        sqlInsertExec = sqlInsertExec.replace("&&" + entry.getKey() + "&&", (null == entry.getValue() || null == sheet.getRow(i).getCell(entry.getValue())) ? "" : sheet.getRow(i).getCell(entry.getValue()).getStringCellValue().replaceAll("\\s*", ""));

                        //校验不通过
                        if (!checkRegular) {
                            //检查excel的行号是否已经有了，有了用之前的mppid2errorid
                            Long seq = null;
                            if (CollectionUtils.isEmpty(fileParsingFailedEntityListSql)) {
                                seq = mppErrorInfoMapper.selectErrorSeq();
                            } else {
                                for (FileParsingFailedEntity fileParsingFailedEntityBean : fileParsingFailedEntityListSql) {
                                    if (fileParsingFailedEntityBean.getRowNumber().equals(String.valueOf(i))) {
                                        seq = fileParsingFailedEntityBean.getMppId2ErrorId();
                                        break;
                                    }
                                }
                            }

                            if (null == seq) {
                                seq = mppErrorInfoMapper.selectErrorSeq();
                            }
                            FileParsingFailedEntity fileParsingFailedEntitySql = new FileParsingFailedEntity();
                            fileParsingFailedEntitySql.setRowNumber(String.valueOf(i));
                            fileParsingFailedEntitySql.setFileDetailId(fileSeq);
                            fileParsingFailedEntitySql.setFileTemplateId(fileTemplateDto.getId());
                            fileParsingFailedEntitySql.setFileTemplateDetailId(entry.getKey());
                            fileParsingFailedEntitySql.setContent(sheet.getRow(i).getCell(entry.getValue()).getStringCellValue());
                            fileParsingFailedEntitySql.setCaseId(caeId);
                            //mpp表名是唯一的
                            fileParsingFailedEntitySql.setTableName((String) tableInfos[0]);
                            fileParsingFailedEntitySql.setUserId(userId);
                            fileParsingFailedEntitySql.setMark(false);
                            fileParsingFailedEntitySql.setMppId2ErrorId(seq);
                            fileParsingFailedEntitySql.setFileAttachmentId(fileAttachmentId);
                            fileParsingFailedEntityListSql.add(fileParsingFailedEntitySql);
                            MppErrorInfoEntity mppErrorInfoEntity = new MppErrorInfoEntity();
                            mppErrorInfoEntity.setFileAttachmentId(fileAttachmentId);
                            mppErrorInfoEntity.setFileDetailId(fileSeq);
                            mppErrorInfoEntity.setFileRinseDetailId(fileRinseDetailDto.getId());
                            mppErrorInfoEntity.setMppid2errorid(seq);
                            mppErrorInfoEntity.setFileCaseId(caeId);
                            mppErrorInfoEntity.setMppTableName((String) tableInfos[0]);
                            //mppErrorInfoEntityList.add(mppErrorInfoEntity);
                            Long idSeq = mppMapper.selectSeq("error_info_id_seq");
                            mppErrorInfoEntity.setId(idSeq);
                            errorBuffer.append(mppErrorInfoEntity.toString());
                            sqlInsertExec = sqlInsertExec.replace("&&xx_mppId2ErrorId_xx&&", String.valueOf(seq));
                        }
                    }
                    sqlInsertExec = sqlInsertExec.replace("&&xx_file_detail_id_xx&&", String.valueOf(fileSeq));
                    //通过校验没有对应的错误信息 赋空值
                    if (mppId2ErrorId_flag) {
                        sqlInsertExec = sqlInsertExec.replace("&&xx_mppId2ErrorId_xx&&", "0");
                        importRowCount++;
                    }
                    //塞id值
                    Long id = mppMapper.selectSeq((String)tableInfos[0]+"_id_seq");
                    sqlInsertExec = sqlInsertExec.replace("&&xx_id_xx&&", String.valueOf(id));
                    tuples.append(sqlInsertExec);
                }
            }
            //正常数据入库
            saveToGaussdb(tuples, (String) tableInfos[0]);

            //没有不满足的行，则返回
            if (!CollectionUtils.isEmpty(fileParsingFailedEntityListSql)) {
                //处理校验不满足数据
                dealWithfailed(fileParsingFailedEntityListSql, errorBuffer);
            }
            //保存文件信息
            saveFileDetail(userId, caeId, fileTemplateDto, file, tableInfos, fileSeq, fileAttachmentId, hasImport, fileType, rowCount, importRowCount);
        }catch (Exception e){
            errorList.add(new Error(ErrorCode_Enum.FILE_01_016.getCode(), ErrorCode_Enum.FILE_01_016.getMessage(),e.toString()));
            e.printStackTrace();
        }
    }

    private Long saveToGaussdb(StringBuffer tuples, String tableName) throws SQLException, IOException {
        ByteArrayInputStream stream = new ByteArrayInputStream(tuples.toString().getBytes());
        Connection connection = new GaussConnection().getConnection();
        CopyManager cm = new CopyManager((BaseConnection) connection);
        String sql = "copy " + tableName + " from STDIN with (format 'CSV', delimiter '"+delimter+"', quote '"+quote+"')";
        long records  = cm.copyIn(sql,stream);
        connection.commit();
        connection.close();
        return records;

       /*
        Connection connection = new GaussConnection().getConnection();
        CopyManager cm = new CopyManager((BaseConnection) connection);
        String sql = "copy " + tableName + " from STDIN with (format 'CSV', delimiter '|', quote '$')";
        StringReader sr = new StringReader(tuples.toString());
        long records = cm.copyIn(sql, sr);
        connection.commit();
        connection.close();
        return records;
        */
    }


    @Override
    public void doRinse(FileTemplateDto fileTemplateDto, Object[] tableInfos , Long fileSeq , List<Error> errorList) throws IllegalAccessException {
        //通过模板id获取清洗信息
        List<RinseBusinessNullDto> rinseBusinessNullDtoList = rinseBusinessServiceImpl.getNullBussList(fileTemplateDto.getId(),errorList);
        //List<RinseBusinessRepeatDto> rinseBusinessRepeatDtoList = rinseBusinessServiceImpl.getRepetBussList(fileTemplateDto.getId(),errorList);
        List<RinseBusinessReplaceDto> rinseBusinessReplaceDtoList = rinseBusinessServiceImpl.getReplaceBussList(fileTemplateDto.getId(),errorList);
        List<RinseBusinessSuffixDto>  rinseBusinessSuffixDtoList = rinseBusinessServiceImpl.getSuffixBussList(fileTemplateDto.getId(),errorList);
        List<RinseBusinessPrefixDto>  rinseBusinessPrefixDtoList = rinseBusinessServiceImpl.getPrefixBussList(fileTemplateDto.getId(),errorList);

        //放入子线程中删除
        List<Long> mppid2erroridDeleteList = new ArrayList<>();

        /**
         * 进行null替换
         */
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
                                  fileTemplateDetailEntity.getFieldName()+") is null and  file_detail_id ="+fileSeq;
                  sql = sql.replace("_&condition&_",nullSql + " where  "+whereSql);
                  mppMapper.mppSqlExec(sql);
              }
        }

        //整表去重
        /*
        String repeatSql = "select c.* from (select a.*  from " +
                "(select row_number() OVER(PARTITION BY _&condition&_   order by mppid2errorid) AS rownum,b.* " +
                "  from "+tableInfos[0]+" b ) a ) c where rownum > 1";
        doRepeat(fileTemplateDto.getId(), tableInfos[0].toString(), rinseBusinessRepeatDtoList, mppid2erroridDeleteList, repeatSql);
        */
        /**
         * 字段关键字进行替换
         */
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

        /**
         * 去除后缀
         */
        String suffixSql = "update "+tableInfos[0] +" set _&condition&_";
        if(!CollectionUtils.isEmpty(rinseBusinessSuffixDtoList)){
            for(RinseBusinessSuffixDto rinseBusinessSuffixDto : rinseBusinessSuffixDtoList){
                FileTemplateDetailEntity fileTemplateDetailEntity = fileTemplateDetailMapper.selectFileTemplateDetailByPrimary(rinseBusinessSuffixDto.getFileTemplateDetailId());
                String condition = fileTemplateDetailEntity.getFieldName() + "=  reverse(substr(reverse("+fileTemplateDetailEntity.getFieldName()+"),position('"+rinseBusinessSuffixDto.getSuffix()+"' in reverse("+fileTemplateDetailEntity.getFieldName()+"))+length('"+rinseBusinessSuffixDto.getSuffix()+"')))"+
                                   " where "+fileTemplateDetailEntity.getFieldName()+" like '%" +rinseBusinessSuffixDto.getSuffix()+"%' and file_detail_id="+fileSeq;
                suffixSql = suffixSql.replace("_&condition&_",condition);
                mppMapper.mppSqlExec(suffixSql);
            }
        }

        /**
         * 去除前缀
         */
        String prefixSql = "update "+tableInfos[0] +" set _&condition&_";
        if(!CollectionUtils.isEmpty(rinseBusinessPrefixDtoList)){
            for(RinseBusinessPrefixDto rinseBusinessPrefixDto : rinseBusinessPrefixDtoList){
                FileTemplateDetailEntity fileTemplateDetailEntity = fileTemplateDetailMapper.selectFileTemplateDetailByPrimary(rinseBusinessPrefixDto.getFileTemplateDetailId());
                String condition = fileTemplateDetailEntity.getFieldName() + "=   substring("+fileTemplateDetailEntity.getFieldName()+",position('"+rinseBusinessPrefixDto.getPrefix() + "' in "+  fileTemplateDetailEntity.getFieldName()+")+length('"+rinseBusinessPrefixDto.getPrefix()+"'))"+
                        " where "+fileTemplateDetailEntity.getFieldName()+" like '%" +rinseBusinessPrefixDto.getPrefix()+"%' and file_detail_id="+fileSeq;
                prefixSql = prefixSql.replace("_&condition&_",condition);
                mppMapper.mppSqlExec(prefixSql);
            }
        }

    }

    public void doRepeat(String tableName, List<RinseBusinessRepeatDto> rinseBusinessRepeatDtoList, String repeatSql) throws InterruptedException, ExecutionException {
        String repeatCondition = "";
        if(!CollectionUtils.isEmpty(rinseBusinessRepeatDtoList)){
            //组装orderBy排序部分
            //进行去重
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
                    StringBuffer idsBuffer = new StringBuffer();
                    StringBuffer mppid2erroridsBuffer = new StringBuffer();
                    StringBuffer deleteSql = new StringBuffer("DELETE FROM  ").append(tableName).append("  WHERE id in  (");
                    StringBuffer deleteErrorSql =new StringBuffer("DELETE FROM  &&_tableNmae_&&").append("  WHERE mppid2errorid in  (");
                    List<Long> mppid2erroridList = new ArrayList<>();
                    for(Map map : mapList) {
                        idsBuffer.append(map.get("id")).append(",");
                        if(!map.get("mppid2errorid").toString().equals("0")) {
                            mppid2erroridsBuffer.append(map.get("mppid2errorid")).append(",");
                            mppid2erroridList.add((Long)map.get("mppid2errorid"));
                        }
                    }
                    boolean flag = mppid2erroridsBuffer.length() > 0;
                    //去除末尾的逗号
                    idsBuffer.deleteCharAt(idsBuffer.length()-1).append(")");
                    //去除末尾的逗号
                    if(flag) {
                        mppid2erroridsBuffer.deleteCharAt(mppid2erroridsBuffer.length() - 1).append(")");
                    }
                    deleteSql.append(idsBuffer);
                    //删除重复的数据
                    mppMapper.mppSqlExec(deleteSql.toString());

                    //把错误信息表的数据一并删除
                    if(flag) {
                        String[] errorTables = {"error_info", "file_parsing_failed"};
                        StringBuffer deleteErrorSqlBuffer = deleteErrorSql.append(mppid2erroridsBuffer);
                        ExecutorService executorServiceSqlDelete = Executors.newFixedThreadPool(2);

                        for (String str : errorTables) {
                            executorServiceSqlDelete.execute(new Runnable() {
                                @Override
                                public void run() {
                                    if ("error_info".equals(str)) {

                                        String deleteErrorSqlStr = deleteErrorSqlBuffer.toString().replace("&&_tableNmae_&&", str);

                                        try {
                                            mppMapper.mppSqlExec(deleteErrorSqlStr);
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }


                                    } else if ("file_parsing_failed".equals(str)) {
                                        try {

                                          fileParsingFailedMapper.deleteFileParsingFailedByMppid2erroridBatch(mppid2erroridList);

                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                    }
                                }
                            });
                        }
                    }

                }
            }
        }
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
        fileDetailEntityfSql.setOriginTableName((String) tableInfos[2]);
        //把信息存入文件信息表
        fileDetailMapper.fileDetailInsert(fileDetailEntityfSql);
    }

    private void saveFilefailedDetail(Long userId, Long caeId, FileTemplateDto fileTemplateDto, File file, Object[] tableInfos, Long fileSeq, Long fileAttachmentId, String fileType,String formatError) {
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
        fileDetailEntityfSql.setImportRowCount(0);
        fileDetailEntityfSql.setRowCount(0);
        fileDetailEntityfSql.setTableName((String) tableInfos[0]);
        fileDetailEntityfSql.setFileTableId((Long) tableInfos[1]);
        fileDetailEntityfSql.setOriginTableName((String) tableInfos[2]);
        fileDetailEntityfSql.setHasImport(false);
        fileDetailEntityfSql.setFailureMessage(formatError);
        //把信息存入文件信息表
        fileDetailMapper.fileDetailInsert(fileDetailEntityfSql);
    }

    public void dealWithfailed(List<FileParsingFailedEntity> fileParsingFailedEntityListSql, StringBuffer errorBuffer) throws IOException, SQLException {
        ExecutorService fileParsingFailedInsert = Executors.newFixedThreadPool(4);

        //子线程错误数据入mpp库
        fileParsingFailedInsert.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    saveToGaussdb(errorBuffer, "error_info");
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });

        int errorPackage = 0;
        //进行分包存库
        List<FileParsingFailedEntity> fileParsingFailedEntityPackage = new ArrayList<>();
        for(int i =0 ;i<fileParsingFailedEntityListSql.size();i++){
            fileParsingFailedEntityPackage.add(fileParsingFailedEntityListSql.get(i));
            if((++errorPackage == 500) || (i== fileParsingFailedEntityListSql.size()-1)){
                errorPackage = 0;
                //多线程避免脏读
                Vector<FileParsingFailedEntity> vector = new Vector<>();
                vector.addAll(fileParsingFailedEntityPackage);
                fileParsingFailedEntityPackage = new ArrayList<>();
                fileParsingFailedInsert.execute(new Runnable() {
                    @Override
                    public void run() {
                       fileParsingFailedMapper.fileParsingFailedInsert(vector);
                    }
                });
            }
        }


    }

    //获取csv、excel和排序字段的对应关系
    private boolean getFeildRefIndex(List<FileTemplateDetailDto> fileTemplateDetailDtoList, Map<Long, Integer> feildRefIndex, List<String> headList) {
        boolean result = false;
        if(!CollectionUtils.isEmpty(fileTemplateDetailDtoList) && !CollectionUtils.isEmpty(headList) ){
            for(int j =0;j< fileTemplateDetailDtoList.size();j++){
                feildRefIndex.put(fileTemplateDetailDtoList.get(j).getId(),null);//没有对应的表头也要保留到map中，为insertsql保留所有字段
                String[] excludeList = null;
                String[] keyList = null;
                if(StringUtils.isNotEmpty(fileTemplateDetailDtoList.get(j).getExclude())) {
                    excludeList = fileTemplateDetailDtoList.get(j).getExclude().split("&&");
                }
                if(StringUtils.isNotEmpty(fileTemplateDetailDtoList.get(j).getFieldKey())) {
                    keyList = fileTemplateDetailDtoList.get(j).getFieldKey().split("&&");
                }
                goto_for: for(int i=0;i<headList.size();i++){
                    //关键字排除
                    if(null != excludeList && excludeList.length>0 ){
                        for(String exclude:excludeList){
                            if(StringUtils.isNotEmpty(exclude) && headList.get(i).contains(exclude.trim())){
                                break  goto_for;
                            }
                        }
                    }
                    //关键字匹配
                    if(null != keyList && keyList.length>0 ){
                        for(String key:keyList){
                            if(StringUtils.isNotEmpty(key) && headList.get(i).contains(key.trim())){
                                feildRefIndex.put(fileTemplateDetailDtoList.get(j).getId(),i);
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

    private boolean isHeadRepeat(Long userId ,Long caeId,List<String> headList,File file,Long fileSeq,Long fileAttachmentId,List<Error> errorList){
        boolean flag = false;
        String message = "";
        for_1: for(String str1 : headList){
                    if(StringUtils.isEmpty(str1)){
                        flag = true;
                        message="表头不能为空";
                        break;
                    }
                    int c = 0;
                   for(String str2 : headList){
                      if(str1.equals(str2)){
                          c++;
                          if(c > 1) {
                              flag = true;
                              message = "表头不能重复【" + str1 + "】";
                              break for_1;
                          }
                      }
                   }
        }
        if(flag){
           //错误信息存库
            FileDetailEntity fileDetailEntityfSql = new FileDetailEntity();
            fileDetailEntityfSql.setId(fileSeq);
            fileDetailEntityfSql.setUserId(userId);
            fileDetailEntityfSql.setCaseId(caeId);
            if(file.getName().contains(".csv")) {
                fileDetailEntityfSql.setFileType("csv");
            } else if(file.getName().contains(".xls")){
                fileDetailEntityfSql.setFileType("xls");
            } else if(file.getName().contains(".xlsx")){
                fileDetailEntityfSql.setFileType("xlsx");
            } else{
                fileDetailEntityfSql.setFileType("");
            }
            fileDetailEntityfSql.setFileAttachmentId(fileAttachmentId);
            fileDetailEntityfSql.setFileName(file.getName());
            fileDetailEntityfSql.setFilePath(file.getAbsolutePath());
            fileDetailEntityfSql.setRowCount(0);
            fileDetailEntityfSql.setHasImport(false);
            fileDetailEntityfSql.setFailureMessage(message);
            //把信息存入文件信息表
            fileDetailMapper.fileDetailInsert(fileDetailEntityfSql);
            errorList.add(new Error(ErrorCode_Enum.FILE_01_002.getCode(),ErrorCode_Enum.FILE_01_002.getMessage()));
        }
        return flag;
    }
}
