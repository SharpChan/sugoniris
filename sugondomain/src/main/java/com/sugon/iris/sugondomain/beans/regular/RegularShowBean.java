package com.sugon.iris.sugondomain.beans.regular;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class RegularShowBean {

    /**
     * 组名称
     */
    @ApiModelProperty(value="组名称")
    private String groupName;

    /**
     * 正则表达式名称
     */
    @ApiModelProperty(value="正则表达式名称")
    private String regularName;

    /**
     *格式
     */
    @ApiModelProperty(value="格式")
    private String format;

    /**
     * 类型   1：需要匹配；2：需要排除
     */
    @ApiModelProperty(value="类型   1：需要匹配；2：需要排除")
    private String type;
}
