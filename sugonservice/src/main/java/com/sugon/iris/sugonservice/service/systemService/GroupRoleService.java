package com.sugon.iris.sugonservice.service.systemService;

import com.sugon.iris.sugondomain.beans.baseBeans.Error;
import com.sugon.iris.sugondomain.dtos.systemDtos.GroupRoleDto;
import com.sugon.iris.sugondomain.dtos.systemDtos.RoleDto;

import java.util.List;

public interface GroupRoleService {

    Integer saveGroupRole(GroupRoleDto groupRoleDto, List<Error> errorList) throws IllegalAccessException;

    Integer deleteGroupRole(GroupRoleDto groupRoleDto,List<Error> errorList) throws IllegalAccessException;

    List<RoleDto> getGroupRoleByGroupId(Long groupId, List<Error> errorList) throws IllegalAccessException;
}
