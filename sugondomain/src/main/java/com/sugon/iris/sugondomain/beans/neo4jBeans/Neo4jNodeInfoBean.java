package com.sugon.iris.sugondomain.beans.neo4jBeans;

import lombok.Data;

@Data
public class Neo4jNodeInfoBean {

    /**
     * 自增序列
     */
    private Long id;

    /**
     * 表fileTable 序列
     */
    private Long fileTableId;

    /**
     *已经初始化到neo4j数据库的行数
     */
    private Long rownum;

    /**
     *显示颜色
     */
    private String color;

    /**
     *大小
     */
    private String size;

    /**
     *标签用 . 号分割
     */
    private String label;

    /**
     *形状
     */
    private String form;

    /**
     * 创建人id
     */
    private Long userId;
}
