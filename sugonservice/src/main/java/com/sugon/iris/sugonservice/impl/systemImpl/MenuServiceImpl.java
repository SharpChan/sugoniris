package com.sugon.iris.sugonservice.impl.systemImpl;

import com.sugon.iris.sugoncommon.publicUtils.PublicUtils;
import com.sugon.iris.sugondata.jdbcTemplate.intf.system.MenuServiceDao;
import com.sugon.iris.sugondomain.beans.baseBeans.Error;
import com.sugon.iris.sugondomain.dtos.systemDtos.MenuDto;
import com.sugon.iris.sugondomain.entities.jdbcTemplateEntity.systemEntities.MenuEntity;
import com.sugon.iris.sugondomain.enums.ErrorCode_Enum;
import com.sugon.iris.sugonservice.service.systemService.MenuService;
import com.sugon.iris.sugonservice.service.systemService.TranslateService;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import javax.annotation.Resource;
import java.util.*;

@Service
public class MenuServiceImpl implements MenuService {
    @Resource
    private MenuServiceDao menuServiceDaoImpl;

    @Resource
    private TranslateService translateServiceImpl;

    @Override
    public Integer saveMenu(MenuDto menuDto, List<Error> errorList) throws IllegalAccessException {
        if(null == menuDto.getFatherId()){
            errorList.add(new Error(ErrorCode_Enum.SUGON_01_005.getCode(),"请先选择节点"));
        }
        int i=0;
        MenuEntity menuEntity = new MenuEntity();
        PublicUtils.trans(menuDto,menuEntity);
        i=menuServiceDaoImpl.insertMenu(menuEntity,errorList);
        translateServiceImpl.addTranslate(menuDto.getTranslate(),menuEntity.getId(),"zh_cn",menuDto.getName(),errorList);
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
                int diff = bean2.getSort() - bean1.getSort();
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

    @Override
    public List<MenuDto> getSiderBarMenu(Long userId,List<Error> errorList) throws IllegalAccessException {
        List<MenuDto> menuDtoList= new ArrayList();
        List<MenuEntity> menuEntityList = menuServiceDaoImpl.getMenuInfosByUserId(userId,errorList);
        if(CollectionUtils.isEmpty(menuEntityList)){
            return null;
        }

        List<Map<MenuDto,List<MenuDto>>>  menuDtoTmpList = new ArrayList<>();

        for(MenuEntity menuEntityBean : menuEntityList){
            MenuDto menuDto = new MenuDto();
            PublicUtils.trans(menuEntityBean,menuDto);
            menuDtoList.add(menuDto);
        }

        for(Iterator<MenuDto> it = menuDtoList.iterator();it.hasNext();){
            MenuDto menuDto = it.next();
            if(menuDto.getHeading()){
                Map<MenuDto,List<MenuDto>> map = new HashMap<>();
                List<MenuDto> menuDtolist = new ArrayList<>();
                menuDtoTmpList.add(map);
                for( MenuDto menuDto2 : menuDtoList){
                    if(null != menuDto2.getFatherId() && menuDto2.getFatherId().equals(menuDto.getId())){
                        menuDtolist.add(menuDto2);
                    }
                }
                map.put(menuDto,menuDtolist);
                it.remove();
            }
        }

        //组装父子节点
        for(MenuDto menuDto1 : menuDtoList){
            for(MenuDto menuDto2 : menuDtoList){
              if( null != menuDto1.getId() &&  menuDto1.getId().equals(menuDto2.getFatherId())){
                  menuDto1.getSubmenu().add(menuDto2);
              }
            }
        }
        //对所有的submenu排序，倒序
        for(MenuDto menuDto1 : menuDtoList){
            menuSort(menuDto1.getSubmenu());
        }

        //通过map排序，倒序
        Collections.sort(menuDtoTmpList, new Comparator<Map<MenuDto,List<MenuDto>>>() {
            @Override
            public int compare(Map<MenuDto,List<MenuDto>> bean1, Map<MenuDto,List<MenuDto>> bean2) {
                int a = 0;
                int b = 0;
                for (Map.Entry<MenuDto,List<MenuDto>> entry : bean1.entrySet()) {
                    a = entry.getKey().getSort();
                }
                for (Map.Entry<MenuDto,List<MenuDto>> entry : bean2.entrySet()) {
                    b = entry.getKey().getSort();
                }

                int diff = a - b;
                if (diff > 0) {
                    return 1;
                }else if (diff < 0) {
                    return -1;
                }
                return 0; //相等为0
            }
        });

        List<MenuDto> result = new ArrayList<>();
        //把map平铺
        for(Map<MenuDto,List<MenuDto>> map : menuDtoTmpList){
            for (Map.Entry<MenuDto,List<MenuDto>> entry : map.entrySet()) {
                result.add(entry.getKey());
                if(!CollectionUtils.isEmpty(entry.getValue())) {
                    result.addAll(entry.getValue());
                }
            }
        }


        return result;
    }

    @Override
    public List<MenuDto> getAllSiderBarMenu(List<Error> errorList) throws IllegalAccessException {
        List<MenuDto> menuDtoList= new ArrayList();
        List<MenuEntity> menuEntityList = menuServiceDaoImpl.getMenuInfos(null,errorList);
        if(CollectionUtils.isEmpty(menuEntityList)){
            return null;
        }

        List<Map<MenuDto,List<MenuDto>>>  menuDtoTmpList = new ArrayList<>();

        for(MenuEntity menuEntityBean : menuEntityList){
            MenuDto menuDto = new MenuDto();
            PublicUtils.trans(menuEntityBean,menuDto);
            menuDtoList.add(menuDto);
        }

        for(Iterator<MenuDto> it = menuDtoList.iterator();it.hasNext();){
            MenuDto menuDto = it.next();
            if(menuDto.getHeading()){
                Map<MenuDto,List<MenuDto>> map = new HashMap<>();
                List<MenuDto> menuDtolist = new ArrayList<>();
                menuDtoTmpList.add(map);
                for( MenuDto menuDto2 : menuDtoList){
                    if(null != menuDto2.getFatherId() && menuDto2.getFatherId().equals(menuDto.getId())){
                        menuDtolist.add(menuDto2);
                    }
                }
                map.put(menuDto,menuDtolist);
                it.remove();
            }
        }

        //组装父子节点
        for(MenuDto menuDto1 : menuDtoList){
            for(MenuDto menuDto2 : menuDtoList){
                if( null != menuDto1.getId() &&  menuDto1.getId().equals(menuDto2.getFatherId())){
                    menuDto1.getSubmenu().add(menuDto2);
                }
            }
        }
        //对所有的submenu排序，倒序
        for(MenuDto menuDto1 : menuDtoList){
            menuSort(menuDto1.getSubmenu());
        }

        //通过map排序，倒序
        Collections.sort(menuDtoTmpList, new Comparator<Map<MenuDto,List<MenuDto>>>() {
            @Override
            public int compare(Map<MenuDto,List<MenuDto>> bean1, Map<MenuDto,List<MenuDto>> bean2) {
                int a = 0;
                int b = 0;
                for (Map.Entry<MenuDto,List<MenuDto>> entry : bean1.entrySet()) {
                    a = entry.getKey().getSort();
                }
                for (Map.Entry<MenuDto,List<MenuDto>> entry : bean2.entrySet()) {
                    b = entry.getKey().getSort();
                }

                int diff = a - b;
                if (diff > 0) {
                    return 1;
                }else if (diff < 0) {
                    return -1;
                }
                return 0; //相等为0
            }
        });

        List<MenuDto> result = new ArrayList<>();
        //把map平铺
        for(Map<MenuDto,List<MenuDto>> map : menuDtoTmpList){
            for (Map.Entry<MenuDto,List<MenuDto>> entry : map.entrySet()) {
                result.add(entry.getKey());
                if(!CollectionUtils.isEmpty(entry.getValue())) {
                    result.addAll(entry.getValue());
                }
            }
        }


        return result;
    }

    @Override
    public MenuDto getNodeInfo(Long id, List<Error> errorList) throws IllegalAccessException {
        if(null == id){
            return null;
        }
        MenuEntity menuEntity =  menuServiceDaoImpl.getNode(id,errorList);
        MenuDto menuDto = new MenuDto();
        PublicUtils.trans(menuEntity,menuDto);
        return menuDto;
    }

    @Override
    public Integer modifyMenu(MenuDto menuDto, List<Error> errorList) throws IllegalAccessException {
        MenuEntity menuEntity = new MenuEntity();
        PublicUtils.trans(menuDto,menuEntity);
        //获取之前的翻译看有没有发生改变，没发生改变不修改
        if(!menuDto.getTranslate().equals(menuServiceDaoImpl.getMenuInfos(menuEntity,errorList).get(0).getTranslate())){
            translateServiceImpl.updateTranslate(menuDto.getTranslate(),menuDto.getId(),"zh_cn",menuDto.getName(),errorList);
        }
        //查看名称是否发生改变
        MenuEntity menuEntityOld = menuServiceDaoImpl.getMenuInfos(menuEntity,errorList).get(0);
        if(!menuEntityOld.getName().equals(menuEntity)){
            //修改翻译表的中文翻译
            translateServiceImpl.updateTranslate(menuDto.getTranslate(),menuDto.getId(),"zh_cn",menuDto.getName(),errorList);
        }
        return menuServiceDaoImpl.updateMenu(menuEntity,errorList);
    }

    @Override
    public Integer deleteMenu(Long id, List<Error> errorList) throws IllegalAccessException {
        translateServiceImpl.deleteTranslate(id,"zh_cn",errorList);
        return menuServiceDaoImpl.deleteMenu(id,errorList);
    }

    private void menuSort(List<MenuDto> menuDtolist) {
        if (!CollectionUtils.isEmpty(menuDtolist)) {
            Collections.sort(menuDtolist, new Comparator<MenuDto>() {
                @Override
                public int compare(MenuDto bean1, MenuDto bean2) {
                    int diff = bean1.getSort() - bean2.getSort();
                    if (diff > 0) {
                        return 1;
                    } else if (diff < 0) {
                        return -1;
                    }
                    return 0; //相等为0
                }
            });
        }
    }
}
