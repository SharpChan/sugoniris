package com.sugon.iris.sugondomain.dtos.neo4jDtos;

import com.sugon.iris.sugondomain.beans.neo4jBeans.Neo4jNodeAttributeBean;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
public class Neo4jNodeAttributeDto extends Neo4jNodeAttributeBean {

    /**
     * 含有该节点(源节点)属性的所有关系
     */
    @ApiModelProperty(value="含有该节点(源节点)属性的所有关系")
    List<Neo4jRelationDto> neo4jSourceRelationDtoList;

    /**
     * 含有该节点(目标节点)属性的所有关系
     */
    @ApiModelProperty(value="含有该节点(目标节点)属性的所有关系")
    List<Neo4jRelationDto> neo4jTargetRelationDtoList;
}
