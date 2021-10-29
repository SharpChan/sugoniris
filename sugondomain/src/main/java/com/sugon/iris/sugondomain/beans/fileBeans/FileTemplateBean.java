package com.sugon.iris.sugondomain.beans.fileBeans;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * 模板信息
 */

@Data
public class FileTemplateBean {

    /**
     * 自增序列
     */
    @ApiModelProperty(value="自增序列")
    private Long id;

    /**
     *模板名称
     */
    @ApiModelProperty(value="模板名称")
    private String templateName;

    /**
     *对应的表名关键字
     */
    @ApiModelProperty(value="对应的表名关键字")
    private String tablePrefix;

    /**
     *自动匹配的关键字以&&分隔
     */
    @ApiModelProperty(value="自动匹配的关键字以&&分隔")
    private String templateKey;

    /**
     *备注
     */
    @ApiModelProperty(value="备注")
    private String comment;

    /**
     *创建者
     */
    @ApiModelProperty(value="创建者")
    private Long userId;

    /**
     * 关键字排除以&&分隔
     */
    @ApiModelProperty(value="关键字排除以&&分隔")
    private String exclude;

    /**
     * 创建时间呢
     */
    @ApiModelProperty(value="创建时间呢")
    @JsonFormat(shape=JsonFormat.Shape.STRING,pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date createTime = new Date();

    /**
     *修改时间
     */
    @ApiModelProperty(value="修改时间")
    @JsonFormat(shape=JsonFormat.Shape.STRING,pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date updateTime = new Date();

    /**
     * 清洗字段组id
     */
    @ApiModelProperty(value="清洗字段组id")
    private Long fileRinseGroupId;
}
