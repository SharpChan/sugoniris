package com.sugon.iris.sugondomain.entities.neo4j;

import lombok.Builder;
import lombok.Data;
import org.neo4j.ogm.annotation.*;

@NodeEntity(label = "dept")
@Data
@Builder
public class Dept {

    @Id
    @GeneratedValue
    private Long id;

    @Property(name = "deptName")
    private String deptName;
}