package com.sugon.iris.sugondomain.beans.neo4jBeans;

import lombok.Data;

@Data
public class Neo4jNodeAttributeBean {

    /**
     * 自增序列
     */
    private Long id;

    /**
     * 自增序列
     */
    private String attributeName;

    /**
     * 表节点信息序列
     */
    private Long fileTableId;

    /**
     *显示颜色
     */
    private String color;

    /**
     *宽度
     */
    private Integer width;

    /**
     *高度
     */
    private Integer height;


    /**
     *形状
     */
    private String shape;

    /**
     *显示内容
     */
    private String content;
}
