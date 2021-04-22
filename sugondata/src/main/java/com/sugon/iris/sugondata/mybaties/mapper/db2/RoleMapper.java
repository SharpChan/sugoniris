package com.sugon.iris.sugondata.mybaties.mapper.db2;


import com.sugon.iris.sugondomain.entities.mybatiesEntity.db2.RoleEntity;
import java.util.List;

public interface RoleMapper {

    List<RoleEntity> selectRoleList();

    void saveRole(RoleEntity role);
}