package com.sugon.iris.sugondomain.beans.system;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import java.util.Date;

@Data
public class RolePage {

    /**
     * 自增序列
     */
    private Long id;

    /**
     * 角色id
     */
    private Long roleId;

    /**
     * 菜单id
     */
    private Long menuId;

    @JsonFormat(shape= JsonFormat.Shape.STRING,pattern="yyyy-MM-dd HH:mm:ss.SSSSSS",timezone="GMT+8")
    private Date createTime = new Date();
}
