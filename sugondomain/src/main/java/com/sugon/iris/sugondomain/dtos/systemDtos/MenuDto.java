package com.sugon.iris.sugondomain.dtos.systemDtos;

import com.sugon.iris.sugondomain.beans.system.Menu;
import lombok.Data;

import java.util.List;

@Data
public class MenuDto extends Menu {

    /**
     * 子菜单
     */
    private List<MenuDto> submenu;
}
