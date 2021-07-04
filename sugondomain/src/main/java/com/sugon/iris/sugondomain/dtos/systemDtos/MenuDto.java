package com.sugon.iris.sugondomain.dtos.systemDtos;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.sugon.iris.sugondomain.beans.system.Menu;
import lombok.Data;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

@Data
public class MenuDto extends Menu {

    /**
     * 子菜单
     */
    @JSONField(name="submenu")
    private List<MenuDto> submenu;


    public List<MenuDto> getSubmenu(){
        if(CollectionUtils.isEmpty(submenu)){
            submenu = new ArrayList<MenuDto>();
        }
      return submenu;
    }
}
