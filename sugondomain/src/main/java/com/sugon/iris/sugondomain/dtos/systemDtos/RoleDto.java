package com.sugon.iris.sugondomain.dtos.systemDtos;

import com.sugon.iris.sugondomain.beans.system.Role;
import lombok.Data;

@Data
public class RoleDto extends Role {

    /**
     *是否勾选
     */
    private boolean isChecked;
}
