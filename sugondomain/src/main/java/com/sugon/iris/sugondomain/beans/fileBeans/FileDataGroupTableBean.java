package com.sugon.iris.sugondomain.beans.fileBeans;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class FileDataGroupTableBean {

    /**
     * 自增序列
     */
    @ApiModelProperty(value="自增序列")
    private Long id;

    /**
     * 数据组id
     */
    @ApiModelProperty(value="数据组id")
    private Long dataGroupId;

    /**
     * 表编号
     */
    @ApiModelProperty(value="表编号")
    private Long tableId;

    /**
     *创建者id
     */
    @ApiModelProperty(value="创建者id")
    private Long createUserId;
}
