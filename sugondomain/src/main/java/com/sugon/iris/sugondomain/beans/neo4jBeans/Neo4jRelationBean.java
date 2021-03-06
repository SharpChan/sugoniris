package com.sugon.iris.sugondomain.beans.neo4jBeans;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class Neo4jRelationBean {
    /**
     * 自增序列
     */
    @ApiModelProperty(value="自增序列")
    private Long id;

    /**
     * 关系
     */
    @ApiModelProperty(value="关系")
    private String relationship;

    /**
     * 源节点样式id（neo4j_node_attribute的id）
     */
    @ApiModelProperty(value="源节点样式id（neo4j_node_attribute的id）")
    private Long sourceAttributeId;

    /**
     * 目标样式id（neo4j_node_attribute的id）
     */
    @ApiModelProperty(value="目标样式id（neo4j_node_attribute的id）")
    private Long targetAttributeId;

    /**
     * 箭头颜色
     */
    @ApiModelProperty(value="箭头颜色")
    private String color;

    /**
     * 箭头形状
     */
    @ApiModelProperty(value="箭头形状")
    private String shape;

    /**
     * 自定义程序URL地址
     */
    @ApiModelProperty(value="自定义程序URL地址")
     private String program;

    /**
     *创建者id
     */
    @ApiModelProperty(value="创建者id")
     private Long userId;

    /**
     *同名区分字段，用于关系的属性
     */
    @ApiModelProperty(value="同名区分字段，用于关系的属性")
    private String differentiate;
}