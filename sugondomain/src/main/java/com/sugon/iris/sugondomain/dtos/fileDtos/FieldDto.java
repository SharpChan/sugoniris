package com.sugon.iris.sugondomain.dtos.fileDtos;

import lombok.Data;

@Data
public class  FieldDto{

    /**
     * 表中字段
     */
    private String  fieldName;

    /**
     * 字段关键字&&进行分割
     */
    private String  fieldKey;
}