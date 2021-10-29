package com.sugon.iris.sugondomain.beans.baseBeans;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.util.Date;

@Data
public class WhiteIp implements Serializable {

    /**
     * 自增序列
     */
    @ApiModelProperty(value="自增序列")
    private Long id;

    /**
     * 姓名
     */
    @ApiModelProperty(value="姓名")
    private String name;

    /**
     * 身份证号
     */
    @ApiModelProperty(value="身份证号")
    @NotBlank(message = "身份证号")
    private String idCard;

    /**
     * 警号
     */
    @ApiModelProperty(value="警号")
    private String policeNo;

    /**
     * ip地址
     */
    @ApiModelProperty(value="ip地址")
    @NotBlank(message = "请输入ip")
    private String ip;

    /**
     * mac地址
     */
    @ApiModelProperty(value="mac地址")
    private String mac;

    /**
     * 备注
     */
    @ApiModelProperty(value="备注")
    private String comment;

    @ApiModelProperty(value="创建时间")
    @JsonFormat(shape=JsonFormat.Shape.STRING,pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date createTime = new Date();

    @ApiModelProperty(value="修改时间")
    @JsonFormat(shape=JsonFormat.Shape.STRING,pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date updateTime = new Date();
}
