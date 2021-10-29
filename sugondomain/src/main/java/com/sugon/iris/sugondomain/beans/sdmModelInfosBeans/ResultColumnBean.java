package com.sugon.iris.sugondomain.beans.sdmModelInfosBeans;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class ResultColumnBean {
    /**
     * id
     */
    @ApiModelProperty(value="自增序列")
    private Long id;
    /**
     * 列名
     */
    @ApiModelProperty(value="列名")
    private  String columnName;

    /**
     * 列注释
     */
    @ApiModelProperty(value="列注释")
    private  String columnComment;
}
