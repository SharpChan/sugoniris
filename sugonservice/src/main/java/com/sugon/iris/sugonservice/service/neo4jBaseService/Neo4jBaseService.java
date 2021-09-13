package com.sugon.iris.sugonservice.service.neo4jBaseService;

import com.sugon.iris.sugondomain.beans.baseBeans.Error;

import java.util.List;

public interface Neo4jBaseService {

    int addRelationBatch(String sourceTableName,
                         String targetTableName,
                         String relationship,
                         String relationshipAttribute,
                         String sourceFiled,
                         String targetFiled,
                         String sourceValue,
                         String targetValue,
                         String relationId,
                         List<Error> errorList);

    int addRelationById(String sourceLable,
                    String targetLable,
                    String sourceId,
                    String targetId,
                    String relationship,
                    String relationshipAttribute,
                    String relationId,
                        List<Error> errorList);
}
