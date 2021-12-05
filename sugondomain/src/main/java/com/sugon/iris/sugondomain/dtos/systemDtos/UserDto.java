package com.sugon.iris.sugondomain.dtos.systemDtos;

import com.sugon.iris.sugondomain.beans.system.User;
import lombok.Data;

@Data
public class UserDto extends User {

    /**
     * 原始密码
     */
    private String oldPassword;

}
