package com.sugon.iris.sugondomain.beans.rinseBusiness;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class RinseBusinessPrefixBean {

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

    /**
     *后缀，一个列可能有多种后缀
     */
    @ApiModelProperty(value="前缀，一个列可能有多种前缀")
    private String prefix;

}
