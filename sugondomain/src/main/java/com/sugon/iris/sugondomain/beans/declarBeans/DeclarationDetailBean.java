package com.sugon.iris.sugondomain.beans.declarBeans;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import java.util.Date;

@Data
public class DeclarationDetailBean {
    /**
     *自增id
     */
    private Long id;

    /**
     *申报类型 1：删除案件；2：删除文件
     */
    private String businessType;

    /**
     * 申报内容
     */
    private String detail;

    /**
     *申报者id
     */
    private Long ownerUserId;

    /**
     *申报者id
     */
    private String ownerUserName;

    /**
     *审核状态 0：未审核；1：审核通过；2：审核不通过；
     */
    private String status;

    /**
     *审核人id
     */
    private Long checkUserId;

    /**
     *审核人名称
     */
    private String checkUserName;

    /**
     * 创建时间
     */
    @JsonFormat(shape=JsonFormat.Shape.STRING,pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date createTime = new Date();

    /**
     *修改时间
     */
    @JsonFormat(shape=JsonFormat.Shape.STRING,pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date updateTime = new Date();

    /**
     * 业务id
     */
    private Long businessId;
}
