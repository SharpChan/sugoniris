package com.sugon.iris.sugonservice.impl.dataSyncServiceImpl;

import com.sugon.iris.sugondata.jdbcTemplate.intf.system.AccountServiceDao;
import com.sugon.iris.sugondata.mybaties.mapper.db1.JM_t_model_local_tableMapper;
import com.sugon.iris.sugondata.mybaties.mapper.db1.JM_t_system_userMapper;
import com.sugon.iris.sugondata.mybaties.mapper.db1.JM_t_system_user_roleMapper;
import com.sugon.iris.sugondata.mybaties.mapper.db2.FileTableMapper;
import com.sugon.iris.sugondomain.beans.baseBeans.Error;
import com.sugon.iris.sugondomain.entities.jdbcTemplateEntity.systemEntities.UserEntity;
import com.sugon.iris.sugondomain.entities.mybatiesEntity.db1.JM_t_model_local_tableEntity;
import com.sugon.iris.sugondomain.entities.mybatiesEntity.db1.JM_t_system_userEntity;
import com.sugon.iris.sugondomain.entities.mybatiesEntity.db1.JM_t_system_user_roleEntity;
import com.sugon.iris.sugondomain.entities.mybatiesEntity.db2.FileTableEntity;
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


    @Override
    public void dataSync(List<Error> errorList) {

        //获取该平台的所有分配（经侦）角色的用户
        List<UserEntity> userEntityList = accountServiceDaoImpl.getUserEntityIsEconomic(errorList);
        //获取建模平台的所有用户
        List<JM_t_system_userEntity> jM_t_system_userEntityList =jM_t_system_userMapper.queryAllJMUsers();

        List<UserEntity>  userEntityNoSyncList = new ArrayList<>();

        List<UserEntity>  userEntityHasSyncList = new ArrayList<>();

       //获取 没有同步的用户,和已经同步的用户
        for(UserEntity userEntity : userEntityList) {
            boolean flag = true;
            for (JM_t_system_userEntity jM_t_system_userEntity : jM_t_system_userEntityList) {
                if(userEntity.getUserName().equals(jM_t_system_userEntity.getUserName())){
                    flag = false;
                    userEntityHasSyncList.add(userEntity);
                    break;
                }
            }
            if(flag){
                userEntityNoSyncList.add(userEntity);
            }
        }

        //进行用户同步
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
        //插入用户和角色对应信息
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


        //需要插入建模平台的表
        List<JM_t_model_local_tableEntity> jM_t_model_local_tableEntityList = new ArrayList<>();
        //对未同步用户，同步数据表
        for(JM_t_system_userEntity jM_t_system_userEntity : jM_t_system_userEntityToBeSyncList){

            //获取该用户数据表
            List<FileTableEntity>  fileTableEntityList =  fileTableMapper.findAllFileTablesByUserId(jM_t_system_userEntity.getIris_user_id());

            //该用户下需要同步的数据表
            for(FileTableEntity fileTableEntity : fileTableEntityList){
                JM_t_model_local_tableEntity jM_t_model_local_tableEntity = new JM_t_model_local_tableEntity();
                jM_t_model_local_tableEntity.setTableName(fileTableEntity.getTableName());
                jM_t_model_local_tableEntity.setCreateTime(new Timestamp(System.currentTimeMillis()));
                jM_t_model_local_tableEntity.setUserId(jM_t_system_userEntity.getId());
                jM_t_model_local_tableEntityList.add(jM_t_model_local_tableEntity);
            }
        }

        //插入数据库
        if(!CollectionUtils.isEmpty(jM_t_model_local_tableEntityList)){
            jM_t_model_local_tableMapper.batchTModelLocalTableInsert(jM_t_model_local_tableEntityList);
        }
    }
}
