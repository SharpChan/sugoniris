package com.sugon.iris.sugondata.jdbcTemplate.intf.system;

import com.sugon.iris.sugondomain.beans.baseBeans.Error;
import com.sugon.iris.sugondomain.entities.jdbcTemplateEntity.systemEntities.MenuEntity;
import com.sugon.iris.sugondomain.entities.jdbcTemplateEntity.systemEntities.RoleEntity;

import java.util.List;

public interface RoleServiceDaoIntf {

    List<RoleEntity> getMenuInfos(RoleEntity roleEntity, List<Error> errorList);

}
