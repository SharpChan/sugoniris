package com.sugon.iris.sugonservice.service.relationService;

import com.sugon.iris.sugondomain.beans.webSocket.WebSocketRequest;
import com.sugon.iris.sugondomain.dtos.neo4jDtos.Neo4jRelationDto;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Map;

@Service
public interface RelationCreateService {

    /**
     *
      * @param
     * @return
     */
   void test1(WebSocketRequest webSocketRequest) throws IOException;

}
