package com.sugon.iris.sugondomain.beans.declarBeans;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class DeclarInfoBean {

    /**
     * 申报状态
     */
    @ApiModelProperty(value="申报状态")
    private String status;

    /**
     * 计数
     */
    @ApiModelProperty(value="计数")
    private Integer amount;

}
