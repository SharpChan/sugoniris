package com.sugon.iris.sugondomain.beans.regular;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class RegularGroupBean {
    /**
     * 自增序列
     */
    @ApiModelProperty(value="自增序列")
    private Long id;

    /**
     *正则表达式名称
     */
    @ApiModelProperty(value="正则表达式名称")
    private String groupName;

    /**
     *备注
     */
    @ApiModelProperty(value="备注")
    private String comment;

    /**
     *排序
     */
    @ApiModelProperty(value="排序")
    private Integer sort;

    /**
     *用户id
     */
    @ApiModelProperty(value="用户id")
    private Long userId;
}