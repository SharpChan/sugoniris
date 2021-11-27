package com.sugon.iris.sugonservice.impl.rocketMqServiceImpl;

import com.google.gson.Gson;
import com.sugon.iris.sugoncommon.publicUtils.PublicUtils;
import com.sugon.iris.sugondata.jdbcTemplate.intf.system.AccountServiceDao;
import com.sugon.iris.sugondata.mybaties.mapper.db2.FileCaseMapper;
import com.sugon.iris.sugondata.mybaties.mapper.db2.FileTemplateDetailMapper;
import com.sugon.iris.sugondata.mybaties.mapper.db2.FileTemplateMapper;
import com.sugon.iris.sugondata.mybaties.mapper.db2.RobotTokenMapper;
import com.sugon.iris.sugondomain.beans.baseBeans.Error;
import com.sugon.iris.sugondomain.entities.jdbcTemplateEntity.systemEntities.UserEntity;
import com.sugon.iris.sugondomain.entities.mybatiesEntity.db2.FileCaseEntity;
import com.sugon.iris.sugondomain.entities.mybatiesEntity.db2.FileTemplateDetailEntity;
import com.sugon.iris.sugondomain.entities.mybatiesEntity.db2.FileTemplateEntity;
import com.sugon.iris.sugondomain.entities.mybatiesEntity.db2.RebotEntity;
import com.sugon.iris.sugonservice.service.rocketMqService.DealWithCrawlerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import javax.annotation.Resource;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;

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

    @Override
    public void dealWithBankData(String message) throws UnsupportedEncodingException, NoSuchAlgorithmException {

        Gson gson = new Gson();
        Map<String , Object> map = gson.fromJson(message,Map.class);
        List<Error> errorList = new ArrayList<>();
        String serialNumber = map.get("token").toString();
        String token =   serialNumber.substring(0,22);
        String date =   serialNumber.substring(22);
        String policeno =  map.get("policeno").toString();

        Map<String ,List<Map<String,String>>> map2 = (Map<String, List<Map<String, String>>>) map.get(token);
        if(CollectionUtils.isEmpty(map2)){
           return;
        }
            //案件创建
        Long caseId = createCase(map, token, policeno, errorList);
            //初始化token 信息
        initTokenInfo(token,caseId,date,policeno);

        //创建文件夹
        String uploadPath = null;
        if("1".equals(PublicUtils.getConfigMap().get("environment"))){
            uploadPath = PublicUtils.getConfigMap().get("fileUploadPath_windows");
        }else{
            Calendar calendar = Calendar.getInstance();
            String year =String.valueOf(calendar.get(Calendar.YEAR));
            String month = String.valueOf(calendar.get(Calendar.MONTH)+1);
            String day = String.valueOf(calendar.get(Calendar.DATE));
            String fileServerBasePath = PublicUtils.getConfigMap().get("fileServerBasePath");
            String fileServerPAth = fileServerBasePath + "/"+year+"/"+month+"/"+day+"/"+caseId+"/";
        }


        //进行解析
        for(Map.Entry entry : map2.entrySet()){
             //模板
             String templateId = entry.getKey().toString();
             //通过模板id获取获取模板信息
             FileTemplateEntity fileTemplateEntity = fileTemplateMapper.selectFileTemplateByPrimaryKey(Long.parseLong(templateId));
             //获取模板字段信息
             List<FileTemplateDetailEntity> fileTemplateDetailEntityList = fileTemplateDetailMapper.selectFileTemplateDetailByTemplateId(fileTemplateEntity.getId());


             //模板对应的值
             List<Map<String,String>> records = (List<Map<String, String>>) entry.getValue();
             for(Map<String,String> map3 : records){
                 for(Map.Entry entry1 : map3.entrySet()){

                 }
             }
        }
    }

    private void initTokenInfo(String token,Long caseId,String date,String policeno ){
            RebotEntity rebotEntity = robotTokenMapper.selectRobotTokenByToken(token);
            if (null == rebotEntity) {
                RebotEntity rebotEntity4Sql = new RebotEntity();
                rebotEntity4Sql.setToken(token);
                rebotEntity4Sql.setCaseId(caseId);
                rebotEntity4Sql.setDate(date);
                rebotEntity4Sql.setPoliceno(policeno);
                robotTokenMapper.insertRobotToken(rebotEntity4Sql);
            }
        //判断是否  时间发送改变，发生改变则进行回退

    }

    private Long createCase(Map<String, Object> map,String token ,String policeno,List<Error> errorList) throws NoSuchAlgorithmException, UnsupportedEncodingException {
      RebotEntity rebotEntity = robotTokenMapper.selectRobotTokenByToken(token);
      FileCaseEntity fileCaseEntity = new FileCaseEntity();
      if (null == rebotEntity) {
          //通过警号查询userId

          List<UserEntity> userEntityList = accountServiceDao.getUserEntitys(null, null, null, null, policeno, errorList);
          if (CollectionUtils.isEmpty(userEntityList)) {
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
          }
          UserEntity userEntity = accountServiceDao.getUserEntitys(null, policeno, null, null, null, errorList).get(0);
          //查询案件有没有被登记
          FileCaseEntity fileCaseEntity4Sql = new FileCaseEntity();
          fileCaseEntity4Sql.setCaseNo(token);
          List<FileCaseEntity> fileCaseEntityList = fileCaseMapper.selectFileCaseEntityList(fileCaseEntity4Sql);
          if (CollectionUtils.isEmpty(fileCaseEntityList)) {
              fileCaseEntity.setUserId(userEntity.getId());
              fileCaseEntity.setCaseNo(token);
              fileCaseEntity.setCaseName("rebot-" + token);
              fileCaseMapper.fileCaseInsert(fileCaseEntity);
          }
      }
      return fileCaseEntity.getId();
    }
}
