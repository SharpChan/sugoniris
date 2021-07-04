package com.sugon.iris.sugonservice.impl.systemImpl;

import com.sugon.iris.sugoncommon.publicUtils.PublicUtils;
import com.sugon.iris.sugondata.jdbcTemplate.intf.system.MenuServiceDaoIntf;
import com.sugon.iris.sugondomain.beans.baseBeans.Error;
import com.sugon.iris.sugondomain.beans.baseBeans.RestResult;
import com.sugon.iris.sugondomain.dtos.systemDtos.MenuDto;
import com.sugon.iris.sugondomain.entities.jdbcTemplateEntity.systemEntities.MenuEntity;
import com.sugon.iris.sugondomain.entities.mybatiesEntity.db2.FileTemplateDetailEntity;
import com.sugon.iris.sugonservice.service.systemService.MenuService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
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

    @Override
    public List<MenuDto> getMenu(MenuDto menuDto, List<Error> errorList) throws IllegalAccessException {
        List<MenuDto> menuDtoList= new ArrayList();
        MenuEntity menuEntity = new MenuEntity();
        PublicUtils.trans(menuDto,menuEntity);
        List<MenuEntity> menuEntityList = menuServiceDaoImpl.getMenuInfos(menuEntity,errorList);
        for(MenuEntity menuEntityBean : menuEntityList){
            MenuDto menuDtoBean = new MenuDto();
            PublicUtils.trans(menuEntityBean,menuDtoBean);
            menuDtoList.add(menuDtoBean);
        }

        Collections.sort(menuDtoList, new Comparator<MenuDto>() {
            @Override
            public int compare(MenuDto bean1, MenuDto bean2) {
                int diff = bean1.getSort() - bean2.getSort();
                if (diff > 0) {
                    return 1;
                }else if (diff < 0) {
                    return -1;
                }
                return 0; //相等为0
            }
        });
        return menuDtoList;
    }
}
