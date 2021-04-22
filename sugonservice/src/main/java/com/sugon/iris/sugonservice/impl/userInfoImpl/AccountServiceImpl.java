package com.sugon.iris.sugonservice.impl.userInfoImpl;

import com.sugon.iris.sugoncommon.publicUtils.PublicUtils;
import com.sugon.iris.sugondata.jdbcTemplate.intf.system.AccountServiceDaoIntf;
import com.sugon.iris.sugondomain.dtos.userDtos.UserDto;
import com.sugon.iris.sugondomain.entities.jdbcTemplateEntity.userEntities.UserEntity;
import com.sugon.iris.sugonservice.service.userInfoService.AccountService;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import com.sugon.iris.sugondomain.beans.baseBeans.Error;
import java.util.List;
import com.sugon.iris.sugondomain.beans.userBeans.User;

import javax.annotation.Resource;

@Service
public class AccountServiceImpl implements AccountService {

    @Resource
    private AccountServiceDaoIntf accountServiceDaoImpl;




  public int saveAccount(UserDto userDto, List<Error> errorList){
      int result = 0;
      try {
          UserEntity user = new UserEntity();
          PublicUtils.trans(userDto, user);
          List<UserEntity> userEntityList = accountServiceDaoImpl.getUserEntitys(null,userDto.getEmail(),null,null,errorList);
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

        List<UserEntity> userEntityList_01 = accountServiceDaoImpl.getUserEntitys(null,userDto.getEmail(),null,null,errorList);
        if(CollectionUtils.isEmpty(userEntityList_01)){
            errorList.add(new Error("iris-00-002","请先注册账户",""));
            return null;
        }
        List<UserEntity> userEntityList_02 = accountServiceDaoImpl.getUserEntitys(null,userDto.getEmail(),userDto.getPassword(),null,errorList);
        if(CollectionUtils.isEmpty(userEntityList_02)){
            errorList.add(new Error("iris-00-003","邮箱或密码错误",""));
            return null;
        }else if(userEntityList_02.size()>1){
            errorList.add(new Error("iris-00-004","存在多个账户请联系管理员",""));
            return null;
        }
        User user = new User();
        PublicUtils.trans(userEntityList_02.get(0), user);
        return user;
    }
    public int updateUser(UserDto userDto, List<Error> errorList) throws IllegalAccessException {
        UserEntity user = new UserEntity();
        PublicUtils.trans(userDto, user);
        return accountServiceDaoImpl.update(user,errorList);
  }
}
