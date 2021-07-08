package com.sugon.iris.sugondata.jdbcTemplate.intf.system;

import com.sugon.iris.sugondomain.beans.baseBeans.Error;
import com.sugon.iris.sugondomain.entities.jdbcTemplateEntity.systemEntities.RoleEntity;
import java.util.List;

public interface RoleServiceDao {

    List<RoleEntity> getRoles(RoleEntity roleEntity, List<Error> errorList);

    Integer saveRole(RoleEntity roleEntity, List<Error> errorList);

    Integer updateRole(RoleEntity roleEntity, List<Error> errorList);

    Integer deleteRole(Long id, List<Error> errorList);
}
