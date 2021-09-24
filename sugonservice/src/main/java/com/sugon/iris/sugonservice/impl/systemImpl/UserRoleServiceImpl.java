package com.sugon.iris.sugonservice.impl.systemImpl;

import com.sugon.iris.sugoncommon.publicUtils.PublicUtils;
import com.sugon.iris.sugondata.jdbcTemplate.intf.system.RoleServiceDao;
import com.sugon.iris.sugondomain.beans.baseBeans.Error;
import com.sugon.iris.sugondomain.dtos.systemDtos.RoleDto;
import com.sugon.iris.sugondomain.entities.jdbcTemplateEntity.systemEntities.RoleEntity;
import com.sugon.iris.sugonservice.service.systemService.UserRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;

@Service
public class UserRoleServiceImpl implements UserRoleService {

    @Autowired
    private RoleServiceDao roleServiceDaoImpl;

    @Override
    public List<RoleDto> getAllRoles(RoleDto roleDto,List<Error> errorList) throws IllegalAccessException {
        RoleEntity roleEntity = new RoleEntity();
        PublicUtils.trans(roleDto,roleEntity);
        List<RoleDto> roleDtoList = new ArrayList<>();
        List<RoleEntity> roleEntityList = roleServiceDaoImpl.getRoles(roleEntity,errorList);
        for(RoleEntity roleEntityBean : roleEntityList){
            RoleDto roleDtoBean = new RoleDto();
            PublicUtils.trans(roleEntityBean,roleDtoBean);
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

        //管理员角色和经侦角色无法删除
        if(id == 1 || id ==2){
           return 0;
        }
        return roleServiceDaoImpl.deleteRole(id,errorList);
    }
}
