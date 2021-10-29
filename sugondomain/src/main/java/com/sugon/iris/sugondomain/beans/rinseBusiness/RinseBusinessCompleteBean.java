package com.sugon.iris.sugondomain.beans.rinseBusiness;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class RinseBusinessCompleteBean {

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
     *需补全字段id
     */
    @ApiModelProperty(value="需补全字段id")
    private Long fileTemplateDetailId;

    /**
     *关联字段  用逗号分隔
     */
    @ApiModelProperty(value="关联字段  用逗号分隔")
    private String relationFieldKeys;


}
