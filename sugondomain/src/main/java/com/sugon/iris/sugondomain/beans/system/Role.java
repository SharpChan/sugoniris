package com.sugon.iris.sugondomain.beans.system;

import lombok.Data;

import java.io.Serializable;

@Data
public class Role implements Serializable {

    private Long id;

    private String roleName;

    private String description;

    private String create_user_id;
}
