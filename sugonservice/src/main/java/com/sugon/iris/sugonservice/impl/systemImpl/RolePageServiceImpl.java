package com.sugon.iris.sugonservice.impl.systemImpl;

import com.sugon.iris.sugoncommon.publicUtils.PublicUtils;
import com.sugon.iris.sugondata.jdbcTemplate.intf.system.RolePageServiceDao;
import com.sugon.iris.sugondomain.beans.baseBeans.Error;
import com.sugon.iris.sugondomain.dtos.systemDtos.MenuDto;
import com.sugon.iris.sugondomain.dtos.systemDtos.OwnerMenuDto;
import com.sugon.iris.sugondomain.entities.jdbcTemplateEntity.systemEntities.OwnerMenuEntity;
import com.sugon.iris.sugonservice.service.systemService.MenuService;
import com.sugon.iris.sugonservice.service.systemService.RolePageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
@Service
public class RolePageServiceImpl implements RolePageService {

    @Autowired
    private RolePageServiceDao rolePageServiceDaoImpl;

    @Autowired
    private MenuService menuServiceImpl;

    @Override
    public int[] saveRolePage(List<OwnerMenuDto> rolePageDtoList, List<Error> errorList) throws IllegalAccessException {
        if(CollectionUtils.isEmpty(rolePageDtoList)){
            return null;
        }
        List<OwnerMenuEntity> rolePageEntityList = new ArrayList<>();
        for (OwnerMenuDto rolePageDto : rolePageDtoList){
            OwnerMenuEntity rolePageEntity = new OwnerMenuEntity();
            PublicUtils.trans(rolePageDto,rolePageEntity);
            rolePageEntityList.add(rolePageEntity);
        }
        return rolePageServiceDaoImpl.saveRolePages(rolePageEntityList,errorList);
    }

    @Override
    public int[] deleteRolePage(List<OwnerMenuDto> rolePageDtoList, List<Error> errorList) {
        List<Object[]> objArrList = new ArrayList<>();
        for(OwnerMenuDto rolePageDto : rolePageDtoList){
            Object[] objArr = new Object[2];
            objArr[0] = rolePageDto.getOwnerId();
            objArr[1] = rolePageDto.getMenuId();
            objArrList.add(objArr);
        }
        return rolePageServiceDaoImpl.deleteRolePages(objArrList,errorList);
    }

    @Override
    public List<MenuDto> getPagesByRoleId(Long roleId, List<Error> errorList) throws IllegalAccessException {
        //获取所有的页面
        List<MenuDto> menuDtoList =menuServiceImpl.getAllSiderBarMenu(errorList);
        if(CollectionUtils.isEmpty(menuDtoList)){
            return null;
        }
        //获取该角色配置的页面
        List<OwnerMenuEntity> rolePageEntityList = rolePageServiceDaoImpl.getRolePageByRoleId(roleId,errorList);

        //设置勾选标记
        setCheckFlag( menuDtoList , rolePageEntityList);

        List<MenuDto> menuDtoList2 = new ArrayList<>();
        menuDtoList2.addAll(menuDtoList);
        for(Iterator<MenuDto> it = menuDtoList.iterator(); it.hasNext();){
            MenuDto menuDto1 = it.next();
            for(MenuDto menuDto2 : menuDtoList2){
                if(menuDto2.getId().equals(menuDto1.getFatherId())){
                    menuDto2.getSubmenu().add(menuDto1);
                    it.remove();
                    continue;
                }
            }
        }
        return menuDtoList;
    }

    private void setCheckFlag(List<MenuDto> menuDtoList ,List<OwnerMenuEntity> rolePageEntityList){
        for(MenuDto menuDto : menuDtoList) {
            for (OwnerMenuEntity rolePageEntity : rolePageEntityList) {
                if (menuDto.getId().equals(rolePageEntity.getMenuId())) {
                    menuDto.setIsChecked(true);
                    break;
                }
            }
            if(!CollectionUtils.isEmpty(menuDto.getSubmenu())){
                setCheckFlag(menuDto.getSubmenu(),rolePageEntityList);
            }
        }
    }
}


