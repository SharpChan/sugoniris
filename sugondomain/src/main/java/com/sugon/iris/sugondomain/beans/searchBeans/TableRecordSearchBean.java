package com.sugon.iris.sugondomain.beans.searchBeans;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
public class TableRecordSearchBean {

    /**
     * 案件编号
     */
    @ApiModelProperty(value="案件编号")
    private String caseNo;

    /**
     * 案件名称
     */
    @ApiModelProperty(value="案件名称")
    private String caseName;

    /**
     *表名
     */
    @ApiModelProperty(value="表名")
    private String tableName;

    /**
     *模板名称
     */
    @ApiModelProperty(value="模板名称")
    private String templateName;

    /**
     * 查询结果
     */
    @ApiModelProperty(value="查询结果")
    private List<String> result;
}
