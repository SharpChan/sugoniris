package com.sugon.iris.sugonservice.impl.systemImpl;

import com.sugon.iris.sugoncommon.publicUtils.PublicUtils;
import com.sugon.iris.sugondata.jdbcTemplate.intf.system.RoleServiceDaoIntf;
import com.sugon.iris.sugondomain.beans.baseBeans.Error;
import com.sugon.iris.sugondomain.dtos.systemDtos.RoleDto;
import com.sugon.iris.sugondomain.entities.jdbcTemplateEntity.systemEntities.RoleEntity;
import com.sugon.iris.sugonservice.service.systemService.UserRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.ArrayList;
import java.util.List;

public class UserRoleServiceImpl implements UserRoleService {

    @Autowired
    private RoleServiceDaoIntf roleServiceDaoImpl;

    @Override
    public List<RoleDto> getAllRoles(List<Error> errorList) throws IllegalAccessException {
        List<RoleDto> roleDtoList = new ArrayList<>();
        List<RoleEntity> roleEntityList = roleServiceDaoImpl.getRoles(null,errorList);
        for(RoleEntity roleEntity : roleEntityList){
            RoleDto roleDtoBean = new RoleDto();
            PublicUtils.trans(roleEntity,roleDtoBean);
            roleDtoList.add(roleDtoBean);
        }
        return roleDtoList;
    }

    @Override
    public Integer saveRole(RoleDto roleDto, List<Error> errorList) throws IllegalAccessException {

        RoleEntity roleEntity = new RoleEntity();
        PublicUtils.trans(roleDto,roleEntity);
        return roleServiceDaoImpl.saveRole(roleEntity,errorList);
    }

    @Override
    public Integer modifyRole(RoleDto roleDto, List<Error> errorList) throws IllegalAccessException {
        RoleEntity roleEntity = new RoleEntity();
        PublicUtils.trans(roleDto,roleEntity);
        return roleServiceDaoImpl.updateRole(roleEntity,errorList);
    }

    @Override
    public Integer deleteRole(Long id, List<Error> errorList) {
        return roleServiceDaoImpl.deleteRole(id,errorList);
    }
}
