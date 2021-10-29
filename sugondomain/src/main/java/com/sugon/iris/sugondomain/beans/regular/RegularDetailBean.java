package com.sugon.iris.sugondomain.beans.regular;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class RegularDetailBean {
    /**
     * 自增序列
     */
    @ApiModelProperty(value="自增序列")
    private Long id;

    /**
     * 正则表达式名称
     */
    @ApiModelProperty(value="正则表达式名称")
    private String regularName;

    /**
     * 格式
     */
    @ApiModelProperty(value="格式")
    private String format;

    /**
     * 正则表达式
     */
    @ApiModelProperty(value="正则表达式")
    private String regularValue;

    /**
     * 正则表达式组
     */
    @ApiModelProperty(value="正则表达式组")
    private Long regularGroupId;

    /**
     * 备注
     */
    @ApiModelProperty(value="备注")
    private String comment;

    /**
     * 排序字段
     */
    @ApiModelProperty(value="排序字段")
    private Integer sort;

    /**
     * 创建者id
     */
    @ApiModelProperty(value="创建者id")
    private Long userId;

}