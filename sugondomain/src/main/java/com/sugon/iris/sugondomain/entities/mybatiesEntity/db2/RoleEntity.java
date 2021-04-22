package com.sugon.iris.sugondomain.entities.mybatiesEntity.db2;


public class RoleEntity {

    private Integer id;
    private String name;

    public RoleEntity() {
    }

    public RoleEntity(Integer id, String name) {
        this.id = id;
        this.name = name;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}