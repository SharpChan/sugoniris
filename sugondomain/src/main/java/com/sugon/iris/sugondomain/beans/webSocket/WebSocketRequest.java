package com.sugon.iris.sugondomain.beans.webSocket;

import com.sugon.iris.sugondomain.dtos.fileDtos.FileTableDto;
import com.sugon.iris.sugondomain.dtos.neo4jDtos.Neo4jRelationDto;
import lombok.Data;

@Data
public class WebSocketRequest {

    /**
     *
     */
    Long userId;

    /**
     * 关系信息
     */
    private Neo4jRelationDto neo4jRelationDto ;

    /**
     * 源数据模板
     */
    private FileTableDto sourceFileTableDto;


    /**
     * 目标数据模板
     */
    private FileTableDto targetFileTableDto;
}
