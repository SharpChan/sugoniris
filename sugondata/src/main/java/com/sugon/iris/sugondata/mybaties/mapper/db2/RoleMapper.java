package com.sugon.iris.sugondata.mybaties.mapper.db2;


import com.sugon.iris.sugondomain.entities.db2.Role;

import java.util.List;

public interface RoleMapper {

    List<Role> selectRoleList();

    void saveRole(Role role);
}