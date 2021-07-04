package com.sugon.iris.sugonservice.service.systemService;

import com.sugon.iris.sugondomain.beans.baseBeans.Error;
import com.sugon.iris.sugondomain.beans.baseBeans.RestResult;
import com.sugon.iris.sugondomain.dtos.systemDtos.MenuDto;
import java.util.List;
import java.util.Map;

public interface MenuService {

    Integer saveMenu(MenuDto menuDto, List<Error> errorList) throws IllegalAccessException;

    List<MenuDto>  getMenu(MenuDto menuDto, List<Error> errorList) throws IllegalAccessException;

    List<MenuDto> getSiderBarMenu(Long userId, List<Error> errorList) throws IllegalAccessException;
}
