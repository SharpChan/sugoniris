package com.sugon.iris.sugondomain.entities.jdbcTemplateEntity.baseEntities;

import lombok.Data;

@Data
public class SequenceEntity {
    private String name;

    private long current_value;

    private long increment;
}
