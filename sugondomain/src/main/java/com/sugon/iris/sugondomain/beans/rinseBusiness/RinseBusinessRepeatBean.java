package com.sugon.iris.sugondomain.beans.rinseBusiness;


import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class RinseBusinessRepeatBean {

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
     *字段id组合用逗号隔开
     */
    @ApiModelProperty(value="字段id组合用逗号隔开")
    private String fields;

}
