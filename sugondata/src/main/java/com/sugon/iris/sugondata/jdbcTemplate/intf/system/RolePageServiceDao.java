package com.sugon.iris.sugondata.jdbcTemplate.intf.system;

import com.sugon.iris.sugondomain.beans.baseBeans.Error;
import com.sugon.iris.sugondomain.entities.jdbcTemplateEntity.systemEntities.OwnerMenuEntity;
import java.util.List;

public interface RolePageServiceDao {

    int[] saveRolePages(List<OwnerMenuEntity> rolePageEntityList, List<Error> errorList);

    int[] deleteRolePages(List<Object[]> idList,List<Error> errorList);

    List<OwnerMenuEntity>  getRolePageByRoleId(Long roleId, List<Error> errorList);

}
