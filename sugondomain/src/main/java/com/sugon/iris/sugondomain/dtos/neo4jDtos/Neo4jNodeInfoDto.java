package com.sugon.iris.sugondomain.dtos.neo4jDtos;

import com.sugon.iris.sugondomain.beans.neo4jBeans.Neo4jNodeInfoBean;
import lombok.Data;

import java.util.List;

@Data
public class Neo4jNodeInfoDto extends Neo4jNodeInfoBean {

    /**
     * 标签列表
     */
    private List<String> labelList;
    /**
     * 节点属性
     */
    private List<Neo4jNodeAttributeDto> neo4jNodeAttributeDtoList;


}
