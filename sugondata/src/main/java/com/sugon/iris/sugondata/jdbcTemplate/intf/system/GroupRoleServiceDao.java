package com.sugon.iris.sugondata.jdbcTemplate.intf.system;

import com.sugon.iris.sugondomain.beans.baseBeans.Error;
import com.sugon.iris.sugondomain.entities.jdbcTemplateEntity.systemEntities.GroupRoleEntity;
import java.util.List;

public interface GroupRoleServiceDao {

    Integer saveGroupRole(GroupRoleEntity groupRoleEntity, List<Error> errorList);

    Integer deleteGroupRole(GroupRoleEntity groupRoleEntity,List<Error> errorList);

    List<GroupRoleEntity>  getGroupRole(Long groupId,List<Error> errorList);

}
