package com.sugon.iris.sugonservice.service.userInfoService;




import com.sugon.iris.sugondomain.entities.mybatiesEntity.db2.RoleEntity;

import java.util.List;

public interface RoleService {

    List<RoleEntity> selectRoleList();

    void saveRole(RoleEntity role);

}