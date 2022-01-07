package com.sugon.iris.sugondomain.beans.system;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Date;


@Data
public class User implements Serializable {

    @ApiModelProperty(value="自增序列")
    private Long id;

    /*
    @Pattern(regexp = Constants.EMAIL_REGEX)
    private String email;
    */
    /**
     * 用户名
     */
    @ApiModelProperty(value="用户名")
    private String userName;

    /**
     * 身份证号
     */
    @ApiModelProperty(value="身份证号")
    private String idCard;

    /**
     * 警号
     */
    @ApiModelProperty(value="警号")
    private String policeNo;

    @ApiModelProperty(value="密码")
    @Size(min = 1, max = 50)
    private String password;

    @ApiModelProperty(value="照片URL")
    @Size(max = 256)
    private String imageUrl;

    /**
     * ip地址
     */
    @ApiModelProperty(value="ip地址")
    private String ip;

    /**
     * 用户是否被锁定 true:被锁定；false：未被锁定
     */
    @ApiModelProperty(value="用户是否被锁定 true:被锁定；false：未被锁定")
    private boolean isLocked = false;

    @ApiModelProperty(value="创建时间")
    @JsonFormat(shape=JsonFormat.Shape.STRING,pattern="yyyy-MM-dd HH:mm:ss.SSSSSS",timezone="GMT+8")
    private Date createTime = new Date();

    //0:无效 1：有效
    @ApiModelProperty(value="0:无效 1：有效")
    private Integer flag = 2;

    @ApiModelProperty(value="修改时间")
    @JsonFormat(shape=JsonFormat.Shape.STRING,pattern="yyyy-MM-dd HH:mm:ss.SSSSSS",timezone="GMT+8")
    private Date updateTime = new Date();

    /**
     * 是系统管理员角色
     */
    @ApiModelProperty(value="是系统管理员角色")
    private boolean isSystemUser;

    /**
     *是否为经侦用户
     */
    @ApiModelProperty(value="是否为经侦用户")
    private boolean isEconomicUser;

    /**
     *姓名
     */
    @ApiModelProperty(value="姓名")
    private String xm;

    /**
     * 用户单位代码
     */
    private String  xtyhbmbh;

    /**
     * 用户单位名称
     */
    private String  xtyhbmmc;
}
