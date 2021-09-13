package com.sugon.iris.sugondata.neo4j.intf;

import org.neo4j.driver.Result;

import java.util.Map;

public interface Neo4jDao {

     void addNode(String label,String property);

     Map<String, Object> addRelationBatch(String sourceTableName, String targetTableName, String relationship, String relationshipAttribute, String sourceFiled, String targetFiled, String sourceValue, String targetValue, String relationId);

     Map<String, Object> addRelationById( String sourceLable,
                      String targetLable,
                      String sourceId,
                      String targetId,
                      String relationship,
                      String relationshipAttribute,
                      String relationId);

     Map<String, Object> getRelations(
             String relationship,
             String relationshipAttribute,
             String relationId);
}
