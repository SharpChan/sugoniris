package com.sugon.iris.sugondomain.beans.fileBeans;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class FileRinseRegularBean {
    /**
     * 自增序列
     */
    @ApiModelProperty(value="自增序列")
    private Long id;

    /**
     * 清洗字段id
     */
    @ApiModelProperty(value="清洗字段id")
    private Long fileRinseDetailId;

    /**
     * 正则表达式id
     */
    @ApiModelProperty(value="正则表达式id")
    private Long regularDetailId;

    /**
     * 创建者id
     */
    @ApiModelProperty(value="创建者id")
    private Long userId;

    /**
     * 1:需要满足；2:满足的话进行排除
     */
    @ApiModelProperty(value="1:需要满足；2:满足的话进行排除")
    private String type;

}