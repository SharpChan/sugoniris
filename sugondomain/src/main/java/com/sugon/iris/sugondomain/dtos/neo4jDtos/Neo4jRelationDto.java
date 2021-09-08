package com.sugon.iris.sugondomain.dtos.neo4jDtos;

import com.sugon.iris.sugondomain.beans.neo4jBeans.Neo4jRelationBean;
import lombok.Data;

@Data
public class Neo4jRelationDto extends Neo4jRelationBean {

    /**
     * 源节点名称
     */
    private String sourceAttributeName;

    /**
     * 目标样式id（neo4j_node_attribute的id）
     */
    private String targetAttributeName;
}
