package com.sugon.iris.sugondomain.beans.declarBeans;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import java.util.Date;

@Data
public class DeclarationDetailBean {
    /**
     *自增id
     */
    @ApiModelProperty(value="自增id")
    private Long id;

    /**
     *申报类型 1：删除案件；2：删除文件
     */
    @ApiModelProperty(value="申报类型 1：删除案件；2：删除文件")
    private String businessType;

    /**
     * 申报内容
     */
    @ApiModelProperty(value="申报内容")
    private String detail;

    /**
     *申报者id
     */
    @ApiModelProperty(value="申报者id")
    private Long ownerUserId;

    /**
     *申报者姓名
     */
    @ApiModelProperty(value="申报者姓名")
    private String ownerUserName;

    /**
     *审核状态 0：未审核；1：审核通过；2：审核不通过；
     */
    @ApiModelProperty(value="审核状态 0：未审核；1：审核通过；2：审核不通过；")
    private String status;

    /**
     *审核人id
     */
    @ApiModelProperty(value="审核人id")
    private Long checkUserId;

    /**
     *审核人名称
     */
    @ApiModelProperty(value="审核人名称")
    private String checkUserName;

    /**
     * 创建时间
     */
    @ApiModelProperty(value="创建时间")
    @JsonFormat(shape=JsonFormat.Shape.STRING,pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date createTime = new Date();

    /**
     *修改时间
     */
    @ApiModelProperty(value="修改时间")
    @JsonFormat(shape=JsonFormat.Shape.STRING,pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date updateTime = new Date();

    /**
     * 业务id
     */
    @ApiModelProperty(value="业务id")
    private Long businessId;
}
