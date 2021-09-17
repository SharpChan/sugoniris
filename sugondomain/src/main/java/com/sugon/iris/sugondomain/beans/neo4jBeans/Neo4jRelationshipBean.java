package com.sugon.iris.sugondomain.beans.neo4jBeans;

import lombok.Data;

@Data
public class Neo4jRelationshipBean {

  private String id;

  private  String endNodeId;

  private  String startNodeId;

  private String relationId;

  private String relationship;

  private String color;

  private String shape;
}
