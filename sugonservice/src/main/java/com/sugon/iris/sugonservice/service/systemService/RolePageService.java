package com.sugon.iris.sugonservice.service.systemService;

import com.sugon.iris.sugondomain.beans.baseBeans.Error;
import com.sugon.iris.sugondomain.dtos.systemDtos.MenuDto;
import com.sugon.iris.sugondomain.dtos.systemDtos.RolePageDto;
import java.util.List;

public interface RolePageService {

      int[] saveRolePage(List<RolePageDto> rolePageDtoList,List<Error> errorList) throws IllegalAccessException;

      int[] deleteRolePage(List<RolePageDto> rolePageDtoList,List<Error> errorList);

      List<MenuDto> getPagesByRoleId(Long roleId,List<Error> errorList) throws IllegalAccessException;
}
