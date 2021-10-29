package com.sugon.iris.sugondomain.beans.sdmModelInfosBeans;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
public class ResultTableBean {

    /**
     * 表id
     */
    @ApiModelProperty(value="表id")
    private Long tableID;

    /**
     * hadoop表名
     */
    @ApiModelProperty(value="hadoop表名")
    private String tableName;
    /**
     *表注释
     */
    @ApiModelProperty(value="表注释")
    private String note;

}
