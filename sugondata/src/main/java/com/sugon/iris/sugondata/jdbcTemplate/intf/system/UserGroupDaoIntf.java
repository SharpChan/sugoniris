package com.sugon.iris.sugondata.jdbcTemplate.intf.system;

import com.sugon.iris.sugondomain.beans.baseBeans.Error;
import com.sugon.iris.sugondomain.beans.userBeans.User;
import com.sugon.iris.sugondomain.entities.jdbcTemplateEntity.userEntities.UserEntity;
import com.sugon.iris.sugondomain.entities.jdbcTemplateEntity.userEntities.UserGroupDetailEntity;
import com.sugon.iris.sugondomain.entities.jdbcTemplateEntity.userEntities.UserGroupEntity;

import java.util.List;

public interface UserGroupDaoIntf {

    List<UserGroupEntity> getUserGroupEntitys( List<Error> errorList);

    UserGroupEntity getUserGroupEntityById( Long id,List<Error> errorList);

    Integer deleteUserGroupEntity(Long id, List<Error> errorList);

    Integer updateUserGroupEntity(UserGroupEntity userGroupEntitySql, List<Error> errorList);

    Integer insertUserGroupEntity(UserGroupEntity userGroupEntitySql, List<Error> errorList);

    List<UserEntity> getUserEntityList(Long groupId, List<Error> errorList);

    List<UserEntity> getGroupUserEntityList (Long groupId, List<Error> errorList);

    Integer insertUserGroupDetailEntity(UserGroupDetailEntity userGroupDetailEntitySql, List<Error> errorList);

    Integer deleteUserGroupDetailEntity(UserGroupDetailEntity userGroupDetailEntitySql, List<Error> errorList);
}
