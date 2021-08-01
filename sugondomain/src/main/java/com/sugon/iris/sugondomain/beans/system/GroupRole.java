package com.sugon.iris.sugondomain.beans.system;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import java.util.Date;

@Data
public class GroupRole {

    /**
     * 自增序列
     */
    private Long id;

    /**
     * 用户组id
     */
    private Long  groupId;

    /**
     * 角色id
     */
    private Long roleId;


    @JsonFormat(shape= JsonFormat.Shape.STRING,pattern="yyyy-MM-dd HH:mm:ss.SSSSSS",timezone="GMT+8")
    private Date createTime = new Date();
}
