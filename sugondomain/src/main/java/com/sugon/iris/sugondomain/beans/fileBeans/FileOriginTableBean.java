package com.sugon.iris.sugondomain.beans.fileBeans;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class FileOriginTableBean {

    /**
     * 自增id
     */
    @ApiModelProperty(value="自增id")
    private Long id;

    /**
     * 表名
     */
    @ApiModelProperty(value="表名")
    private String tableName;

    /**
     *模板id
     */
    @ApiModelProperty(value="模板id")
    private Long fileTemplateId;

    /**
     * 案件编号
     */
    @ApiModelProperty(value="案件编号")
    private Long caseId;

    /**
     * 创建人id
     */
    @ApiModelProperty(value="创建人id")
    private Long userId;

    /**
     * 对应的资源表id
     */
    @ApiModelProperty(value="对应的资源表id")
    private Long fileTableId;

}
