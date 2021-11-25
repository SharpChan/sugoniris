package com.sugon.iris.sugondomain.beans.configBeans;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

@Data
public class ConfigBean {

    //ID
    @ApiModelProperty(value="自增序列")
    private Long id;

    //对应的key
    @ApiModelProperty(value="对应的key")
    private String cfg_key;

    //对应的值
    @ApiModelProperty(value="对应的值")
    private String cfg_value;

    //创建时间
    @ApiModelProperty(value="创建时间")
    @JsonFormat(shape=JsonFormat.Shape.STRING,pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date createTime = new Date();

    //有效标志 0：无效；1：有效
    @ApiModelProperty(value="有效标志 0：无效；1：有效")
    private Integer flag;

    //修改时间
    @ApiModelProperty(value="修改时间")
    @JsonFormat(shape=JsonFormat.Shape.STRING,pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date updateTime = new Date();

    //用户
    @ApiModelProperty(value="用户")
    private String userName;

    //描述
    @ApiModelProperty(value="描述")
    private String description;

    //描述
    @ApiModelProperty(value="排序编号")
    private String sortNo;
}
