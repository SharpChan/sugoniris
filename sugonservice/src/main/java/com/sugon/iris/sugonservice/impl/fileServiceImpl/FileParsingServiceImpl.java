package com.sugon.iris.sugonservice.impl.fileServiceImpl;

import com.alibaba.fastjson.JSONArray;
import com.google.gson.Gson;
import com.sugon.iris.sugoncommon.phoneNo.PhoneUtil;
import com.sugon.iris.sugoncommon.publicUtils.PublicUtils;
import com.sugon.iris.sugondata.mybaties.mapper.db2.*;
import com.sugon.iris.sugondata.mybaties.mapper.db4.MppMapper;
import com.sugon.iris.sugondomain.beans.baseBeans.Error;
import com.sugon.iris.sugondomain.beans.webSocket.WebSocketRequestDto;
import com.sugon.iris.sugondomain.dtos.fileDtos.FileRinseDetailDto;
import com.sugon.iris.sugondomain.dtos.fileDtos.FileRinseGroupDto;
import com.sugon.iris.sugondomain.dtos.fileDtos.FileTemplateDetailDto;
import com.sugon.iris.sugondomain.dtos.fileDtos.FileTemplateDto;
import com.sugon.iris.sugondomain.dtos.regularDtos.RegularDetailDto;
import com.sugon.iris.sugondomain.entities.mybatiesEntity.db2.*;
import com.sugon.iris.sugondomain.enums.ErrorCode_Enum;
import com.sugon.iris.sugondomain.enums.SZ_JZ_RinseType_Enum;
import com.sugon.iris.sugondomain.enums.WebSocketType_Enum;
import com.sugon.iris.sugonservice.impl.websocketServiceImpl.WebSocketClient;
import com.sugon.iris.sugonservice.impl.websocketServiceImpl.WebSocketServer;
import com.sugon.iris.sugonservice.service.fileService.FileDoParsingService;
import com.sugon.iris.sugonservice.service.fileService.FileParsingService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import javax.annotation.Resource;
import javax.websocket.ContainerProvider;
import javax.websocket.WebSocketContainer;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
@Service
public class FileParsingServiceImpl implements FileParsingService {

    public static final String quote = "^";
    public static final String delimter = "|";

    @Resource
    private FileAttachmentMapper fileAttachmentMapper;

    @Resource
    private FileTemplateGroupMapper fileTemplateGroupMapper;

    @Resource
    private FileTemplateDetailMapper fileTemplateDetailMapper;

    @Resource
    private FileTemplateMapper fileTemplateMapper;

    @Resource
    private FileDetailMapper fileDetailMapper;

    @Resource
    private MppMapper mppMapper;

    @Resource
    private SequenceMapper sequenceMapper;

    @Resource
    private FileTableMapper fileTableMapper;

    @Resource
    private FileRinseGroupMapper fileRinseGroupMapper;

    @Resource
    private FileRinseDetailMapper fileRinseDetailMapper;

    @Resource
    private RegularDetailMapper regularDetailMapper;

    @Resource
    private FileRinseRegularMapper fileRinseRegularMapper;

    @Resource
    private FileDoParsingService fileDoParsingServiceImpl;

    @Resource
    private FileCaseMapper fileCaseMapper;

    @Resource
    private FileOriginTableMapper fileOriginTableMapper;

    @Resource
    private RinseBusinessIpMapper rinseBusinessIpMapper;

    @Resource
    private RinseBusinessPhoneMapper rinseBusinessPhoneMapper;

    @Resource
    private RinseBusinessIdCardNoMapper rinseBusinessIdCardNoMapper;

    @Override
    public void test(Long userId) throws IOException {
        for(int i =0 ; i <100;i++) {
            Map<String,Map<String,String>> map1 = new HashMap<>();
            Map<String,String> map2 = new HashMap<>();
            map2.put("60",String.valueOf(80));
            map1.put(WebSocketType_Enum.WS_01.getCode(),map2);
            Gson gson = new Gson();
            String json = gson.toJson(map1);
            WebSocketServer.sendInfo(json,String.valueOf(userId));
        }
    }

    /**
     * ??????csv??????????????????mpp,???????????????????????????????????????
     * @param userId
     * @param fileAttachmentId
     * @param errorList
     */
    @Override
    public List<Long> fileParsing(Long userId, Long fileAttachmentId, List<Error> errorList) throws IOException, IllegalAccessException, InterruptedException, ExecutionException {

         List<Long> fileIdList = new ArrayList<>();
     try {

         //???????????????????????????
         AtomicInteger xlsCount = new AtomicInteger(0);
         //???????????????????????????
         AtomicInteger xlsxCount = new AtomicInteger(0);
         //???????????????????????????
         AtomicInteger csvCount = new AtomicInteger(0);

         FileAttachmentEntity fileAttachmentEntity = getFileAttachment(fileAttachmentId, errorList);
         String path = null;
         if (".csv".equals(fileAttachmentEntity.getFileType()) || ".xls".equals(fileAttachmentEntity.getFileType()) || ".xlsx".equals(fileAttachmentEntity.getFileType())) {
             path = fileAttachmentEntity.getAttachment().substring(0, fileAttachmentEntity.getAttachment().lastIndexOf("/"));
         } else {
             path = fileAttachmentEntity.getAttachment().substring(0, fileAttachmentEntity.getAttachment().lastIndexOf("."));
         }
         //???????????????????????????????????????
         List<File> fileList = new ArrayList<>();
         File baseFile = new File(path);
         PublicUtils.getAllFile(baseFile, fileList);

         if (CollectionUtils.isEmpty(fileList)) {
             errorList.add(new Error(ErrorCode_Enum.FILE_01_001.getCode(), ErrorCode_Enum.FILE_01_001.getMessage()));
             return fileIdList;
         }

         //??????fileTemplateGroupId???????????????
         List<FileTemplateGroupEntity> fileTemplateGroupEntityList = getFileTemplateGroupEntities(fileAttachmentEntity.getTemplateGroupId(), errorList);
         //????????????????????????????????????????????????????????????
         List<FileTemplateDto> fileTemplateDtoList = null;
         if (!CollectionUtils.isEmpty(fileTemplateGroupEntityList)) {
             fileTemplateDtoList = getFileTemplateDtoList(fileTemplateGroupEntityList, errorList);
         }

         if (CollectionUtils.isEmpty(fileTemplateDtoList)) {
             errorList.add(new Error(ErrorCode_Enum.FILE_01_002.getCode(), ErrorCode_Enum.FILE_01_002.getMessage()));
             return fileIdList;
         }

         //???????????????????????????????????????
         for (File file : fileList) {
             boolean flag = false;
             int c = 0;
             for (FileTemplateDto fileTemplateDto : fileTemplateDtoList) {
                 for (String key : fileTemplateDto.getTemplateKey().split("&&")) {
                     if (file.getName().contains(key)) {
                         c++;
                     }
                 }
                 for (String exclude : fileTemplateDto.getExclude().split("&&")) {
                     if (file.getName().contains(exclude)) {
                         flag = true;
                         break;
                     }
                 }
                 if (c > 1 && !flag) {
                     errorList.add(new Error(ErrorCode_Enum.FILE_01_012.getCode(), ErrorCode_Enum.FILE_01_012.getMessage()));
                     return fileIdList;
                 }
             }
         }
         //------------------------


         List<File> fileListNew = new ArrayList<>();
         fileListNew.addAll(fileList);
         /*
          *???????????????????????????map???key????????????value???????????????????????????
          */
         Map<FileTemplateDto, List<File>> mapTemplate2File = template2Files(fileListNew, fileTemplateDtoList);
         fileDetailFailedSave(userId, fileAttachmentEntity, fileListNew);

         if (CollectionUtils.isEmpty(mapTemplate2File)) {
             errorList.add(new Error(ErrorCode_Enum.FILE_01_003.getCode(), ErrorCode_Enum.FILE_01_003.getMessage()));
             return fileIdList;
         }

         //???????????????ip????????????
         List<RinseBusinessIpEntity>  allRinseBusinessIpEntityList = rinseBusinessIpMapper.getAllRinseBusinessIpList();
         Set<Long> ipSet = new HashSet<>();
         for(RinseBusinessIpEntity rinseBusinessIpEntity : allRinseBusinessIpEntityList){
             ipSet.add(rinseBusinessIpEntity.getFileTemplateDetailId());
         }
         //?????????????????????????????????
         List<RinseBusinessPhoneEntity> allRinseBusinessPhoneEntityList = rinseBusinessPhoneMapper.getAllRinseBusinessPhoneList();
         Set<Long> phoneSet = new HashSet<>();
         for(RinseBusinessPhoneEntity rinseBusinessPhoneEntity : allRinseBusinessPhoneEntityList){
             phoneSet.add(rinseBusinessPhoneEntity.getFileTemplateDetailId());
         }
         //????????????????????????????????????
         List<RinseBusinessIdCardNoEntity> allRinseBusinessIdCardNoEntityList = rinseBusinessIdCardNoMapper.getAllRinseBusinessIdCardNoList();
         Set<Long> idCardNoSet = new HashSet<>();
         for(RinseBusinessIdCardNoEntity rinseBusinessIdCardNoEntity : allRinseBusinessIdCardNoEntityList){
             idCardNoSet.add(rinseBusinessIdCardNoEntity.getFileTemplateDetailId());
         }

         //????????????????????????????????????????????????,???????????????????????????
         ExecutorService executorService = Executors.newFixedThreadPool(mapTemplate2File.size());

         List<Callable<String>> cList = new ArrayList<>();  //???????????????????????????

         Callable<String> task = null;  //??????????????????

         List<File> fileList4Process = new ArrayList<>();
         String json = getString(5, String.valueOf(fileAttachmentId));
         WebSocketServer.sendInfo(json, String.valueOf(userId));
         //??????????????????
         for (Map.Entry<FileTemplateDto, List<File>> entry : mapTemplate2File.entrySet()) {
             task = new Callable<String>() {
                 @Override
                 public String call() throws Exception {
                     FileTemplateDto fileTemplateDto = entry.getKey();
                     List<File> fileList4Template = entry.getValue();

                     //???????????????
                     PublicUtils.fileTemplateDetailDtoListSort(fileTemplateDto.getFileTemplateDetailDtoList());

                     //index[0] :tableName ;index[01: tableId
                     Object[] tableInfos = createMppTable(userId, fileAttachmentEntity, fileTemplateDto,allRinseBusinessIpEntityList,allRinseBusinessPhoneEntityList,allRinseBusinessIdCardNoEntityList);
                     String insertSql = getInsertSql(fileAttachmentEntity.getId(), fileAttachmentEntity.getCaseId(),
                                                     fileTemplateDto.getFileTemplateDetailDtoList(), (String) tableInfos[0],
                                                     fileTemplateDto.getId(),allRinseBusinessIpEntityList,allRinseBusinessPhoneEntityList,
                                                     allRinseBusinessIdCardNoEntityList);

                     //key:????????????id?????????value??????????????????????????????????????????????????????
                     Map<Long, FileRinseDetailDto> regularMap = new HashMap<>();
                     for (FileTemplateDetailDto fileTemplateDetailDtoBean : fileTemplateDto.getFileTemplateDetailDtoList()) {
                         regularMap.put(fileTemplateDetailDtoBean.getId(), fileTemplateDetailDtoBean.getFileRinseDetailDto());
                     }

                     //?????????????????????
                     for (File file : fileList4Template) {
                         fileList4Process.add(file);
                         int percent = fileList4Process.size() * 100 / fileList.size();
                         String json = getString(percent, String.valueOf(fileAttachmentId));
                         WebSocketServer.sendInfo(json, String.valueOf(userId));

                         Long fileSeq = sequenceMapper.getSeq("file_detail");
                         fileIdList.add(fileSeq);
                         if (file.getName().contains(".csv")) {
                             csvCount.incrementAndGet();
                             try {
                                 fileDoParsingServiceImpl.doParsingCsv(userId, fileAttachmentEntity.getCaseId(),
                                         fileTemplateDto, file, tableInfos, insertSql, regularMap, fileSeq, fileAttachmentId,ipSet,phoneSet,idCardNoSet, errorList);

                             } catch (IOException e) {
                                 e.printStackTrace();
                             }
                         }
                         if (file.getName().contains(".xls") || file.getName().contains(".xlsx")) {
                             xlsCount.incrementAndGet();
                             try {
                                 fileDoParsingServiceImpl.doParsingExcel(userId, fileAttachmentEntity.getCaseId(),
                                         fileTemplateDto, file, tableInfos, insertSql, regularMap, fileSeq, fileAttachmentId,ipSet,phoneSet, idCardNoSet,errorList);
                             } catch (Exception e) {
                                 e.printStackTrace();
                             }
                         }

                         //????????????????????????
                         saveOrigin(tableInfos,fileTemplateDto,fileSeq);

                         //??????????????????????????????
                         try {
                             fileDoParsingServiceImpl.doRinse(fileTemplateDto, tableInfos, fileSeq, errorList);
                         } catch (Exception e) {
                             e.printStackTrace();
                         }
                     }
                     return fileTemplateDto.getTemplateName();
                 }
             };
             cList.add(task);
         }

         List<Future<String>> results = executorService.invokeAll(cList, 30, TimeUnit.MINUTES); //?????????????????????????????????????????????????????????????????????????????????????????????

         for (Future<String> str : results) {  //???????????????
             //log.info(str.get());
         }
         executorService.shutdown();


         //????????????http/websocket ????????????
         //??????caseId??????????????????http/websocket??????
         //this.doUserDefinedRinse(fileAttachmentEntity.getCaseId(), userId, errorList);

         json = getString(100, String.valueOf(fileAttachmentId));
         WebSocketServer.sendInfo(json, String.valueOf(userId));
     }catch (Exception e){
         e.printStackTrace();
     }
        return fileIdList;
    }

    void saveOrigin(Object[] tableInfos,FileTemplateDto fileTemplateDto,Long fileSeq){

        if(CollectionUtils.isEmpty(fileTemplateDto.getFileTemplateDetailDtoList())){
            return;
        }
        String columnNames = "";
        for(FileTemplateDetailDto fileTemplateDetailDto : fileTemplateDto.getFileTemplateDetailDtoList()){
            columnNames += fileTemplateDetailDto.getFieldName()+",";
        }
        String sql = "insert into "+tableInfos[2]+"(id,file_template_id,"+columnNames+"file_detail_id ,file_attachment_id,case_id) select id,file_template_id,"+columnNames+"file_detail_id,file_attachment_id,case_id from "+tableInfos[0]+" where file_detail_id ="+fileSeq;
        mppMapper.mppSqlExec(sql);
    }

    private String getString(int percent,String fileAttachmentId) {
        //key:??????id???value??????
        Map<String,Map<String,String>> map1 = new HashMap<>();
        Map<String,String> map2 = new HashMap<>();
        map2.put(fileAttachmentId,String.valueOf(percent));
        map1.put(WebSocketType_Enum.WS_01.getCode(),map2);
        Gson gson = new Gson();
        return gson.toJson(map1);
    }

    //???????????????????????????
    public void doUserDefinedRinse(Long caseId, Long userId, List<Error> errorList) {
        FileCaseEntity fileCaseEntity =  fileCaseMapper.selectFileCaseByPrimaryKey(caseId);
        if(null == fileCaseEntity){
           return;
        }
        String rinseUrl = fileCaseEntity.getRinseUrl();
        if(StringUtils.isEmpty(rinseUrl)){
            return;
        }
        if(rinseUrl.contains("http")){



        }else if(rinseUrl.contains("ws:")){
            WebSocketContainer container = ContainerProvider.getWebSocketContainer();
            WebSocketClient client = new WebSocketClient();
            String URI = rinseUrl+"/"+userId;
            try {
                container.connectToServer(client, new URI(URI));
            }catch (Exception e){
                e.printStackTrace();
                errorList.add(new Error(ErrorCode_Enum.SUGON_02_009.getCode(),ErrorCode_Enum.SUGON_02_009.getMessage(),e.toString()));
            }
            WebSocketRequestDto<Long> webSocketRequestDto = new WebSocketRequestDto<>();
            webSocketRequestDto.setParam(caseId);
            webSocketRequestDto.setUserId(userId);
            webSocketRequestDto.setBusinessId(SZ_JZ_RinseType_Enum.RINSE_01.getCode());
            Object obj = JSONArray.toJSON(webSocketRequestDto);
            String json = obj.toString();
            client.send(json);
        }
    }

    private void fileDetailFailedSave(Long userId, FileAttachmentEntity fileAttachmentEntity, List<File> fileListNew) {
        //?????????????????????????????????
        for(File fileFailed : fileListNew){
            Long fileSeq = sequenceMapper.getSeq("file_detail");
            FileDetailEntity fileDetailEntityfSql = new FileDetailEntity();
            fileDetailEntityfSql.setId(fileSeq);
            fileDetailEntityfSql.setUserId(userId);
            fileDetailEntityfSql.setCaseId(fileAttachmentEntity.getCaseId());
            if(fileFailed.getName().contains(".csv")) {
                fileDetailEntityfSql.setFileType("csv");
            } else if(fileFailed.getName().contains(".xls")){
                fileDetailEntityfSql.setFileType("xls");
            } else if(fileFailed.getName().contains(".xlsx")){
                fileDetailEntityfSql.setFileType("xlsx");
            } else{
                fileDetailEntityfSql.setFileType("");
            }
            fileDetailEntityfSql.setFileAttachmentId(fileAttachmentEntity.getId());
            fileDetailEntityfSql.setFileName(fileFailed.getName());
            fileDetailEntityfSql.setFilePath(fileFailed.getAbsolutePath());
            fileDetailEntityfSql.setRowCount(0);
            fileDetailEntityfSql.setImportRowCount(0);
            fileDetailEntityfSql.setHasImport(false);
            fileDetailEntityfSql.setFailureMessage("?????????????????????");
            //??????????????????????????????
            fileDetailMapper.fileDetailInsert(fileDetailEntityfSql);
        }
    }

    //????????????????????????
    private String getInsertSql(Long fileAttachmentId ,Long caseId,List<FileTemplateDetailDto> fileTemplateDetailDtoList, String tableName,
                                Long fileTemplateId,List<RinseBusinessIpEntity>  allRinseBusinessIpEntityList,List<RinseBusinessPhoneEntity> allRinseBusinessPhoneEntityList,
                                List<RinseBusinessIdCardNoEntity> allRinseBusinessIdCardNoEntityList) {
        //??????insertSql??????
   /*
        String sqlInsert = "insert into "+tableName+"(";
        String  sqlValues = "";
        for(FileTemplateDetailDto fileTemplateDetailDto : fileTemplateDetailDtoList){
            sqlInsert += fileTemplateDetailDto.getFieldName()+",";
            sqlValues += "'&&"+fileTemplateDetailDto.getId()+"&&',";
        }
        sqlInsert += "file_template_id,case_id,file_attachment_id,file_detail_id,mppId2ErrorId";
        sqlValues += "'"+fileTemplateId+"','"+caseId+"','"+fileAttachmentId+"','&&xx_file_detail_id_xx&&',"+"'&&xx_mppId2ErrorId_xx&&'";
        sqlInsert +=") values(" +sqlValues+");";
        return sqlInsert;
   */


        StringBuilder sqlInsertBuilder = new StringBuilder();
        sqlInsertBuilder.append(quote).append("&&xx_").append("id").append("_xx&&").append(quote).append(delimter);
        for(FileTemplateDetailDto fileTemplateDetailDto :  fileTemplateDetailDtoList){
            sqlInsertBuilder.append(quote).append("&&").append(fileTemplateDetailDto.getId()).append("&&").append(quote).append(delimter);
        }

        //???????????????????????????ip??????????????????
        List<RinseBusinessIpEntity> rinseBusinessIpEntityList =  new ArrayList<>();
        if(!CollectionUtils.isEmpty(allRinseBusinessIpEntityList)) {
            for (RinseBusinessIpEntity rinseBusinessIpEntity : allRinseBusinessIpEntityList) {
                if (fileTemplateId.equals(rinseBusinessIpEntity.getFileTemplateId())) {
                    rinseBusinessIpEntityList.add(rinseBusinessIpEntity);
                }
            }
        }
        //?????????????????????????????????????????????
        List<RinseBusinessPhoneEntity> rinseBusinessPhoneEntityList = new ArrayList<>();
        if(!CollectionUtils.isEmpty(allRinseBusinessPhoneEntityList)) {
            for (RinseBusinessPhoneEntity rinseBusinessPhoneEntity : allRinseBusinessPhoneEntityList) {
                if (fileTemplateId.equals(rinseBusinessPhoneEntity.getFileTemplateId())) {
                    rinseBusinessPhoneEntityList.add(rinseBusinessPhoneEntity);
                }
            }
        }

        //????????????????????????????????????????????????
        List<RinseBusinessIdCardNoEntity> rinseBusinessIdCardNoEntityList = new ArrayList<>();
        if(!CollectionUtils.isEmpty(allRinseBusinessIdCardNoEntityList)) {
            for (RinseBusinessIdCardNoEntity rinseBusinessIdCardNoEntity : allRinseBusinessIdCardNoEntityList) {
                if (fileTemplateId.equals(rinseBusinessIdCardNoEntity.getFileTemplateId())) {
                    rinseBusinessIdCardNoEntityList.add(rinseBusinessIdCardNoEntity);
                }
            }
        }


        if(!CollectionUtils.isEmpty(rinseBusinessIpEntityList)){
              for(RinseBusinessIpEntity rinseBusinessIpEntity : rinseBusinessIpEntityList){
                  sqlInsertBuilder.append(quote).append("&&xx_").append(rinseBusinessIpEntity.getFileTemplateDetailId()).append("_ipCountry").append("_xx&&").append(quote).append(delimter);
                  sqlInsertBuilder.append(quote).append("&&xx_").append(rinseBusinessIpEntity.getFileTemplateDetailId()).append("_ipArea").append("_xx&&").append(quote).append(delimter);
              }
        }

        if(!CollectionUtils.isEmpty(rinseBusinessPhoneEntityList)){
              for(RinseBusinessPhoneEntity rinseBusinessPhoneEntity : rinseBusinessPhoneEntityList){
                  sqlInsertBuilder.append(quote).append("&&xx_").append(rinseBusinessPhoneEntity.getFileTemplateDetailId()).append("_phoneInfo").append("_xx&&").append(quote).append(delimter);
              }
        }

        if(!CollectionUtils.isEmpty(rinseBusinessIdCardNoEntityList)){
            for(RinseBusinessIdCardNoEntity rinseBusinessIdCardNoEntity : rinseBusinessIdCardNoEntityList){
                sqlInsertBuilder.append(quote).append("&&xx_").append(rinseBusinessIdCardNoEntity.getFileTemplateDetailId()).append("_IdCardNo_Province").append("_xx&&").append(quote).append(delimter);
                sqlInsertBuilder.append(quote).append("&&xx_").append(rinseBusinessIdCardNoEntity.getFileTemplateDetailId()).append("_IdCardNo_City").append("_xx&&").append(quote).append(delimter);
                sqlInsertBuilder.append(quote).append("&&xx_").append(rinseBusinessIdCardNoEntity.getFileTemplateDetailId()).append("_IdCardNo_Region").append("_xx&&").append(quote).append(delimter);
                sqlInsertBuilder.append(quote).append("&&xx_").append(rinseBusinessIdCardNoEntity.getFileTemplateDetailId()).append("_IdCardNo_Birthday").append("_xx&&").append(quote).append(delimter);
                sqlInsertBuilder.append(quote).append("&&xx_").append(rinseBusinessIdCardNoEntity.getFileTemplateDetailId()).append("_IdCardNo_Gender").append("_xx&&").append(quote).append(delimter);
            }
        }

        sqlInsertBuilder.append(quote).append("&&xx_").append("mppId2ErrorId").append("_xx&&").append(quote).append(delimter);
        sqlInsertBuilder.append(quote).append("&&xx_").append("file_detail_id").append("_xx&&").append(quote).append(delimter);
        sqlInsertBuilder.append(quote).append(fileTemplateId).append(quote).append(delimter);
        sqlInsertBuilder.append(quote).append(fileAttachmentId).append(quote).append(delimter);
        sqlInsertBuilder.append(quote).append(caseId).append(quote).append("\n");
        return sqlInsertBuilder.toString();
    }

    /**
     * ??????mpp?????????,??????????????????id
     * @param userId
     * @param fileAttachmentEntity
     * @param fileTemplateDto
     * @return   index[0] :tableName ;index[01: tableId
     */
    private Object[] createMppTable(Long userId,FileAttachmentEntity fileAttachmentEntity, FileTemplateDto fileTemplateDto,
                                    List<RinseBusinessIpEntity>  allRinseBusinessIpEntityList,
                                    List<RinseBusinessPhoneEntity> allRinseBusinessPhoneEntityList,
                                    List<RinseBusinessIdCardNoEntity> allRinseBusinessIdCardNoEntityList) {
        Object[] tableInfos = new Object[3];
        //???????????????????????????????????????
        FileTableEntity fileTableEntity = getMppTableName(fileAttachmentEntity.getCaseId(), fileTemplateDto.getId());
        //??????????????????????????????
        if(null == fileTableEntity) {
            //???????????????????????????
            //??????="base_"+??????????????????+"_"+????????????
            String tableName = "base_" + fileTemplateDto.getTablePrefix() + "_" + fileAttachmentEntity.getCaseId()+"_"+userId+"_localfile";
            String origin_tableName = "origin_" + fileTemplateDto.getTablePrefix() + "_" + fileAttachmentEntity.getCaseId()+"_"+userId;
            tableInfos[0] = tableName;
            tableInfos[2] = origin_tableName;
            String sqlCreate =  "CREATE TABLE "+tableName+" ( id serial not null,";
            String origin_sqlCreate =  "CREATE TABLE "+origin_tableName+" ( id int4 not null,";
            for(FileTemplateDetailDto fileTemplateDetailDto : fileTemplateDto.getFileTemplateDetailDtoList()){
                sqlCreate += fileTemplateDetailDto.getFieldName() +" varchar NULL,";
                origin_sqlCreate += fileTemplateDetailDto.getFieldName() +" varchar NULL,";
            }

            //ip?????????????????????
            if(!CollectionUtils.isEmpty(allRinseBusinessIpEntityList)) {
                for (RinseBusinessIpEntity rinseBusinessIpEntity : allRinseBusinessIpEntityList) {
                    if(rinseBusinessIpEntity.getFileTemplateId().equals(fileTemplateDto.getId())){
                        sqlCreate += rinseBusinessIpEntity.getFieldName()+"_country  varchar NULL,";
                        sqlCreate += rinseBusinessIpEntity.getFieldName()+"_area  varchar NULL,";
                    }
                }
            }

            //?????????????????????????????????
            if(!CollectionUtils.isEmpty(allRinseBusinessPhoneEntityList)) {
                for (RinseBusinessPhoneEntity rinseBusinessPhoneEntity : allRinseBusinessPhoneEntityList) {
                    if(rinseBusinessPhoneEntity.getFileTemplateId().equals(fileTemplateDto.getId())){
                        sqlCreate += rinseBusinessPhoneEntity.getFieldName()+"_description  varchar NULL,";
                    }
                }
            }

            //?????????????????????????????????
            if(!CollectionUtils.isEmpty(allRinseBusinessIdCardNoEntityList)){
                for(RinseBusinessIdCardNoEntity rinseBusinessIdCardNoEntity : allRinseBusinessIdCardNoEntityList){
                   if(rinseBusinessIdCardNoEntity.getFileTemplateId().equals(fileTemplateDto.getId())){
                       sqlCreate += rinseBusinessIdCardNoEntity.getFieldName()+"_province  varchar NULL,";
                       sqlCreate += rinseBusinessIdCardNoEntity.getFieldName()+"_city  varchar NULL,";
                       sqlCreate += rinseBusinessIdCardNoEntity.getFieldName()+"_region  varchar NULL,";
                       sqlCreate += rinseBusinessIdCardNoEntity.getFieldName()+"_birthday  varchar NULL,";
                       sqlCreate += rinseBusinessIdCardNoEntity.getFieldName()+"_gender  varchar NULL,";
                   }
                }
            }

            sqlCreate += " mppId2ErrorId int8 NULL, file_detail_id int4 NULL, file_template_id int4 NULL,file_attachment_id  varchar NULL,case_id varchar NULL);";
            origin_sqlCreate += " file_detail_id int4 NULL, file_template_id int4 NULL ,file_attachment_id  varchar NULL,case_id varchar NULL);";

            String index_file_template_id = "CREATE INDEX "+tableName+"_mppid2errorid_idx ON "+tableName+" USING btree (file_template_id);";
            String index_case_id = "CREATE INDEX "+tableName+"_caseId_idx ON "+tableName+" USING btree (case_id);";
            String index_file_detail_id = "CREATE INDEX "+tableName+"_file_detail_id ON "+tableName+" USING btree (file_detail_id);";
            mppMapper.mppSqlExec(sqlCreate);
            mppMapper.mppSqlExec(index_file_template_id);
            mppMapper.mppSqlExec(index_case_id);
            mppMapper.mppSqlExec(index_file_detail_id);

            FileTableEntity fileTableEntity4Sql = new FileTableEntity();
            fileTableEntity4Sql.setCaseId(fileAttachmentEntity.getCaseId());
            fileTableEntity4Sql.setFileTemplateId(fileTemplateDto.getId());
            fileTableEntity4Sql.setTableName(tableName);
            fileTableEntity4Sql.setTitle(fileAttachmentEntity.getCaseId()+"_"+fileTemplateDto.getTemplateName());
            fileTableEntity4Sql.setUserId(userId);
            fileTableMapper.saveFileTable(fileTableEntity4Sql);
            tableInfos[1] =  fileTableEntity4Sql.getId();

            //?????????????????????
            mppMapper.mppSqlExec(origin_sqlCreate);
            FileOriginTableEntity fileOriginTableEntity4Sql = new FileOriginTableEntity();
            fileOriginTableEntity4Sql.setCaseId(fileAttachmentEntity.getCaseId());
            fileOriginTableEntity4Sql.setFileTableId(fileTableEntity4Sql.getId());
            fileOriginTableEntity4Sql.setFileTemplateId(fileTemplateDto.getId());
            fileOriginTableEntity4Sql.setTableName(origin_tableName);
            fileOriginTableEntity4Sql.setUserId(userId);
            fileOriginTableMapper.saveFileOriginTable(fileOriginTableEntity4Sql);
        }else{
            String tableName = fileTableEntity.getTableName();
            tableInfos[0] = tableName;
            tableInfos[1] = fileTableEntity.getId();
            String origin_tableName = getMppOriginTableName(fileAttachmentEntity.getCaseId(), fileTemplateDto.getId()).getTableName();
            tableInfos[2] = origin_tableName;
        }
        return tableInfos;
    }

    //???????????????????????????map???key????????????value???????????????????????????
    //???????????????????????????????????????
    private Map<FileTemplateDto, List<File>> template2Files(List<File> fileList, List<FileTemplateDto> fileTemplateDtoList) {
        Map<FileTemplateDto, List<File>> mapTemplate2File = new HashMap<>();
        for(FileTemplateDto fileTemplateDto : fileTemplateDtoList){
            //???????????????
            String templateKeys = fileTemplateDto.getTemplateKey();
            if(StringUtils.isEmpty(templateKeys)){
                  continue;
            }
            List<String>  templateKeyList = Arrays.asList(templateKeys.split("&&"));
            //?????????????????????
            String excludes = fileTemplateDto.getExclude();
            List<String> excludeList = new ArrayList<>();
            if(!StringUtils.isEmpty(excludes)){
                excludeList = Arrays.asList(excludes.split("&&"));
            }
            List<File>  fileList4Template = new ArrayList<>();
            for(Iterator<File> it = fileList.iterator(); it.hasNext();){
                File file = it.next();
                boolean flag1 = false;
                boolean flag2 = true;
                //???????????????
                for(String templateKey : templateKeyList){
                    if(file.getName().contains(templateKey.trim())){
                        flag1 = true;
                        break;
                    }
                }
                //??????????????????????????????
                for(String exclude : excludeList){
                   if(file.getName().contains(exclude.trim())){
                       flag2 = false;
                       break;
                   }
                }
                if(flag1 && flag2){
                    fileList4Template.add(file);
                    //?????????????????????????????????????????????????????????????????????????????????
                    it.remove();
                }
            }
            if(!CollectionUtils.isEmpty(fileList4Template)){
                mapTemplate2File.put(fileTemplateDto,fileList4Template);
            }
        }
        return mapTemplate2File;
    }

    /**
     * ???????????????????????????????????????
     * @param caseId:????????????
     * @param templateId???????????????
     * @return
     */
    private FileTableEntity getMppTableName( Long caseId, Long templateId) {
        FileTableEntity fileTableEntity = new FileTableEntity();
        fileTableEntity.setCaseId(caseId);
        fileTableEntity.setFileTemplateId(templateId);
        List<FileTableEntity> fileTableEntityList = fileTableMapper.findFileTableList(fileTableEntity);
        if(CollectionUtils.isEmpty(fileTableEntityList)){
            return null;
        }
        return fileTableEntityList.get(0);
    }

    /**
     * ???????????????????????????????????????
     * @param caseId:????????????
     * @param templateId???????????????
     * @return
     */
    private FileOriginTableEntity getMppOriginTableName( Long caseId, Long templateId) {
        FileOriginTableEntity fileOriginTableEntity4Sql = new FileOriginTableEntity();
        fileOriginTableEntity4Sql.setCaseId(caseId);
        fileOriginTableEntity4Sql.setFileTemplateId(templateId);
        List<FileOriginTableEntity> fileOriginTableEntityList = fileOriginTableMapper.findFileOriginTableList(fileOriginTableEntity4Sql);
        if(CollectionUtils.isEmpty(fileOriginTableEntityList)){
            return null;
        }
        return fileOriginTableEntityList.get(0);
    }


    //????????????????????????
    private FileAttachmentEntity getFileAttachment(Long fileAttachmentId, List<Error> errorList){
        //??????????????????????????????
        FileAttachmentEntity fileAttachmentEntity = null;
        try {
            fileAttachmentEntity = fileAttachmentMapper.selectFileAttachmentByPrimaryKey(fileAttachmentId);
        }catch (Exception e){
            e.printStackTrace();
            errorList.add(new Error(ErrorCode_Enum.SYS_DB_001.getCode(),"?????????file_attachment??????",e.toString()));
        }
        return fileAttachmentEntity;
    }

    //??????fileTemplateGroupId???????????????
    private List<FileTemplateGroupEntity> getFileTemplateGroupEntities( Long fileTemplateGroupId, List<Error> errorList) {
        FileTemplateGroupEntity fileTemplateGroupEntitySql = new FileTemplateGroupEntity();
        fileTemplateGroupEntitySql.setGroupId(fileTemplateGroupId);
        List<FileTemplateGroupEntity> fileTemplateGroupEntityList = new ArrayList<>();
        try {
            fileTemplateGroupEntityList = fileTemplateGroupMapper.selectFileTemplateGroupList(fileTemplateGroupEntitySql);
        }catch (Exception e){
            e.printStackTrace();
            errorList.add(new Error(ErrorCode_Enum.SYS_DB_001.getCode(),"?????????file_template_group??????",e.toString()));
        }
        return fileTemplateGroupEntityList;
    }

    //????????????????????????????????????????????????????????????
   private List<FileTemplateDto>  getFileTemplateDtoList(List<FileTemplateGroupEntity> fileTemplateGroupEntityList ,List<Error> errorList) throws IllegalAccessException {
       List<FileTemplateDto> fileTemplateDtoList = new ArrayList<>();
       for (FileTemplateGroupEntity fileTemplateGroupEntity : fileTemplateGroupEntityList){
           //????????????????????????
           FileTemplateEntity fileTemplateEntity = getFileTemplateEntity( fileTemplateGroupEntity,errorList);
           FileTemplateDto fileTemplateDto = new FileTemplateDto();
           PublicUtils.trans(fileTemplateEntity,fileTemplateDto);

           //?????????????????????
           FileRinseGroupEntity fileRinseGroupEntity = fileRinseGroupMapper.selectByPrimaryKey(fileTemplateDto.getFileRinseGroupId());
           FileRinseGroupDto fileRinseGroupDto = new FileRinseGroupDto();
           PublicUtils.trans(fileRinseGroupEntity,fileRinseGroupDto);
           fileTemplateDto.setFileRinseGroupDto(fileRinseGroupDto);

           //???????????????????????????
           List<FileTemplateDetailDto> fileTemplateDetailDtoList = new ArrayList<>();
           fileTemplateDto.setFileTemplateDetailDtoList(fileTemplateDetailDtoList);
           FileTemplateDetailEntity fileTemplateDetailEntity4Sql = new FileTemplateDetailEntity();
           fileTemplateDetailEntity4Sql.setTemplateId(fileTemplateGroupEntity.getTemplateId());
           List<FileTemplateDetailEntity> fileTemplateDetailEntityList =  fileTemplateDetailMapper.selectFileTemplateDetailList(fileTemplateDetailEntity4Sql);
           if(CollectionUtils.isEmpty(fileTemplateDetailEntityList)){
               continue;
           }
           //?????????????????????????????????????????????
           fileTemplateDtoList.add(fileTemplateDto);

           for(FileTemplateDetailEntity fileTemplateDetailEntity : fileTemplateDetailEntityList){
               FileTemplateDetailDto fileTemplateDetailDto = new FileTemplateDetailDto();
               PublicUtils.trans(fileTemplateDetailEntity,fileTemplateDetailDto);
               fileTemplateDetailDtoList.add(fileTemplateDetailDto);

               //???????????????????????????????????????
               if(null == fileTemplateDetailDto.getFileRinseDetailId()){
                     continue;
               }

               //??????????????????
               FileRinseDetailEntity fileRinseDetailEntity = fileRinseDetailMapper.selectByPrimaryKey(fileTemplateDetailDto.getFileRinseDetailId());
               FileRinseDetailDto fileRinseDetailDto = new FileRinseDetailDto();
               PublicUtils.trans(fileRinseDetailEntity,fileRinseDetailDto);
               fileTemplateDetailDto.setFileRinseDetailDto(fileRinseDetailDto);
               //?????????????????????
               List<RegularDetailDto>  regularDetailDtoListY = new ArrayList<>();
               List<RegularDetailDto>  regularDetailDtoListN = new ArrayList<>();
               //?????????????????????
               //???????????????????????????????????????????????????
               List<FileRinseRegularEntity> fileRinseRegularEntityList = fileRinseRegularMapper.selectByFileRinseDetailId(fileRinseDetailEntity.getId());

               if(!CollectionUtils.isEmpty(fileRinseRegularEntityList)) {
                   for (FileRinseRegularEntity fileRinseRegularEntity : fileRinseRegularEntityList) {
                       if ("1".equals(fileRinseRegularEntity.getType())) {
                           RegularDetailEntity regularDetailEntityY = regularDetailMapper.selectByPrimaryKey(fileRinseRegularEntity.getRegularDetailId());
                           RegularDetailDto regularDetailDtoY = new RegularDetailDto();
                           PublicUtils.trans(regularDetailEntityY, regularDetailDtoY);
                           regularDetailDtoListY.add(regularDetailDtoY);
                       }
                       if ("2".equals(fileRinseRegularEntity.getType())) {
                           RegularDetailEntity regularDetailEntityN = regularDetailMapper.selectByPrimaryKey(fileRinseRegularEntity.getRegularDetailId());
                           RegularDetailDto regularDetailDtoN = new RegularDetailDto();
                           PublicUtils.trans(regularDetailEntityN, regularDetailDtoN);
                           regularDetailDtoListY.add(regularDetailDtoN);
                       }
                   }
                   fileRinseDetailDto.setRegularDetailDtoListY(regularDetailDtoListY);
                   fileRinseDetailDto.setRegularDetailDtoListN(regularDetailDtoListN);
               }
           }
       }
       return fileTemplateDtoList;
   }

    //??????????????????
    private FileTemplateEntity getFileTemplateEntity( FileTemplateGroupEntity fileTemplateGroupEntityBean,List<Error> errorList) {
        FileTemplateEntity fileTemplateEntity = null;
        try {
            fileTemplateEntity = fileTemplateMapper.selectFileTemplateByPrimaryKey(fileTemplateGroupEntityBean.getTemplateId());
        }catch (Exception e){
          e.printStackTrace();
            errorList.add(new Error(ErrorCode_Enum.SYS_DB_001.getCode(),"?????????file_template??????",e.toString()));
        }
        return fileTemplateEntity;
    }
}
