package com.sugon.iris.sugondomain.beans.system;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class UserGroupDetail implements Serializable {

    /**
     * 自增序列
     */
    @ApiModelProperty(value="自增序列")
  private Long id;

    /**
     * 用户编号
     */
    @ApiModelProperty(value="用户编号")
  private Long userId;

    /**
     * 用户组编号
     */
    @ApiModelProperty(value="用户组编号")
  private Long  userGroupId;

    /**
     * 创建时间
     */
    @ApiModelProperty(value="创建时间")
    @JsonFormat(shape=JsonFormat.Shape.STRING,pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date createTime = new Date();

    /**
     * 创建者id
     */
    @ApiModelProperty(value="创建者id")
    private Long createUserId;

}
