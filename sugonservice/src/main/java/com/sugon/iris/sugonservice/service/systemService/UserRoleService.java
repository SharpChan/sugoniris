package com.sugon.iris.sugonservice.service.systemService;

import com.sugon.iris.sugondomain.beans.baseBeans.Error;
import com.sugon.iris.sugondomain.dtos.systemDtos.RoleDto;
import java.util.List;

public interface UserRoleService {

     List<RoleDto> getAllRoles(List<Error> errorList ) throws IllegalAccessException;

     Integer saveRole(RoleDto roleDto,List<Error> errorList) throws IllegalAccessException;

     Integer  modifyRole(RoleDto roleDto,List<Error> errorList) throws IllegalAccessException;

     Integer deleteRole(Long id ,List<Error> errorList);

}
