package com.sugon.iris.sugondomain.beans.rinseBusiness;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class RinseBusinessNullBean {

    /**
     *自增序列
     */
    @ApiModelProperty(value="自增序列")
    private Long id;

    /**
     *模板编号
     */
    @ApiModelProperty(value="模板编号")
    private Long fileTemplateId;

    /**
     *字段id
     */
    @ApiModelProperty(value="字段id")
    private Long fileTemplateDetailId;

    /**
     *替换的值
     */
    @ApiModelProperty(value="替换的值")
    private String value;
}
