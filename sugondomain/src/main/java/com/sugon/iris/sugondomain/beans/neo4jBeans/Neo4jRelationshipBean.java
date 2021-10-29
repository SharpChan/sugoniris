package com.sugon.iris.sugondomain.beans.neo4jBeans;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class Neo4jRelationshipBean {

  @ApiModelProperty(value="自增序列")
  private String id;

  @ApiModelProperty(value="目标节点id")
  private  String endNodeId;

  @ApiModelProperty(value="开始节点id")
  private  String startNodeId;

  @ApiModelProperty(value="关系id")
  private String relationId;

  @ApiModelProperty(value="关系")
  private String relationship;

  @ApiModelProperty(value="颜色")
  private String color;

  @ApiModelProperty(value="形状")
  private String shape;
}
