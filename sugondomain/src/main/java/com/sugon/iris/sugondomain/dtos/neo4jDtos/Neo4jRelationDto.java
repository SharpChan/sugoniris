package com.sugon.iris.sugondomain.dtos.neo4jDtos;

import com.sugon.iris.sugondomain.beans.neo4jBeans.Neo4jRelationBean;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class Neo4jRelationDto extends Neo4jRelationBean {

    /**
     * 源节点名称
     */
    @ApiModelProperty(value="源节点名称")
    private String sourceAttributeName;

    /**
     * 目标样式名称
     */
    @ApiModelProperty(value="目标样式名称")
    private String targetAttributeName;
}
