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
import com.sugon.iris.sugonservice.service.rocketMqService.DealWithCrawlerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import javax.annotation.Resource;
import java.io.*;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
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

    @Override
    public void dealWithBankData(String message) throws IOException, NoSuchAlgorithmException, InterruptedException, ExecutionException, IllegalAccessException, ParseException {
        try {
            Gson gson = new Gson();
            Map<String, Object> map = gson.fromJson(message, Map.class);
            List<Error> errorList = new ArrayList<>();
            String serialNumber = map.get("token").toString();
            String token = serialNumber.substring(0, 22);
            String date = serialNumber.substring(22);
            String policeno = map.get("policeno").toString();

            List<Map<String, List<Map<String, String>>>> templateDdatas = (List<Map<String, List<Map<String, String>>>>) map.get(token);
            if (CollectionUtils.isEmpty(templateDdatas)) {
                return;
            }
            //创建用户
            Long userId = this.createUser(policeno, errorList);
            //案件创建
            Long caseId = createCase(userId, token, errorList);
            //初始化token 信息,如果同一个token 新来的数据的时间早于入库的时间，则直接舍弃
            if (!initTokenInfo(templateDdatas, userId, token, caseId, date, policeno, errorList)) {
                return;
            }
            //创建文件夹
            Long attAchmentId = createFolder(templateDdatas, userId, caseId);
            //进行文件解析
            fileParsingServiceImpl.fileParsing(userId, attAchmentId, errorList);

            //找出file_attachment信息，设置数据同步状态
            importStatus(token,userId);
        }catch (Exception e){
            e.printStackTrace();
        }
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

    private Long createFolder(List<Map<String, List<Map<String, String>>>> templateDdatas, Long userId, Long caseId) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        //创建文件夹
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
        //保存为csv文件
        saveCsv(templateDdatas, uploadPath);
        //文件夹信息入库
        return saveAttAchment(caseId, userId, md5, uploadPath);
    }

    //创建用户
    private Long createUser(String policeno, List<Error> errorList) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        //用警号查询用户
        List<UserEntity> userEntityList = accountServiceDao.getUserEntitys(null, null, null, null, policeno, errorList);
        if(CollectionUtils.isEmpty(userEntityList)){
            //直接内置用户，用户名和密码都是警号
            UserEntity user = new UserEntity();
            user.setUserName(policeno);
            MessageDigest md = MessageDigest.getInstance("MD5");
            // 计算md5函数
            md.update(policeno.getBytes("utf-8"));
            // digest()最后确定返回md5 hash值，返回值为8为字符串。因为md5 hash值是16位的hex值，实际上就是8位的字符
            // BigInteger函数则将8位的字符串转换成16位hex值，用字符串来表示；得到字符串形式的hash值
            String password = new BigInteger(1, md.digest()).toString(16);
            user.setPassword(password);
            user.setPoliceNo(policeno);
            accountServiceDao.insertAccount(user, errorList);
            List<UserEntity> userEntityList2 = accountServiceDao.getUserEntitys(null, null, null, null, policeno, errorList);
            return userEntityList2.get(0).getId();
        }
        return userEntityList.get(0).getId();
    }

    //创建案件
    private Long createCase(Long userId,String token ,List<Error> errorList) {
        //案件编号存的是token
        //通过案件编号查询案件信息
        FileCaseEntity fileCaseEntity4Sql = new FileCaseEntity();
        fileCaseEntity4Sql.setCaseNo("rebot-"+token);
        List<FileCaseEntity> fileCaseEntityList = fileCaseMapper.selectFileCaseEntityList(fileCaseEntity4Sql);
        if(CollectionUtils.isEmpty(fileCaseEntityList)){
            FileCaseEntity fileCaseEntity = new FileCaseEntity();
            fileCaseEntity.setUserId(userId);
            fileCaseEntity.setCaseNo("rebot-"+token);
            fileCaseEntity.setCaseName("rebot-" + token);
            fileCaseMapper.fileCaseInsert(fileCaseEntity);
            //如果通过token查robot_token存在数据，则更新robot_token 的案件序列号
            RebotEntity rebotEntity = robotTokenMapper.selectRobotTokenByToken(token);
            if(null != rebotEntity){
                robotTokenMapper.updateRobotDateCaseIdByToken(token,fileCaseEntity.getId());
            }
            return fileCaseEntity.getId();
        }
        return fileCaseEntityList.get(0).getId();
    }

    //保存文件信息
    private Long saveAttAchment(Long caseId, Long userId, String md5, String uploadPath) {
        List<FileAttachmentEntity> fileAttachmentEntityList = new ArrayList<>();
        FileAttachmentEntity fileAttachmentEntity = new FileAttachmentEntity();
        fileAttachmentEntity.setTemplateGroupName("模板组-经侦");
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
        return fileAttachmentEntity.getId();
    }

    //保存csv文件
    private void saveCsv(List<Map<String, List<Map<String, String>>>> templateDatas, String uploadPath) {
        //进行解析,生成CSV文件并且保存到服务器
        int i = 1;
        for(Map<String ,List<Map<String,String>>> map2 : templateDatas) {
            for (Map.Entry entry : map2.entrySet()) {    //模板层面
                //1.模板id
                String templateId = entry.getKey().toString();
                //通过模板id获取获取模板信息
                FileTemplateEntity fileTemplateEntity = fileTemplateMapper.selectFileTemplateByPrimaryKey(Long.parseLong(templateId));
                String fileName = fileTemplateEntity.getTemplateKey() + "_" + i + ".csv";
                i++;
                //获取模板字段信息
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

                //2.模板对应的值
                List<Map<String, String>> records = (List<Map<String, String>>) entry.getValue();
                List<String> recordList = new ArrayList<>();
                for (Map<String, String> map3 : records) {   //字段层面
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

                    // GB2312使正确读取分隔符","
                    csvWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(csvFile), "GBK"), 1024);

                    // 写入文件头部标题行
                    csvWriter.write(heards.toString());
                    csvWriter.newLine();

                    // 写入文件内容
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
    }

    //保存token信息
    private boolean initTokenInfo(List<Map<String ,List<Map<String,String>>>> templateDdatas,Long userId,String token,Long caseId,String date,String policeno, List<Error> errorList ) throws ParseException, IllegalAccessException, IOException, NoSuchAlgorithmException, InterruptedException, ExecutionException {
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
                //已经有文件同步
                //1.查看时间是否发生改变，且时间大于之前的时间，则把之前的数据清除
                if (!date.equals(rebotEntity.getDate()) && date1.after(date2)) {
                    //1.删除案件
                    User user = new User();
                    user.setId(userId);
                    String[] strArr = new String[1];
                    strArr[0] = String.valueOf(rebotEntity.getCaseId());
                    //删除前查询一下，该案件是否已经被删除了
                    FileCaseEntity fileCaseEntity = fileCaseMapper.selectFileCaseByPrimaryKey(rebotEntity.getCaseId());
                    if (null != fileCaseEntity) {
                        fileCaseServiceImpl.deleteCase(user, strArr, false, errorList);
                        caseId = createCase(userId, token, errorList);
                    }
                    //2.更新时间和案件编号
                    robotTokenMapper.updateRobotDateByToken(token, date, caseId);

                    //创建文件夹
                    Long attAchmentId = createFolder(templateDdatas, userId, caseId);
                    //进行文件解析
                    fileParsingServiceImpl.fileParsing(userId, attAchmentId, errorList);

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
