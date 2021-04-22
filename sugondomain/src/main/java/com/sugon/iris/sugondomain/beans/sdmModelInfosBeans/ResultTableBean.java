package com.sugon.iris.sugondomain.beans.sdmModelInfosBeans;

import lombok.Data;

import java.util.List;

@Data
public class ResultTableBean {

    /**
     * 表id
     */
    private Long tableID;

    /**
     * hadoop表名
     */
    private String tableName;
    /**
     *表注释
     */
    private String note;

}
