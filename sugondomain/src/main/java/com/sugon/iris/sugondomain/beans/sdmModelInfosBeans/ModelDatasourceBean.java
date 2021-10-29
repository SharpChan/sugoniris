package com.sugon.iris.sugondomain.beans.sdmModelInfosBeans;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class ModelDatasourceBean {

    /**
     * id唯一索引
     */
    @ApiModelProperty(value="id唯一索引")
    private Long id;

    /**
     * 表名
     */
    @ApiModelProperty(value="表名")
    private String tableName;

    /**
     * 注释
     */
    @ApiModelProperty(value="注释")
    private String name;
    /**
     *创建者（警号）
     */
    @ApiModelProperty(value="创建者（警号）")
    private String creator;
    /**
     * 模型的sql语句
     */
    @ApiModelProperty(value="模型的sql语句")
    private String modelSql;

    /**
     * 模型执行状态
     */
    @ApiModelProperty(value="模型执行状态")
    private String status;

    /**
     * 模型结果表编号
     */
    @ApiModelProperty(value="模型结果表编号")
    private String resultId;
}
