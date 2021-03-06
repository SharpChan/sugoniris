package com.sugon.iris.sugonservice.impl.rocketMqServiceImpl;

import com.google.gson.Gson;
import com.sugon.iris.sugoncommon.publicUtils.PublicUtils;
import com.sugon.iris.sugondata.jdbcTemplate.intf.system.AccountServiceDao;
import com.sugon.iris.sugondata.mybaties.mapper.db2.*;
import com.sugon.iris.sugondomain.beans.baseBeans.Error;
import com.sugon.iris.sugondomain.beans.system.User;
import com.sugon.iris.sugondomain.entities.jdbcTemplateEntity.systemEntities.UserEntity;
import com.sugon.iris.sugondomain.entities.mybatiesEntity.db2.*;
import com.sugon.iris.sugonservice.service.fileService.FileCaseService;
import com.sugon.iris.sugonservice.service.fileService.FileParsingService;
import com.sugon.iris.sugonservice.service.fileService.FolderService;
import com.sugon.iris.sugonservice.service.rocketMqService.DealWithCrawlerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import javax.annotation.Resource;
import java.io.*;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ExecutionException;

@Slf4j
@Service
public class DealWithCrawlerServiceImpl implements DealWithCrawlerService {

    @Resource
    private RobotTokenMapper robotTokenMapper;

    @Resource
    private FileCaseMapper fileCaseMapper;

    @Resource
    private AccountServiceDao accountServiceDao;

    @Resource
    private FileTemplateMapper fileTemplateMapper;

    @Resource
    private FileTemplateDetailMapper fileTemplateDetailMapper;

    @Resource
    private FileAttachmentMapper fileAttachmentMapper;

    @Resource
    private FileParsingService fileParsingServiceImpl;

    @Resource
    private FileCaseService fileCaseServiceImpl;

    @Resource
    private FolderService folderServiceImpl;

    @Resource
    private FileDetailMapper fileDetailMapper;

    @Override
    public void dealWithBankData(String message){
        try {
            Gson gson = new Gson();
            Map<String, Object> map = gson.fromJson(message, Map.class);
            List<Error> errorList = new ArrayList<>();
            String serialNumber = map.get("token").toString();
            String token = serialNumber.substring(0, 22);
            String date = serialNumber.substring(22);
            String policeno = map.get("policeno").toString();
            Map<String, List<Map<String, String>>> template2DatasMap = (Map<String, List<Map<String, String>>>) map.get(token);
            if (CollectionUtils.isEmpty(template2DatasMap)) {
                return;
            }
            //????????????
            Long userId = this.createUser(policeno, errorList);
            //????????????
            Long caseId = createCase(userId, token, errorList);
            //?????????token ??????,???????????????token ???????????????????????????????????????????????????????????????
            if (!initTokenInfo(template2DatasMap, userId, token, caseId, date, policeno, errorList)) {
                return;
            }
            //???????????????
            List<FileAttachmentEntity> attAchmentList = createFolder(template2DatasMap, userId, caseId);
            //??????????????????
            fileParsingServiceImpl.fileParsing(userId, attAchmentList.get(0).getId(), errorList);

            //????????????
            doRinse(errorList, userId, attAchmentList);

            //??????file_attachment?????????????????????????????????
            importStatus(token,userId);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void doRinse(List<Error> errorList, Long userId, List<FileAttachmentEntity> attAchmentList) throws InterruptedException, IllegalAccessException, IOException, SQLException, ExecutionException {
        //??????fileAttachmentEntityListAll????????????map???key:caseId;value:?????????id???
        Map<Long, Set<Long>> caseId2TemplateGroupIdsMap = new HashMap<>();
        Set<Long> caseIdSet = new HashSet<>();//???????????????????????????
        for(FileAttachmentEntity fileAttachmentEntity : attAchmentList){
            caseIdSet.add(fileAttachmentEntity.getCaseId());
            Set<Long> templateGroupIdList = caseId2TemplateGroupIdsMap.get(fileAttachmentEntity.getCaseId());
            if(CollectionUtils.isEmpty(templateGroupIdList)){
                Set<Long>  templateGroupIdListNew = new HashSet<>();
                templateGroupIdListNew.add(fileAttachmentEntity.getTemplateGroupId());
                caseId2TemplateGroupIdsMap.put(fileAttachmentEntity.getCaseId(),templateGroupIdListNew);
            }else{
                templateGroupIdList.add(fileAttachmentEntity.getCaseId());
            }
        }
        log.info("????????????");
        //??????????????????
        folderServiceImpl.doFixedDefinedCompleteInCahe(userId,caseId2TemplateGroupIdsMap,errorList);
        log.info("????????????");
        //????????????????????????
        log.info("??????????????????");
        folderServiceImpl.doFixedCompleteByRemaining(caseIdSet,errorList);
        log.info("??????????????????");
        //????????????
        log.info("????????????");
        folderServiceImpl.doRemoveRepeat(caseIdSet,errorList);
        log.info("????????????");
        //?????????????????????????????????
        log.info("?????????????????????????????????");
        //???????????????????????????????????????????????????
        List<Long> fileIdList = fileDetailMapper.getIdsByFileAttachmentId(attAchmentList.get(0).getId());
        folderServiceImpl.regularCompleteField(userId,fileIdList,errorList);
        log.info("?????????????????????????????????");
    }

    private void importStatus(String token,Long userId) {
        RebotEntity rebotEntity = robotTokenMapper.selectRobotTokenByToken(token);
        FileAttachmentEntity fleAttachmentEntity4Sql = new FileAttachmentEntity();
        fleAttachmentEntity4Sql.setCaseId(rebotEntity.getCaseId());
        fleAttachmentEntity4Sql.setUserId(userId);
        List<FileAttachmentEntity> fileAttachmentEntityList = fileAttachmentMapper.selectFileAttachmentList(fleAttachmentEntity4Sql);
        if(!CollectionUtils.isEmpty(fileAttachmentEntityList)){
            for(FileAttachmentEntity fileAttachmentEntity : fileAttachmentEntityList){
                fileAttachmentEntity.setHasImport(true);
                fileAttachmentMapper.updateFileAttachment(fileAttachmentEntity);
            }
        }
    }


    private List<FileAttachmentEntity> createFolder(Map<String, List<Map<String, String>>> template2Datasmap, Long userId, Long caseId) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        //???????????????
        String folderName = new Date().getTime()+""+new Random().nextInt(99);
        MessageDigest md = MessageDigest.getInstance("MD5");
        md.update(folderName.getBytes("utf-8"));
        String md5 = new BigInteger(1, md.digest()).toString(16);
        String uploadPath = null;
        if("1".equals(PublicUtils.getConfigMap().get("environment"))){
            uploadPath = PublicUtils.getConfigMap().get("fileUploadPath_windows")+"/"+md5;
        }else{
            Calendar calendar = Calendar.getInstance();
            String year =String.valueOf(calendar.get(Calendar.YEAR));
            String month = String.valueOf(calendar.get(Calendar.MONTH)+1);
            String day = String.valueOf(calendar.get(Calendar.DATE));
            String fileServerBasePath = PublicUtils.getConfigMap().get("fileServerBasePath");
            uploadPath  = fileServerBasePath + "/"+year+"/"+month+"/"+day+"/"+caseId+"/"+md5;
        }
        File folder = new File(uploadPath);
        if (!folder.exists()) {
            folder.mkdirs();
        }
        //?????????csv??????
        saveCsv(template2Datasmap, uploadPath);
        //?????????????????????
        return saveAttAchment(caseId, userId, md5, uploadPath);
    }

    //????????????
    private Long createUser(String policeno, List<Error> errorList) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        //?????????????????????
        List<UserEntity> userEntityList = accountServiceDao.getUserEntitys(null, null, null, null,null, policeno, errorList);
        if(CollectionUtils.isEmpty(userEntityList)){
            //???????????????????????????????????????????????????
            UserEntity user = new UserEntity();
            user.setUserName(policeno);
            MessageDigest md = MessageDigest.getInstance("MD5");
            // ??????md5??????
            md.update(policeno.getBytes("utf-8"));
            // digest()??????????????????md5 hash??????????????????8?????????????????????md5 hash??????16??????hex?????????????????????8????????????
            // BigInteger????????????8????????????????????????16???hex??????????????????????????????????????????????????????hash???
            String password = new BigInteger(1, md.digest()).toString(16);
            user.setPassword(password);
            user.setPoliceNo(policeno);
            accountServiceDao.insertAccount(user, errorList);
            return user.getId();
        }
        return userEntityList.get(0).getId();
    }

    //????????????
    private Long createCase(Long userId,String token ,List<Error> errorList) {
        //?????????????????????token
        //????????????????????????????????????
        FileCaseEntity fileCaseEntity4Sql = new FileCaseEntity();
        fileCaseEntity4Sql.setCaseNo("rebot-"+token);
        List<FileCaseEntity> fileCaseEntityList = fileCaseMapper.selectFileCaseEntityList(fileCaseEntity4Sql);
        if(CollectionUtils.isEmpty(fileCaseEntityList)){
            FileCaseEntity fileCaseEntity = new FileCaseEntity();
            fileCaseEntity.setUserId(userId);
            fileCaseEntity.setCaseNo("rebot-"+token);
            fileCaseEntity.setCaseName("rebot-" + token);
            fileCaseMapper.fileCaseInsert(fileCaseEntity);
            //????????????token???robot_token????????????????????????robot_token ??????????????????
            RebotEntity rebotEntity = robotTokenMapper.selectRobotTokenByToken(token);
            if(null != rebotEntity){
                robotTokenMapper.updateRobotDateCaseIdByToken(token,fileCaseEntity.getId());
            }
            return fileCaseEntity.getId();
        }
        return fileCaseEntityList.get(0).getId();
    }

    //??????????????????
    private List<FileAttachmentEntity> saveAttAchment(Long caseId, Long userId, String md5, String uploadPath) {
        List<FileAttachmentEntity> fileAttachmentEntityList = new ArrayList<>();
        FileAttachmentEntity fileAttachmentEntity = new FileAttachmentEntity();
        fileAttachmentEntity.setTemplateGroupName("?????????-??????");
        fileAttachmentEntity.setTemplateGroupId(1L);
        fileAttachmentEntity.setHasImport(false);
        fileAttachmentEntity.setUserId(userId);
        fileAttachmentEntity.setFileType("");
        fileAttachmentEntity.setFileName(md5);
        fileAttachmentEntity.setHasDecompress(true);
        fileAttachmentEntity.setAttachment(uploadPath + ".zip");
        fileAttachmentEntity.setFileSize("0");
        fileAttachmentEntity.setCaseId(caseId);
        fileAttachmentEntityList.add(fileAttachmentEntity);
        fileAttachmentMapper.batchFileAttachmentInsert(fileAttachmentEntityList);
        return fileAttachmentEntityList;
    }

    //??????csv??????
        private void saveCsv(Map<String, List<Map<String, String>>> map2, String uploadPath) {
        //????????????,??????CSV??????????????????????????????
        int i = 1;
            for (Map.Entry entry : map2.entrySet()) {    //????????????
                //1.??????id
                String templateId = entry.getKey().toString();
                //????????????id????????????????????????
                FileTemplateEntity fileTemplateEntity = fileTemplateMapper.selectFileTemplateByPrimaryKey(Long.parseLong(templateId));
                String fileName = fileTemplateEntity.getTemplateKey() + "_" + i + ".csv";
                i++;
                //????????????????????????
                List<FileTemplateDetailEntity> fileTemplateDetailEntityList = fileTemplateDetailMapper.selectFileTemplateDetailByTemplateId(fileTemplateEntity.getId());
                if (CollectionUtils.isEmpty(fileTemplateDetailEntityList)) {
                    continue;
                }
                List<String> fieldNameList = new ArrayList<>();
                PublicUtils.fileTemplateDetailEntityListSort(fileTemplateDetailEntityList);
                StringBuilder heards = new StringBuilder();
                int k = 0;
                for (FileTemplateDetailEntity fileTemplateDetailEntity : fileTemplateDetailEntityList) {
                    if (k < fileTemplateDetailEntityList.size() - 1) {
                        heards.append("\"").append(fileTemplateDetailEntity.getFieldKey()).append("\"").append(",");
                    } else {
                        heards.append("\"").append(fileTemplateDetailEntity.getFieldKey()).append("\"");
                    }
                    fieldNameList.add(fileTemplateDetailEntity.getFieldName());
                    k++;
                }

                //2.??????????????????
                List<Map<String, String>> records = (List<Map<String, String>>) entry.getValue();
                List<String> recordList = new ArrayList<>();
                for (Map<String, String> map3 : records) {   //????????????
                    StringBuilder record = new StringBuilder();
                    int j = 0;
                    for (String fieldName : fieldNameList) {
                        if (j < fieldNameList.size() - 1) {
                            record.append("\"").append(map3.get(fieldName)).append("\"").append(",");
                        } else {
                            record.append("\"").append(map3.get(fieldName)).append("\"");
                        }
                        j++;
                    }
                    recordList.add(record.toString());
                }

                File csvFile = null;
                BufferedWriter csvWriter = null;

                try {
                    csvFile = new File(uploadPath + File.separator + fileName);
                    File parent = csvFile.getParentFile();
                    if (parent != null && !parent.exists()) {
                        parent.mkdirs();
                    }
                    csvFile.createNewFile();

                    // GB2312????????????????????????","
                    csvWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(csvFile), "GBK"), 1024);

                    // ???????????????????????????
                    csvWriter.write(heards.toString());
                    csvWriter.newLine();

                    // ??????????????????
                    for (String record : recordList) {
                        csvWriter.write(record);
                        csvWriter.newLine();
                    }
                    csvWriter.flush();
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    try {
                        csvWriter.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
    }

    //??????token??????
    private boolean initTokenInfo(Map<String ,List<Map<String,String>>> template2DatasMap,Long userId,String token,Long caseId,String date,String policeno, List<Error> errorList ) throws ParseException, IllegalAccessException, IOException, NoSuchAlgorithmException, InterruptedException, ExecutionException {
            try {
                RebotEntity rebotEntity = robotTokenMapper.selectRobotTokenByToken(token);
                if (null == rebotEntity) {
                    RebotEntity rebotEntity4Sql = new RebotEntity();
                    rebotEntity4Sql.setToken(token);
                    rebotEntity4Sql.setCaseId(caseId);
                    rebotEntity4Sql.setDate(date);
                    rebotEntity4Sql.setPoliceno(policeno);
                    robotTokenMapper.insertRobotToken(rebotEntity4Sql);
                    return true;
                }
                Date date1 = null;
                Date date2 = null;
                try {
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    date1 = sdf.parse(date);
                    date2 = sdf.parse(rebotEntity.getDate());
                } catch (Exception e) {
                    e.printStackTrace();
                }
                //?????????????????????
                //1.?????????????????????????????????????????????????????????????????????????????????????????????
                if (!date.equals(rebotEntity.getDate()) && date1.after(date2)) {
                    //1.????????????
                    User user = new User();
                    user.setId(userId);
                    String[] strArr = new String[1];
                    strArr[0] = String.valueOf(rebotEntity.getCaseId());
                    //?????????????????????????????????????????????????????????
                    FileCaseEntity fileCaseEntity = fileCaseMapper.selectFileCaseByPrimaryKey(rebotEntity.getCaseId());
                    if (null != fileCaseEntity) {
                        fileCaseServiceImpl.deleteCase(user, strArr, false, errorList);
                        caseId = createCase(userId, token, errorList);
                    }
                    //2.???????????????????????????
                    robotTokenMapper.updateRobotDateByToken(token, date, caseId);

                    //???????????????
                    List<FileAttachmentEntity> attAchmentList = createFolder(template2DatasMap, userId, caseId);
                    //??????????????????
                    fileParsingServiceImpl.fileParsing(userId,  attAchmentList.get(0).getId(), errorList);

                    //????????????
                    doRinse(errorList, userId, attAchmentList);

                    //??????file_attachment?????????????????????????????????
                    importStatus(token,userId);
                    return false;

                }
                if (!date.equals(rebotEntity.getDate()) && date1.before(date2)) {
                    return false;
                }
            }catch (Exception e){
                e.printStackTrace();
            }
            return   true;
    }
}
