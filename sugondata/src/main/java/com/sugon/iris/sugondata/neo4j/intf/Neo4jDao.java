package com.sugon.iris.sugondata.neo4j.intf;

import com.sugon.iris.sugondomain.beans.neo4jBeans.Elements;

import java.util.Map;

public interface Neo4jDao {

     void addNode(String label,String property);

     Map<String, Object> addRelationBatch(String sourceTableName, String targetTableName, String relationship, String sourceFiled, String targetFiled, String sourceValue, String targetValue, String relationId);

     Map<String, Object> addRelationById( String sourceLable,
                      String targetLable,
                      String sourceId,
                      String targetId,
                      String relationship,
                      String relationId);

     Elements getRelations(
             String relationship,
             String relationId,
             Map<String,String> map);
}
