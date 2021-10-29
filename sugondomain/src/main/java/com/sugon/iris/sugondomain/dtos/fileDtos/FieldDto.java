package com.sugon.iris.sugondomain.dtos.fileDtos;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class  FieldDto{

    /**
     * 表中字段
     */
    @ApiModelProperty(value="表中字段")
    private String  fieldName;

    /**
     * 字段关键字&&进行分割
     */
    @ApiModelProperty(value="字段关键字&&进行分割")
    private String  fieldKey;
}