package com.sugon.iris.sugondomain.beans.fileBeans;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;
import java.util.Objects;

@Data
public class FileTemplateGroupBean {

    /**
     * 自增序列
     */
    @ApiModelProperty(value="自增序列")
    private Long id;

    /**
     * 模板组ID
     */
    @ApiModelProperty(value="模板组ID")
    private Long groupId;

    /**
     * 模板组名称
     */
    @ApiModelProperty(value="模板组名称")
    private String groupName;

    /**
     * 模板id
     */
    @ApiModelProperty(value="模板id")
    private Long templateId;

    /**
     * 模板名称
     */
    @ApiModelProperty(value="模板名称")
    private String templateName;

    /**
     * 备注
     */
    @ApiModelProperty(value="备注")
    private String comment;

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
     *创建者
     */
    @ApiModelProperty(value="创建者")
    private Long userId;



}
