package com.sugon.iris.sugondomain.beans.system;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class Role implements Serializable {

    @ApiModelProperty(value="自增序列")
    private Long id;

    @ApiModelProperty(value="角色名称")
    private String roleName;

    @ApiModelProperty(value="描述")
    private String description;

    @ApiModelProperty(value="创建者id")
    private Long create_user_id;

    @ApiModelProperty(value="创建时间")
    @JsonFormat(shape= JsonFormat.Shape.STRING,pattern="yyyy-MM-dd HH:mm:ss.SSSSSS",timezone="GMT+8")
    private Date createTime = new Date();

    @ApiModelProperty(value="修改时间")
    @JsonFormat(shape= JsonFormat.Shape.STRING,pattern="yyyy-MM-dd HH:mm:ss.SSSSSS",timezone="GMT+8")
    private Date updateTime = new Date();;
}
