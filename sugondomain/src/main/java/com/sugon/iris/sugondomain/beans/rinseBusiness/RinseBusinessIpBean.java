package com.sugon.iris.sugondomain.beans.rinseBusiness;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class RinseBusinessIpBean {

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
     *模板字段id
     */
    @ApiModelProperty(value="模板字段id")
    private Long fileTemplateDetailId;

}
