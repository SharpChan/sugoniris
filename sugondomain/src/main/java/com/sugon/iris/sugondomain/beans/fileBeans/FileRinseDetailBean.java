package com.sugon.iris.sugondomain.beans.fileBeans;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class FileRinseDetailBean{

    /**
     * 自增序列
     */
    @ApiModelProperty(value="自增序列")
    private Long id;

    /**
     * 清洗组id
     */
    @ApiModelProperty(value="清洗组id")
    private Long fileRinseGroupId;

    /**
     * 类型名称
     */
    @ApiModelProperty(value="类型名称")
    private String typeName;

    /**
     * 备注
     */
    @ApiModelProperty(value="备注")
    private String comment;

    /**
     * 创建者id
     */
    @ApiModelProperty(value="创建者id")
    private Long userId;

}