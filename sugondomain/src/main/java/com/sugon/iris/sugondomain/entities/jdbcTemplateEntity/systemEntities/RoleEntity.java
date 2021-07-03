package com.sugon.iris.sugondomain.entities.jdbcTemplateEntity.systemEntities;

import lombok.Data;

import java.io.Serializable;

@Data
public class RoleEntity implements Serializable {

    private Long id;

    private String roleName;

    private String description;

    private String create_user_id;
}
