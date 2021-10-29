package com.sugon.iris.sugondomain.beans.neo4jBeans;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class Neo4jNodeAttributeBean {

    /**
     * 自增序列
     */
    @ApiModelProperty(value="自增序列")
    private Long id;

    /**
     * 属性名称
     */
    @ApiModelProperty(value="属性名称")
    private String attributeName;

    /**
     * 表节点信息序列
     */
    @ApiModelProperty(value="表节点信息序列")
    private Long fileTableId;

    /**
     *显示颜色
     */
    @ApiModelProperty(value="显示颜色")
    private String color;

    /**
     *宽度
     */
    @ApiModelProperty(value="宽度")
    private Integer width;

    /**
     *高度
     */
    @ApiModelProperty(value="高度")
    private Integer height;


    /**
     *形状
     */
    @ApiModelProperty(value="形状")
    private String shape;

    /**
     *显示内容
     */
    @ApiModelProperty(value="显示内容")
    private String content;
}
