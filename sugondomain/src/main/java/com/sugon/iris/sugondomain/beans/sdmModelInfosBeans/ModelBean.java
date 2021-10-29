package com.sugon.iris.sugondomain.beans.sdmModelInfosBeans;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class ModelBean {
    /**
     *模型id
     */
    @ApiModelProperty(value="模型id")
    private String modelId;

    /**
     * 模型名称
     */
    @ApiModelProperty(value="模型名称")
    private String modelName;

    /**
     *创建者（警号）
     */
    @ApiModelProperty(value="创建者（警号）")
    private String creator;

    /**
     * 该模型有没有共享1：分享给指定用户  0：分享给所有用户
     */
    @ApiModelProperty(value="该模型有没有共享1：分享给指定用户  0：分享给所有用户")
    private String modelType;

}
