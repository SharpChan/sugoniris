package com.sugon.iris.sugondomain.entities.menuEntities;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.sugon.iris.sugondomain.beans.menuBeans.Menu;
import com.sugon.iris.sugondomain.enums.MenuType_Enum;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Data
public class MenuEntity implements Serializable {

    /**
     * 菜单编码
     */
    private Long id;

    /**
     * 父菜单编码
     */
    private Long fatherId;
    /**
     * 鼠标移动到上方的说明
     */
    private String text;

    /**
     * 是否为菜单头是字符串
     */
    private Boolean heading;

    /**
     * 转译
     */
    private String translate;

    /**
     * 跳转链接
     */
    private String sref;

    /**
     * 图标
     */
    private String icon;

    /**
     * 警示图标
     */
    private String alert;

    /**
     * 警示图标 风格
     */
    private String label;

    /**
     * 菜单类型
     */
    private MenuType_Enum menuType;
    /**
     * 子菜单
     */
    private List<Menu> submenu;

    @JsonFormat(shape= JsonFormat.Shape.STRING,pattern="yyyy-MM-dd HH:mm:ss.SSSSSS",timezone="GMT+8")
    private Date createTime = new Date();

    @JsonFormat(shape= JsonFormat.Shape.STRING,pattern="yyyy-MM-dd HH:mm:ss.SSSSSS",timezone="GMT+8")
    private Date updateTime;

    private Long createUserId;
}
