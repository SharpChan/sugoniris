package com.sugon.iris.sugonservice.impl.dataSyncServiceImpl;

import com.sugon.iris.sugondata.jdbcTemplate.intf.system.AccountServiceDao;
import com.sugon.iris.sugondata.mybaties.mapper.db1.JM_t_model_local_tableMapper;
import com.sugon.iris.sugondata.mybaties.mapper.db1.JM_t_model_local_tablecolumnsMapper;
import com.sugon.iris.sugondata.mybaties.mapper.db1.JM_t_system_userMapper;
import com.sugon.iris.sugondata.mybaties.mapper.db1.JM_t_system_user_roleMapper;
import com.sugon.iris.sugondata.mybaties.mapper.db2.FileTableMapper;
import com.sugon.iris.sugondata.mybaties.mapper.db2.FileTemplateDetailMapper;
import com.sugon.iris.sugondomain.beans.baseBeans.Error;
import com.sugon.iris.sugondomain.entities.jdbcTemplateEntity.systemEntities.UserEntity;
import com.sugon.iris.sugondomain.entities.mybatiesEntity.db1.JM_t_model_local_tableEntity;
import com.sugon.iris.sugondomain.entities.mybatiesEntity.db1.JM_t_model_local_tablecolumnsEntity;
import com.sugon.iris.sugondomain.entities.mybatiesEntity.db1.JM_t_system_userEntity;
import com.sugon.iris.sugondomain.entities.mybatiesEntity.db1.JM_t_system_user_roleEntity;
import com.sugon.iris.sugondomain.entities.mybatiesEntity.db2.FileTableEntity;
import com.sugon.iris.sugondomain.entities.mybatiesEntity.db2.FileTemplateDetailEntity;
import com.sugon.iris.sugonservice.service.dataSyncService.DataSyncService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import javax.annotation.Resource;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Service
public class DataSyncServiceImpl implements DataSyncService {

    @Autowired
    private AccountServiceDao accountServiceDaoImpl;

    @Resource
    private JM_t_system_userMapper jM_t_system_userMapper;

    @Resource
    private JM_t_system_user_roleMapper jM_t_system_user_roleMapper;

    @Resource
    private FileTableMapper fileTableMapper;

    @Resource
    private JM_t_model_local_tableMapper jM_t_model_local_tableMapper;

    @Resource
    private FileTemplateDetailMapper fileTemplateDetailMapper;

    @Resource
    private JM_t_model_local_tablecolumnsMapper jM_t_model_local_tablecolumnsMapper;



    @Override
    public void dataSync(List<Error> errorList) {

        //?????????????????????????????????????????????????????????
        List<UserEntity> userEntityList = accountServiceDaoImpl.getUserEntityIsEconomic(errorList);
        //?????????????????????????????????
        List<JM_t_system_userEntity> jM_t_system_userEntityList =jM_t_system_userMapper.queryAllJMUsers();

        List<UserEntity>  userEntityNoSyncList = new ArrayList<>();

        List<UserEntity>  userEntityHasSyncList = new ArrayList<>();

       //?????? ?????????????????????,????????????????????????
        for(UserEntity userEntity : userEntityList) {
            boolean flag = true;
            for (JM_t_system_userEntity jM_t_system_userEntity : jM_t_system_userEntityList) {
                if(userEntity.getUserName().equals(jM_t_system_userEntity.getUserName())){
                    flag = false;
                    userEntity.setJmUserId(jM_t_system_userEntity.getId());
                    userEntityHasSyncList.add(userEntity);
                    break;
                }
            }
            if(flag){
                userEntityNoSyncList.add(userEntity);
            }
        }

        //??????????????????
        List<JM_t_system_userEntity> jM_t_system_userEntityToBeSyncList =new ArrayList<>();
        for(UserEntity userEntityBean : userEntityNoSyncList){
            JM_t_system_userEntity jM_t_system_userEntity = new JM_t_system_userEntity();
            jM_t_system_userEntity.setUserName(userEntityBean.getUserName());
            jM_t_system_userEntity.setAccountNo(userEntityBean.getUserName());
            jM_t_system_userEntity.setCardNo(userEntityBean.getIdCard());
            jM_t_system_userEntity.setAccountNo(userEntityBean.getUserName());
            jM_t_system_userEntity.setPassWord(userEntityBean.getPassword());
            jM_t_system_userEntity.setIris_user_id(userEntityBean.getId());
            jM_t_system_userEntityToBeSyncList.add(jM_t_system_userEntity);
        }
        if(!CollectionUtils.isEmpty(jM_t_system_userEntityToBeSyncList)){
            jM_t_system_userMapper.batchTSystemUserInsert(jM_t_system_userEntityToBeSyncList);
        }
        //?????????????????????????????????
        List<JM_t_system_user_roleEntity> jM_t_system_user_roleEntityList = new ArrayList<>();
        if(!CollectionUtils.isEmpty(jM_t_system_userEntityToBeSyncList)){
            for(JM_t_system_userEntity jM_t_system_userEntity : jM_t_system_userEntityToBeSyncList) {
                JM_t_system_user_roleEntity jM_t_system_user_roleEntity = new JM_t_system_user_roleEntity();
                jM_t_system_user_roleEntity.setRoleId(4);
                jM_t_system_user_roleEntity.setUserId(jM_t_system_userEntity.getId());
                jM_t_system_user_roleEntityList.add(jM_t_system_user_roleEntity);
            }
        }
        if(!CollectionUtils.isEmpty(jM_t_system_user_roleEntityList)){
            jM_t_system_user_roleMapper.batchTSystemUserRoleInsert(jM_t_system_user_roleEntityList);
        }


        //??????????????????????????????
        List<JM_t_model_local_tableEntity> jM_t_model_local_tableEntityList = new ArrayList<>();
        List<FileTableEntity> fileTableEntityAllNeedAddList  = new ArrayList<>();
        //????????????????????????????????????
        for(JM_t_system_userEntity jM_t_system_userEntity : jM_t_system_userEntityToBeSyncList){

            //??????????????????????????????
            List<FileTableEntity>  fileTableEntityList =  fileTableMapper.findAllFileTablesByUserId(jM_t_system_userEntity.getIris_user_id());
            if(!CollectionUtils.isEmpty(fileTableEntityList)){
                fileTableEntityAllNeedAddList.addAll(fileTableEntityList);
            }

            //????????????????????????????????????
            for(FileTableEntity fileTableEntity : fileTableEntityList){
                JM_t_model_local_tableEntity jM_t_model_local_tableEntity = new JM_t_model_local_tableEntity();
                jM_t_model_local_tableEntity.setTableName(fileTableEntity.getTableName().replaceAll("_localfile",""));
                jM_t_model_local_tableEntity.setFileTemplateId(fileTableEntity.getFileTemplateId());
                jM_t_model_local_tableEntity.setCreateTime(new Timestamp(System.currentTimeMillis()));
                jM_t_model_local_tableEntity.setUpdateTime(new Timestamp(System.currentTimeMillis()));
                jM_t_model_local_tableEntity.setUserId(jM_t_system_userEntity.getId());
                jM_t_model_local_tableEntity.setAreaType("1");
                jM_t_model_local_tableEntityList.add(jM_t_model_local_tableEntity);
            }
        }

        //????????????????????????
        if(!CollectionUtils.isEmpty(jM_t_model_local_tableEntityList)) {
            addTableToJm(jM_t_model_local_tableEntityList);
        }

        //???????????????????????????????????????????????????
        List<Integer> jM_t_model_local_tableEntityNeedDelIdList = new ArrayList<>();//??????????????????
        List<JM_t_model_local_tableEntity> jM_t_model_local_tableEntityNeedAddList = new ArrayList<>();//???????????????
        for(UserEntity userEntity : userEntityHasSyncList){
            //?????????????????????????????????
            List<JM_t_model_local_tableEntity> jM_t_model_local_tableEntityJMoldList =  jM_t_model_local_tableMapper.queryAllModelLocalTableByUserId(userEntity.getJmUserId(),"1");
            //????????????????????????
            List<FileTableEntity>  fileTableEntityList =  fileTableMapper.findAllFileTablesByUserId(userEntity.getId());

            //???????????????
            for(JM_t_model_local_tableEntity jM_t_model_local_tableEntity : jM_t_model_local_tableEntityJMoldList){
                  boolean flag = true;
                  for(FileTableEntity fileTableEntity : fileTableEntityList){
                       if(jM_t_model_local_tableEntity.getTableName().equals(fileTableEntity.getTableName())){
                           flag = false;
                           break;
                       }
                  }
                  if(flag){
                      jM_t_model_local_tableEntityNeedDelIdList.add(jM_t_model_local_tableEntity.getId());
                  }
            }

            //???????????????
            for(FileTableEntity fileTableEntity : fileTableEntityList){
                boolean flag = true;
                for(JM_t_model_local_tableEntity jM_t_model_local_tableEntity : jM_t_model_local_tableEntityJMoldList){
                        if(fileTableEntity.getTableName().equals(jM_t_model_local_tableEntity.getTableName())){
                            flag = false;
                            break;
                        }
                }
                if(flag){
                    JM_t_model_local_tableEntity jM_t_model_local_tableEntity = new JM_t_model_local_tableEntity();
                    jM_t_model_local_tableEntity.setTableName(fileTableEntity.getTableName().replaceAll("_localfile",""));
                    jM_t_model_local_tableEntity.setFileTemplateId(fileTableEntity.getFileTemplateId());
                    jM_t_model_local_tableEntity.setCreateTime(new Timestamp(System.currentTimeMillis()));
                    jM_t_model_local_tableEntity.setUpdateTime(new Timestamp(System.currentTimeMillis()));
                    jM_t_model_local_tableEntity.setUserId(userEntity.getJmUserId());
                    jM_t_model_local_tableEntity.setExtend1(fileTableEntity.getTitle());
                    jM_t_model_local_tableEntity.setExtend2(fileTableEntity.getTableName());
                    jM_t_model_local_tableEntity.setAreaType("1");
                    jM_t_model_local_tableEntityNeedAddList.add(jM_t_model_local_tableEntity);
                }

            }

        }

        //??????????????????
        if(!CollectionUtils.isEmpty(jM_t_model_local_tableEntityNeedDelIdList)){
            jM_t_model_local_tableMapper.deleteTModelLocalTableByIds(jM_t_model_local_tableEntityNeedDelIdList);
            jM_t_model_local_tablecolumnsMapper.deleteTModelLocalTablecolumnsByTableIds(jM_t_model_local_tableEntityNeedDelIdList);
        }
        if(!CollectionUtils.isEmpty(jM_t_model_local_tableEntityNeedAddList)) {
            addTableToJm(jM_t_model_local_tableEntityNeedAddList);
        }
    }

    private void addTableToJm(List<JM_t_model_local_tableEntity> jM_t_model_local_tableEntityList) {
        //?????????
        List<JM_t_model_local_tablecolumnsEntity> jM_t_model_local_tablecolumnsNeedAddEntityList = new ArrayList<>();
        //??????????????????
        jM_t_model_local_tableMapper.batchTModelLocalTableInsert(jM_t_model_local_tableEntityList);

        //????????????????????????
        for (JM_t_model_local_tableEntity jM_t_model_local_tableEntity : jM_t_model_local_tableEntityList) {
                List<FileTemplateDetailEntity> fileTemplateDetailEntityList = fileTemplateDetailMapper.selectFileTemplateDetailByTemplateId(jM_t_model_local_tableEntity.getFileTemplateId());
                for (FileTemplateDetailEntity fileTemplateDetailEntity : fileTemplateDetailEntityList) {
                    JM_t_model_local_tablecolumnsEntity jM_t_model_local_tablecolumnsEntity = new JM_t_model_local_tablecolumnsEntity();
                    jM_t_model_local_tablecolumnsEntity.setFieldName(fileTemplateDetailEntity.getFieldName());
                    jM_t_model_local_tablecolumnsEntity.setFieldreamrk(fileTemplateDetailEntity.getFieldKey());
                    jM_t_model_local_tablecolumnsEntity.setFieldtype("1");
                    jM_t_model_local_tablecolumnsEntity.setFieldlength(255);
                    jM_t_model_local_tablecolumnsEntity.setTableid(jM_t_model_local_tableEntity.getId());
                    jM_t_model_local_tablecolumnsEntity.setIsprimarykey(1);
                    jM_t_model_local_tablecolumnsEntity.setIsnull(1);
                    jM_t_model_local_tablecolumnsNeedAddEntityList.add(jM_t_model_local_tablecolumnsEntity);
                }
            }

        if (!CollectionUtils.isEmpty(jM_t_model_local_tablecolumnsNeedAddEntityList)) {
            jM_t_model_local_tablecolumnsMapper.batchTModelLocalTablecolumnsInsert(jM_t_model_local_tablecolumnsNeedAddEntityList);
        }
    }
}
