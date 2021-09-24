package com.sugon.iris.sugonservice.impl.systemImpl;

import com.sugon.iris.sugoncommon.publicUtils.PublicUtils;
import com.sugon.iris.sugondata.jdbcTemplate.intf.system.AccountServiceDao;
import com.sugon.iris.sugondata.jdbcTemplate.intf.system.RoleServiceDao;
import com.sugon.iris.sugondomain.dtos.systemDtos.UserDto;
import com.sugon.iris.sugondomain.entities.jdbcTemplateEntity.systemEntities.RoleEntity;
import com.sugon.iris.sugondomain.entities.jdbcTemplateEntity.systemEntities.UserEntity;
import com.sugon.iris.sugondomain.enums.ErrorCode_Enum;
import com.sugon.iris.sugonservice.service.systemService.AccountService;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import com.sugon.iris.sugondomain.beans.baseBeans.Error;
import java.util.ArrayList;
import java.util.List;
import com.sugon.iris.sugondomain.beans.system.User;
import javax.annotation.Resource;

@Service
public class AccountServiceImpl implements AccountService {

    @Resource
    private AccountServiceDao accountServiceDaoImpl;

    @Resource
    private RoleServiceDao roleServiceDaoImpl;


  public int saveAccount(UserDto userDto, List<Error> errorList){
      int result = 0;
      try {
          UserEntity user = new UserEntity();
          PublicUtils.trans(userDto, user);
          List<UserEntity> userEntityList = accountServiceDaoImpl.getUserEntitys(null,userDto.getUserName(),null,null,errorList);
          if(null != userEntityList && userEntityList.size()>0){
              errorList.add(new Error("{iris-00-001}","已经注册请直接登录！",""));
              return result;
          }
          user.setId(accountServiceDaoImpl.getUser_seq(errorList));
          result = accountServiceDaoImpl.insertAccount(user, errorList);
      }catch(Exception e){
          e.printStackTrace();
      }
      return result;
  }

    public User getUserInfo(UserDto userDto, List<Error> errorList) throws IllegalAccessException {
        List<UserEntity> userEntityList_01 = accountServiceDaoImpl.getUserEntitys(null,userDto.getUserName(),null,null,errorList);
        if(CollectionUtils.isEmpty(userEntityList_01)){
            errorList.add(new Error(ErrorCode_Enum.SYS_02_000.getCode(),"请先注册账户",""));
            return null;
        }
        List<UserEntity> userEntityList_02 = accountServiceDaoImpl.getUserEntitys(null,userDto.getUserName(),userDto.getPassword(),null,errorList);
        if(CollectionUtils.isEmpty(userEntityList_02)){
            errorList.add(new Error(ErrorCode_Enum.IRIS_00_003.getCode(),"邮箱或密码错误",""));
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
              if(roleEntity.getId().equals("1")){
                  user.setSystemUser(true);
                  i ++;
              }
            if(roleEntity.getId().equals("2")){
                user.setEconomicUser(true);
                 i ++;
            }
            if(i == 2){
                break;
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
