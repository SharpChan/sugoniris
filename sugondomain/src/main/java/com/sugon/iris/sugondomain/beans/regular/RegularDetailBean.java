package com.sugon.iris.sugondomain.beans.regular;

import lombok.Data;

@Data
public class RegularDetailBean {
    /**
     * 自增序列
     */
    private Long id;

    /**
     * 正则表达式名称
     */
    private String regularName;

    /**
     * 正则表达式
     */
    private String regularValue;

    /**
     * 正则表达式组
     */
    private Long regularGroupId;

    /**
     * 备注
     */
    private String comment;

    /**
     * 排序字段
     */
    private Integer sort;

    /**
     * 创建者id
     */
    private Long userId;

}