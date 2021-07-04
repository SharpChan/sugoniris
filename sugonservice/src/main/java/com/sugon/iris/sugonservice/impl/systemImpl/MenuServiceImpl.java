package com.sugon.iris.sugonservice.impl.systemImpl;

import com.sugon.iris.sugoncommon.publicUtils.PublicUtils;
import com.sugon.iris.sugondata.jdbcTemplate.intf.system.MenuServiceDaoIntf;
import com.sugon.iris.sugondomain.beans.baseBeans.Error;
import com.sugon.iris.sugondomain.dtos.systemDtos.MenuDto;
import com.sugon.iris.sugondomain.entities.jdbcTemplateEntity.systemEntities.MenuEntity;
import com.sugon.iris.sugonservice.service.systemService.MenuService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class MenuServiceImpl implements MenuService {
    @Resource
    private MenuServiceDaoIntf menuServiceDaoImpl;

    @Override
    public Integer saveMenu(MenuDto menuDto, List<Error> errorList) throws IllegalAccessException {
        int i=0;
        MenuEntity menuEntity = new MenuEntity();
        PublicUtils.trans(menuDto,menuEntity);
        i=menuServiceDaoImpl.insertMenu(menuEntity,errorList);
        return i;
    }
}
