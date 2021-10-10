package com.sugon.iris.sugondomain.dtos.fileDtos;

import lombok.Data;

import java.util.List;

@Data
public class MppSearchDto {
    /**
     * 案件名称
     */
    private  String caseName;

    /**
     * 模板名称
     */
    private  String templateName;

    /**
     *mpp表名
     */
    private  String tableName;

    /**
     *模板id
     */
    private Long fileTemlateId ;

    /**
     *查询开始
     */
    private Integer perSize;

    /**
     *偏移量
     */
    private Integer offset;

    /**
     * 勾选的清洗字段
     */
    List<String> checkFields;
}
