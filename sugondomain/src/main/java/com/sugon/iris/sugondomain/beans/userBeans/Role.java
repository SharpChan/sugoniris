package com.sugon.iris.sugondomain.beans.userBeans;

import lombok.Data;
@Data
public class Role {

    private Long id;

    private String roleName;

    private String description;

    private String create_user_id;
}
