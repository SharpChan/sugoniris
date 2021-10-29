package com.sugon.iris.sugondomain.dtos.neo4jDtos;

import com.sugon.iris.sugondomain.beans.neo4jBeans.Neo4jNodeInfoBean;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
public class Neo4jNodeInfoDto extends Neo4jNodeInfoBean {

    /**
     * 标签列表
     */
    @ApiModelProperty(value="标签列表")
    private List<String> labelList;
    /**
     * 节点属性
     */
    @ApiModelProperty(value="节点属性")
    private List<Neo4jNodeAttributeDto> neo4jNodeAttributeDtoList;


}
