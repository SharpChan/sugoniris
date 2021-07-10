package com.sugon.iris.sugonservice.impl.systemImpl;

import com.sugon.iris.sugoncommon.publicUtils.PublicUtils;
import com.sugon.iris.sugondata.jdbcTemplate.intf.system.RolePageServiceDao;
import com.sugon.iris.sugondomain.beans.baseBeans.Error;
import com.sugon.iris.sugondomain.dtos.systemDtos.MenuDto;
import com.sugon.iris.sugondomain.dtos.systemDtos.RolePageDto;
import com.sugon.iris.sugondomain.entities.jdbcTemplateEntity.systemEntities.RolePageEntity;
import com.sugon.iris.sugonservice.service.systemService.MenuService;
import com.sugon.iris.sugonservice.service.systemService.RolePageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
@Service
public class RolePageServiceImpl implements RolePageService {

    @Autowired
    private RolePageServiceDao rolePageServiceDaoImpl;

    @Autowired
    private MenuService menuServiceImpl;

    @Override
    public int[] saveRolePage(List<RolePageDto> rolePageDtoList, List<Error> errorList) throws IllegalAccessException {
        if(CollectionUtils.isEmpty(rolePageDtoList)){
            return null;
        }
        List<RolePageEntity> rolePageEntityList = new ArrayList<>();
        for (RolePageDto rolePageDto : rolePageDtoList){
            RolePageEntity rolePageEntity = new RolePageEntity();
            PublicUtils.trans(rolePageDto,rolePageEntity);
            rolePageEntityList.add(rolePageEntity);
        }
        return rolePageServiceDaoImpl.saveRolePages(rolePageEntityList,errorList);
    }

    @Override
    public int[] deleteRolePage(List<Long> idList, List<Error> errorList) {
        List<Object[]> objArrList = new ArrayList<>();
        for(Long id : idList){
            Object[] objArr = new Object[1];
            objArr[0] = id;
            objArrList.add(objArr);
        }
        return rolePageServiceDaoImpl.deleteRolePages(objArrList,errorList);
    }

    @Override
    public List<MenuDto> getPagesByRoleId(Long roleId, List<Error> errorList) throws IllegalAccessException {
        //获取所有的页面
        List<MenuDto> menuDtoList =menuServiceImpl.getAllSiderBarMenu(errorList);
        //获取该角色配置的页面
        List<RolePageEntity> rolePageEntityList = rolePageServiceDaoImpl.getRolePageByRoleId(roleId,errorList);



        return null;
    }
}
