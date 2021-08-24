package com.sugon.iris.sugondomain.beans.system;

import lombok.Data;

@Data
public class Translate {

    /**
     * 自增序列
     */
   private Long id;

    /**
     *上级id（父级）
     */
    private Long fatherId;

    /**
     * 名称
     */
    private String source;

    /**
     * 译文
     */
    private String value;

    /**
     * 层级
     */
    private String grade;

    /**
     * 翻译类型  1：中文
     */
    private String tsType;

    /**
     * 注册页面的id
     */
    private Long menuId;

}
