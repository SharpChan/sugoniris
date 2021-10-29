package com.sugon.iris.sugondomain.beans.neo4jBeans;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class Neo4jNodeInfoBean {

    /**
     * 自增序列
     */
    @ApiModelProperty(value="自增序列")
    private Long id;

    /**
     * 表fileTable 序列
     */
    @ApiModelProperty(value="表fileTable 序列")
    private Long fileTableId;

    /**
     *已经初始化到neo4j数据库的行数
     */
    @ApiModelProperty(value="已经初始化到neo4j数据库的行数")
    private Long rownum;

    /**
     *标签用 . 号分割
     */
    @ApiModelProperty(value="标签用 . 号分割")
    private String label;

    /**
     * 创建人id
     */
    @ApiModelProperty(value="创建人id")
    private Long userId;
}
