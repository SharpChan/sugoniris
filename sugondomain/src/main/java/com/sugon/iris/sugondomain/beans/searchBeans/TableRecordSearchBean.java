package com.sugon.iris.sugondomain.beans.searchBeans;

import lombok.Data;

@Data
public class TableRecordSearchBean {

    /**
     * 案件编号
     */
    private String caseNo;

    /**
     * 案件名称
     */
    private String caseName;

    /**
     *表名
     */
    private String tableName;

    /**
     *模板名称
     */
    private String templateName;

    /**
     * 查询结果
     */
    private String result;
}
