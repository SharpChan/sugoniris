package com.sugon.iris.sugondomain.beans.fileBeans;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

@Data
public class FileTemplateDetailBean {
    /**
     *自增序列
     */
    @ApiModelProperty(value="自增序列")
    private Long id;

    /**
     *模板id
     */
    @ApiModelProperty(value="模板id")
    private Long templateId;

    /**
     *字段名称
     */
    @ApiModelProperty(value="字段名称")
    private String fieldName;

    /**
     *字段关键字&&进行分割
     */
    @ApiModelProperty(value="字段关键字&&进行分割")
    private String fieldKey;

    /**
     *正则表达式&&进行分割
     */
    @ApiModelProperty(value="正则表达式&&进行分割")
    private String regular;

    /**
     *排序字段
     */
    @ApiModelProperty(value="排序字段")
    private String sortNo;

    /**
     *备注
     */
    @ApiModelProperty(value="备注")
    private String comment;

    /**
     *用户id
     */
    @ApiModelProperty(value="用户id")
    private Long userId;

    /**
     * 关键字排除&&进行分割
     */
    @ApiModelProperty(value="关键字排除&&进行分割")
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
     *清洗类型字段id
     */
    @ApiModelProperty(value="清洗类型字段id")
    private Long fileRinseDetailId;

}
