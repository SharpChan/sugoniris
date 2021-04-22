package com.sugon.iris.sugondomain.beans.sdmModelInfosBeans;

import lombok.Data;

@Data
public class ResultColumnBean {
    /**
     * id
     */
    private Long id;
    /**
     * 列名
     */
    private  String columnName;

    /**
     * 列注释
     */
    private  String columnComment;
}
