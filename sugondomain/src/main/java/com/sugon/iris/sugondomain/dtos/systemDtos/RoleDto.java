package com.sugon.iris.sugondomain.dtos.systemDtos;

import com.sugon.iris.sugondomain.beans.system.Role;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class RoleDto extends Role {

    /**
     *是否勾选
     */
    @ApiModelProperty(value="是否勾选")
    private boolean isChecked;
}
