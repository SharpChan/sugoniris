package com.sugon.iris.sugondomain.beans.system;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import java.util.Date;

@Data
public class OwnerMenu {

    /**
     * 自增序列
     */
    @ApiModelProperty(value="自增序列")
    private Long id;

    /**
     * 角色id
     */
    @ApiModelProperty(value="角色id")
    private Long ownerId;

    /**
     * 菜单id
     */
    @ApiModelProperty(value="菜单id")
    private Long menuId;

    @ApiModelProperty(value="创建时间")
    @JsonFormat(shape= JsonFormat.Shape.STRING,pattern="yyyy-MM-dd HH:mm:ss.SSSSSS",timezone="GMT+8")
    private Date createTime = new Date();
}
