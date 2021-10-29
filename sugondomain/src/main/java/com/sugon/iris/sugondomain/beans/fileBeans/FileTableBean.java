package com.sugon.iris.sugondomain.beans.fileBeans;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class FileTableBean {

    /**
     * 自增id
     */
    @ApiModelProperty(value="自增id")
    private Long id;

    /**
     * 标题
     */
    @ApiModelProperty(value="标题")
    private String title;
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
     *0:同步未开启；1:有新的同步；2：同步结束
     */
    @ApiModelProperty(value="0:同步未开启；1:有新的同步；2：同步结束")
    private String neo4jInitFlag;

}
