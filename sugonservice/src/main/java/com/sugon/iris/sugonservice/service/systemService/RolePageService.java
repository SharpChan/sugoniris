package com.sugon.iris.sugonservice.service.systemService;

import com.sugon.iris.sugondomain.beans.baseBeans.Error;
import com.sugon.iris.sugondomain.dtos.systemDtos.MenuDto;
import com.sugon.iris.sugondomain.dtos.systemDtos.OwnerMenuDto;
import java.util.List;

public interface RolePageService {

      int[] saveRolePage(List<OwnerMenuDto> rolePageDtoList, List<Error> errorList) throws IllegalAccessException;

      int[] deleteRolePage(List<OwnerMenuDto> rolePageDtoList, List<Error> errorList);

      List<MenuDto> getPagesByRoleId(Long roleId,List<Error> errorList) throws IllegalAccessException;
}
