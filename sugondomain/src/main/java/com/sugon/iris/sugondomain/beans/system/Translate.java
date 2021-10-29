package com.sugon.iris.sugondomain.beans.system;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class Translate {

    /**
     * 自增序列
     */
   @ApiModelProperty(value="自增序列")
   private Long id;

    /**
     *上级id（父级）
     */
    @ApiModelProperty(value="上级id（父级）")
    private Long fatherId;

    /**
     * 名称
     */
    @ApiModelProperty(value="名称")
    private String source;

    /**
     * 译文
     */
    @ApiModelProperty(value="译文")
    private String value;

    /**
     * 层级
     */
    @ApiModelProperty(value="层级")
    private String grade;

    /**
     * 翻译类型  1：中文
     */
    @ApiModelProperty(value="翻译类型  1：中文")
    private String tsType;

    /**
     * 注册页面的id
     */
    @ApiModelProperty(value="注册页面的id")
    private Long menuId;

}
