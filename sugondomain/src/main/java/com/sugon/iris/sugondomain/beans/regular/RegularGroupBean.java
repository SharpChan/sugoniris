package com.sugon.iris.sugondomain.beans.regular;

import lombok.Data;

@Data
public class RegularGroupBean {
    /**
     * 自增序列
     */
    private Long id;

    /**
     *正则表达式名称
     */
    private String groupName;

    /**
     *备注
     */
    private String comment;

    /**
     *排序
     */
    private Integer sort;

    /**
     *
     */
    private Long userId;
}