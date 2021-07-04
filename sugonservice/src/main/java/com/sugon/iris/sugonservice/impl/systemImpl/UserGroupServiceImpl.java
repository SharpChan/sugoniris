package com.sugon.iris.sugonservice.impl.systemImpl;

import com.sugon.iris.sugoncommon.publicUtils.PublicUtils;
import com.sugon.iris.sugondata.jdbcTemplate.intf.system.UserGroupDaoIntf;
import com.sugon.iris.sugondomain.beans.baseBeans.Error;
import com.sugon.iris.sugondomain.beans.system.User;
import com.sugon.iris.sugondomain.dtos.systemDtos.UserDto;
import com.sugon.iris.sugondomain.dtos.systemDtos.UserGroupDetailDto;
import com.sugon.iris.sugondomain.dtos.systemDtos.UserGroupDto;
import com.sugon.iris.sugondomain.entities.jdbcTemplateEntity.systemEntities.UserEntity;
import com.sugon.iris.sugondomain.entities.jdbcTemplateEntity.systemEntities.UserGroupDetailEntity;
import com.sugon.iris.sugondomain.entities.jdbcTemplateEntity.systemEntities.UserGroupEntity;
import com.sugon.iris.sugonservice.service.systemService.UserGroupService;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Service
public class UserGroupServiceImpl implements UserGroupService {

    @Resource
    private UserGroupDaoIntf userGroupDaoImpl;

    @Override
    public List<UserGroupDto> userGroupList(List<Error> errorList) throws IllegalAccessException {
        List<UserGroupDto> userGroupDtoList = new ArrayList<>();
        List<UserGroupEntity> userGroupEntityList = userGroupDaoImpl.getUserGroupEntitys(errorList);
        for(UserGroupEntity userGroupEntity : userGroupEntityList){
            UserGroupDto userGroupDto = new UserGroupDto();
            PublicUtils.trans(userGroupEntity,userGroupDto);
            userGroupDtoList.add(userGroupDto);
        }
        return userGroupDtoList;
    }

    @Override
    public UserGroupDto userGroupById(Long id, List<Error> errorList) throws IllegalAccessException {
        UserGroupEntity userGroupEntity = userGroupDaoImpl.getUserGroupEntityById(id,errorList);
        UserGroupDto userGroupDto = new UserGroupDto();
        PublicUtils.trans(userGroupEntity,userGroupDto);
        return userGroupDto;
    }

    @Override
    public Integer addUserGroup(UserGroupDto userGroupDto, List<Error> errorList) throws IllegalAccessException {
        UserGroupEntity userGroupEntity = new UserGroupEntity();
        PublicUtils.trans(userGroupDto,userGroupEntity);
        return userGroupDaoImpl.insertUserGroupEntity(userGroupEntity,errorList);
    }

    @Override
    public Integer modifyUserGroup(UserGroupDto userGroupDto, List<Error> errorList) throws IllegalAccessException {
        UserGroupEntity userGroupEntity = new UserGroupEntity();
        PublicUtils.trans(userGroupDto,userGroupEntity);
        return userGroupDaoImpl.updateUserGroupEntity(userGroupEntity,errorList);
    }

    @Override
    public Integer removeUserGroup(Long id, List<Error> errorList) {
        Integer i = 0;
        i += userGroupDaoImpl.deleteUserGroupEntity(id,errorList);
        UserGroupDetailEntity userGroupDetailEntitySql = new UserGroupDetailEntity();
        userGroupDetailEntitySql.setUserGroupId(id);
        i += userGroupDaoImpl.deleteUserGroupDetailEntity(userGroupDetailEntitySql,errorList);
        return i;
    }

    @Override
    public List<UserDto> getUsers(Long groupId, List<Error> errorList) throws IllegalAccessException {
        List<UserDto> userDtoList = new ArrayList<>();
        List<UserEntity> userEntityList = userGroupDaoImpl.getUserEntityList(groupId,errorList);
        for(UserEntity userEntity : userEntityList){
            UserDto userDto = new UserDto();
            PublicUtils.trans(userEntity ,userDto);
            userDtoList.add(userDto);
        }
        return userDtoList;
    }

    @Override
    public List<UserDto> getGroupUsers(Long groupId, List<Error> errorList) throws IllegalAccessException {

        List<UserDto> userDtoList = new ArrayList<>();
        List<UserEntity> userEntityList = userGroupDaoImpl.getGroupUserEntityList(groupId,errorList);
        for(UserEntity userEntity : userEntityList){
            UserDto userDto = new UserDto();
            PublicUtils.trans(userEntity ,userDto);
            userDtoList.add(userDto);
        }
        return userDtoList;
    }

    @Override
    public Integer addUserToUserGroup(UserGroupDetailDto userGroupDetailDto, List<Error> errorList) throws IllegalAccessException {
        UserGroupDetailEntity userGroupDetailEntitySql = new UserGroupDetailEntity();
        PublicUtils.trans(userGroupDetailDto,userGroupDetailEntitySql);
        return userGroupDaoImpl.insertUserGroupDetailEntity(userGroupDetailEntitySql,errorList);
    }

    @Override
    public Integer addUserToUserGroupBatch(User user,List<UserGroupDetailDto> userGroupDetailDtoList, List<Error> errorList) throws IllegalAccessException {
        Integer i = 0;
        if(!CollectionUtils.isEmpty(userGroupDetailDtoList)){
            for(UserGroupDetailDto userGroupDetailDto : userGroupDetailDtoList){
                UserGroupDetailEntity userGroupDetailEntitySql = new UserGroupDetailEntity();
                PublicUtils.trans(userGroupDetailDto,userGroupDetailEntitySql);
                userGroupDetailEntitySql.setCreateUserId(user.getId());
                userGroupDaoImpl.insertUserGroupDetailEntity(userGroupDetailEntitySql,errorList);
                i++;
            }
        }
        return i;
    }

    @Override
    public Integer removeUserFromUserGroup(UserGroupDetailDto userGroupDetailDto, List<Error> errorList) throws IllegalAccessException {
        UserGroupDetailEntity userGroupDetailEntitySql = new UserGroupDetailEntity();
        PublicUtils.trans(userGroupDetailDto,userGroupDetailEntitySql);
        return userGroupDaoImpl.deleteUserGroupDetailEntity(userGroupDetailEntitySql,errorList);
    }

    @Override
    public Integer removeUserFromUserGroupBatch(List<UserGroupDetailDto> userGroupDetailDtoList, List<Error> errorList) throws IllegalAccessException {
      Integer i = 0;
        if(!CollectionUtils.isEmpty(userGroupDetailDtoList)){
          for(UserGroupDetailDto userGroupDetailDto : userGroupDetailDtoList){
              UserGroupDetailEntity userGroupDetailEntitySql = new UserGroupDetailEntity();
              PublicUtils.trans(userGroupDetailDto,userGroupDetailEntitySql);
              userGroupDaoImpl.deleteUserGroupDetailEntity(userGroupDetailEntitySql,errorList);
              i ++;
          }
        }

        return i;
    }
}
