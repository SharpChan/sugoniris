package com.sugon.iris.sugondomain.entities.userEntities;

import lombok.Data;

import java.io.Serializable;

@Data
public class RoleEntity implements Serializable {

    private Long id;

    private String roleName;

    private String description;

    private String create_user_id;
}
