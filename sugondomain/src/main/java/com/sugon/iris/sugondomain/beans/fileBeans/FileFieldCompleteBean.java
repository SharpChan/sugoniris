package com.sugon.iris.sugondomain.beans.fileBeans;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 可配置数据补全
 */
@Data
public class FileFieldCompleteBean {

    /**
     *自增序列
     */
    @ApiModelProperty(value="自增序列")
    private Long id;

    /**
     *目地模板id
     */
    @ApiModelProperty(value="目地模板id")
    private Long destFileTemplateId;

    /**
     *源模板id
     */
    @ApiModelProperty(value="源模板id")
    private Long sourceFileTemplateId;

    /**
     *关联关系
     */
    @ApiModelProperty(value="关联关系，字段间用&&分割，关系间--分割（存在多个关联关系）")
    private String fieldRelation;

    /**
     *取值字段，用&&进行分割（多个取值字段，取到一个非空即可）
     */
    @ApiModelProperty(value="取值字段，用&&进行分割（多个取值字段，取到一个非空即可）")
    private String fieldSource;
}
