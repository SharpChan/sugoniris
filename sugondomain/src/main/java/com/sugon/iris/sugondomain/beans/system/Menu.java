package com.sugon.iris.sugondomain.beans.system;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.sugon.iris.sugondomain.enums.MenuType_Enum;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import java.io.Serializable;
import java.util.Date;


@Data
public class Menu implements Serializable {
    /**
     * 菜单编码
     */
    @ApiModelProperty(value="菜单编码")
    private Long id;

    /**
     * 名称
     */
    @ApiModelProperty(value="名称")
    private String name;

    /**
     * 父菜单编码
     */
    @ApiModelProperty(value="父菜单编码")
    private Long fatherId;
    /**
     * 鼠标移动到上方的说明
     */
    @ApiModelProperty(value="鼠标移动到上方的说明")
    private String text;

    /**
     * 是否为菜单头
     */
    @ApiModelProperty(value="是否为菜单头")
    private Boolean heading;

    /**
     * 转译
     */
    @ApiModelProperty(value="转译")
    private String translate;

    /**
     * 跳转链接（路由）
     */
    @ApiModelProperty(value="跳转链接（路由）")
    private String sref;

    /**
     * 图标
     */
    @ApiModelProperty(value="图标")
    private String icon;

    /**
     * 警示图标
     */
    @ApiModelProperty(value="警示图标")
    private String alert;

    /**
     * 警示图标 风格
     */
    @ApiModelProperty(value="警示图标 风格")
    private String label;

    /**
     * 菜单类型
     */
    @ApiModelProperty(value="菜单类型")
    private MenuType_Enum menuType;

    @ApiModelProperty(value="创建时间")
    @JsonFormat(shape= JsonFormat.Shape.STRING,pattern="yyyy-MM-dd HH:mm:ss.SSSSSS",timezone="GMT+8")
    private Date createTime = new Date();

    @ApiModelProperty(value="修改时间")
    @JsonFormat(shape= JsonFormat.Shape.STRING,pattern="yyyy-MM-dd HH:mm:ss.SSSSSS",timezone="GMT+8")
    private Date updateTime = new Date();

    /**
     * 创建者id
     */
    @ApiModelProperty(value="创建者id")
    private Long userId;

    /**
     * 菜单层级1-4级
     */
    @ApiModelProperty(value="菜单层级1-4级")
    private Integer tier;

    /**
     * 排序编号
     */
    @ApiModelProperty(value="排序编号")
    private Integer sort;
}
