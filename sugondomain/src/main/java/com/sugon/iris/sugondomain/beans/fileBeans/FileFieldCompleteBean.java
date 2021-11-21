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
    @ApiModelProperty(value="关联关系，字段间用++分割，关系间--分割（存在多个关联关系）（目标++源）")
    private String fieldRelation;

    /**
     *取值字段id，用&&进行分割（多个取值字段，取到一个非空即可）
     */
    @ApiModelProperty(value="取值字段id，用&&进行分割（多个取值字段，取到一个非空即可）")
    private String fieldSource;

    /**
     *模板组id
     */
    @ApiModelProperty(value="模板组id")
    private Long fileTemplateGroupId;

    /**
     *目标字段id
     */
    @ApiModelProperty(value="目标字段id")
    private Long fieldDest;

    /**
     *排序字段
     */
    @ApiModelProperty(value="排序字段")
    private String sortNo;


}
