package com.sugon.iris.sugonservice.impl.systemImpl;

import com.sugon.iris.sugoncommon.publicUtils.PublicUtils;
import com.sugon.iris.sugondata.jdbcTemplate.intf.system.GroupRoleServiceDao;
import com.sugon.iris.sugondomain.beans.baseBeans.Error;
import com.sugon.iris.sugondomain.dtos.systemDtos.GroupRoleDto;
import com.sugon.iris.sugondomain.dtos.systemDtos.RoleDto;
import com.sugon.iris.sugondomain.entities.jdbcTemplateEntity.systemEntities.GroupRoleEntity;
import com.sugon.iris.sugonservice.service.systemService.GroupRoleService;
import com.sugon.iris.sugonservice.service.systemService.UserRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GroupRoleServiceImpl implements GroupRoleService {
    @Autowired
    private UserRoleService userRoleServiceImpl;

    @Autowired
    private GroupRoleServiceDao groupRoleServiceDaoImpl;

    @Override
    public Integer saveGroupRole(GroupRoleDto groupRoleDto, List<Error> errorList) throws IllegalAccessException {
        GroupRoleEntity groupRoleEntity = new GroupRoleEntity();
        PublicUtils.trans(groupRoleDto,groupRoleEntity);
        return groupRoleServiceDaoImpl.saveGroupRole(groupRoleEntity,errorList);
    }

    @Override
    public Integer deleteGroupRole(GroupRoleDto groupRoleDto, List<Error> errorList) throws IllegalAccessException {
        GroupRoleEntity groupRoleEntity = new GroupRoleEntity();
        PublicUtils.trans(groupRoleDto,groupRoleEntity);
        return groupRoleServiceDaoImpl.deleteGroupRole(groupRoleEntity,errorList);
    }

    @Override
    public List<RoleDto> getGroupRoleByGroupId(Long groupId, List<Error> errorList) throws IllegalAccessException {

        //获取所有的角色
        List<RoleDto> roleDtoList =userRoleServiceImpl.getAllRoles(null,errorList);
        List<GroupRoleEntity> groupRoleEntityList = groupRoleServiceDaoImpl.getGroupRole(groupId,errorList);
        for(RoleDto roleDto : roleDtoList){
            for(GroupRoleEntity groupRoleEntity : groupRoleEntityList){
                   if(roleDto.getId().equals(groupRoleEntity.getRoleId())){
                       roleDto.setChecked(true);
                       break;
                   }
            }
        }
        return roleDtoList;
    }
}
