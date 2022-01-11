package com.sugon.iris.sugonservice.impl.systemImpl;

import com.sugon.iris.sugoncommon.publicUtils.PublicUtils;
import com.sugon.iris.sugondata.jdbcTemplate.intf.system.AccountServiceDao;
import com.sugon.iris.sugondata.jdbcTemplate.intf.system.RoleServiceDao;
import com.sugon.iris.sugondomain.dtos.systemDtos.UserDto;
import com.sugon.iris.sugondomain.entities.jdbcTemplateEntity.systemEntities.RoleEntity;
import com.sugon.iris.sugondomain.entities.jdbcTemplateEntity.systemEntities.UserEntity;
import com.sugon.iris.sugondomain.entities.mybatiesEntity.db4.PoliceInfoEntity;
import com.sugon.iris.sugondomain.enums.ErrorCode_Enum;
import com.sugon.iris.sugonservice.service.systemService.AccountService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import com.sugon.iris.sugondomain.beans.baseBeans.Error;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import com.sugon.iris.sugondomain.beans.system.User;
import org.springframework.util.StringUtils;
import javax.annotation.Resource;

@Slf4j
@Service
public class AccountServiceImpl implements AccountService {

    @Resource
    private AccountServiceDao accountServiceDaoImpl;

    @Resource
    private RoleServiceDao roleServiceDaoImpl;

  public int saveAccount(UserDto userDto, List<Error> errorList){

      int result = 0;
      if(StringUtils.isEmpty(userDto.getPassword())){
          errorList.add(new Error(ErrorCode_Enum.IRIS_00_005.getCode(),ErrorCode_Enum.IRIS_00_005.getMessage(),""));
          return result;
      }
      try {
          UserEntity user = new UserEntity();
          PublicUtils.trans(userDto, user);
          List<UserEntity> userEntityList = accountServiceDaoImpl.getUserEntitys(null,userDto.getUserName(),null,null,null,null,errorList);
          if(null != userEntityList && userEntityList.size()>0){
              errorList.add(new Error(ErrorCode_Enum.IRIS_00_001.getCode(),ErrorCode_Enum.IRIS_00_001.getMessage(),""));
              return result;
          }
          //user.setId(accountServiceDaoImpl.getUser_seq(errorList));
          result = accountServiceDaoImpl.insertAccount(user, errorList);
      }catch(Exception e){
          e.printStackTrace();
      }
      return result;
  }

    @Override
    public int restPassword(UserDto userDto, List<Error> errorList) {
        int i =0;
        //通过原始密码和用户名查询用户是否存在
        List<UserEntity> userEntityList_01 = accountServiceDaoImpl.getUserEntitys(null,userDto.getUserName(),userDto.getOldPassword(),null,null,null,errorList);
        if(CollectionUtils.isEmpty(userEntityList_01)){
            errorList.add(new Error(ErrorCode_Enum.SYS_02_000.getCode(),"账号或密码错误",""));
            i = 0;
        }else if(userEntityList_01.size()>1){
            errorList.add(new Error("iris-00-004","存在多个账户请联系管理员",""));
            i =  userEntityList_01.size();
        }else{
            i = accountServiceDaoImpl.updateForPassword (userEntityList_01.get(0).getId(),userDto.getPassword(), errorList);
        }
        return 0;
    }

    public User getUserInfo(UserDto userDto, List<Error> errorList) throws IllegalAccessException {

        if(null == userDto || StringUtils.isEmpty(userDto.getPassword()) || userDto.getPassword().equals("1bbd886460827015e5d605ed44252251")){
            errorList.add(new Error(ErrorCode_Enum.SYS_02_000.getCode(),"密码错误",""));
            return null;
        }

        List<UserEntity> userEntityList_01 = accountServiceDaoImpl.getUserEntitys(null,userDto.getUserName(),null,null,null,null,errorList);
        if(CollectionUtils.isEmpty(userEntityList_01)){
            errorList.add(new Error(ErrorCode_Enum.SYS_02_000.getCode(),"请先注册账户",""));
            return null;
        }
        List<UserEntity> userEntityList_02 = accountServiceDaoImpl.getUserEntitys(null,userDto.getUserName(),userDto.getPassword(),null,null,null,errorList);
        if(CollectionUtils.isEmpty(userEntityList_02)){
            errorList.add(new Error(ErrorCode_Enum.IRIS_00_003.getCode(),"用户名或密码错误",""));
            return null;
        }else if(userEntityList_02.size()>1){
            errorList.add(new Error("iris-00-004","存在多个账户请联系管理员",""));
            return null;
        }
        User user = new User();
        PublicUtils.trans(userEntityList_02.get(0), user);

        //获取该用户加入的用户组
        List<RoleEntity> roleEntityList =  roleServiceDaoImpl.getRolesByUserId(user.getId(),errorList);
        int i = 0;
        for(RoleEntity roleEntity : roleEntityList){
              if(roleEntity.getId() == 1){
                  user.setSystemUser(true);
                  i ++;
              }
            if(roleEntity.getId() == 2){
                user.setEconomicUser(true);
                 i ++;
            }
            if(i == 2){
                break;
            }
        }

        return user;
    }

    //Ca用户登录
    public User getUserInfoForCa(UserDto userDto, List<Error> errorList) throws IllegalAccessException {

        log.info("警号:"+userDto.getPoliceNo());
        log.info("身份证号:"+userDto.getIdCard());
        //校验用户是否存在
        Map<String,PoliceInfoEntity> policeInfoMap  = PublicUtils.policeInfoMap;
        PoliceInfoEntity policeInfoEntity = policeInfoMap.get(userDto.getPoliceNo()+userDto.getIdCard());
        if(policeInfoEntity == null){
            errorList.add(new Error(ErrorCode_Enum.IRIS_00_003.getCode(),"key用户不存在，或者还未进行用户同步！",""));
            return null;
        }

        List<UserEntity> userEntityList_02 = accountServiceDaoImpl.getUserEntitys(null,null,null,null,null,userDto.getPoliceNo(),errorList);

        User user = new User();
        if(null != policeInfoEntity) {
            user.setXm(policeInfoEntity.getXm());
            user.setXtyhbmbh(policeInfoEntity.getXtyhbmbh());
            user.setXtyhbmmc(policeInfoEntity.getXtyhbmmc());
        }

        if(CollectionUtils.isEmpty(userEntityList_02)){
            //ca登录用户不存在则自动注册
            UserEntity userEntity = new UserEntity();
            PublicUtils.trans(userDto, userEntity);
            userEntity.setUserName(userDto.getPoliceNo());
            userEntity.setFlag(1);
            userEntity.setPassword("1bbd886460827015e5d605ed44252251");
            accountServiceDaoImpl.insertAccount(userEntity,errorList);
            PublicUtils.trans(userEntity, user);
        }else if(userEntityList_02.size()>1){
            errorList.add(new Error("iris-00-004","存在多个账户请联系管理员",""));
            return null;
        }else{
            PublicUtils.trans(userEntityList_02.get(0), user);
        }


        //获取该用户加入的用户组
        List<RoleEntity> roleEntityList =  roleServiceDaoImpl.getRolesByUserId(user.getId(),errorList);
        int i = 0;
        if(!CollectionUtils.isEmpty(roleEntityList)) {
            for (RoleEntity roleEntity : roleEntityList) {
                if (roleEntity.getId() == 1) {
                    user.setSystemUser(true);
                    i++;
                }
                if (roleEntity.getId() == 2) {
                    user.setEconomicUser(true);
                    i++;
                }
                if (i == 2) {
                    break;
                }
            }
        }
        return user;
    }

    public int updateUser(UserDto userDto, List<Error> errorList) throws IllegalAccessException {
        UserEntity user = new UserEntity();
        PublicUtils.trans(userDto, user);
        return accountServiceDaoImpl.update(user,errorList);
  }

    /**
     * 作者:SharpChan
     * 日期：2020.08.20
     * 描述：用于用户审核模块查看所有用户
     */
    public List<User> getUserInfoCheck(String keyWord,Integer flag, List<Error> errorList ) throws IllegalAccessException {
        List<UserEntity> userEntityList = accountServiceDaoImpl.getUserEntitysForCheck(keyWord,flag,errorList);
        List<User> userList =null;
        if(!CollectionUtils.isEmpty(userEntityList)){
            userList = new ArrayList<User>();
            for (UserEntity entity : userEntityList){
                User user = new User();
                PublicUtils.trans(entity, user);
                userList.add(user);
            }
        }
        return userList;
    }

    @Override
    public int alterUserStatus(Long id ,Integer flag, List<Error> errorList) {
        return accountServiceDaoImpl.updateForCheck(id,flag,errorList);
    }

    @Override
    public int deleteUser(Long id , List<Error> errorList) {
        return accountServiceDaoImpl.deleteUser(id,errorList);
    }
}
