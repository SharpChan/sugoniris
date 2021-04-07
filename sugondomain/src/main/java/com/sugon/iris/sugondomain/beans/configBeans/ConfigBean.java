package com.sugon.iris.sugondomain.beans.configBeans;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

@Data
public class ConfigBean {

    //ID
    private Long id;

    //对应的key
    private String cfg_key;

    //对应的值
    private String cfg_value;

    //创建时间呢
    @JsonFormat(shape=JsonFormat.Shape.STRING,pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date createTime = new Date();

    //有效标志 0：无效；1：有效
    private Integer flag;

    //修改时间
    @JsonFormat(shape=JsonFormat.Shape.STRING,pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date updateTime;

    //用户
    private String userName;

    //描述
    private String description;
}
