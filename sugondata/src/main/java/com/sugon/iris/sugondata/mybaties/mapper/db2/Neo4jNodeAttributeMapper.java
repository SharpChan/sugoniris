package com.sugon.iris.sugondata.mybaties.mapper.db2;


import com.sugon.iris.sugondomain.entities.mybatiesEntity.db2.Neo4jNodeAttributeEntity;

import java.util.List;

public interface Neo4jNodeAttributeMapper {

  int  saveNeo4jNodeAttribute(Neo4jNodeAttributeEntity neo4jNodeAttributeEntity4Sql);

  List<Neo4jNodeAttributeEntity> getNeo4jNodeAttributeLis(Neo4jNodeAttributeEntity neo4jNodeAttributeEntity4Sql);

  int  updateNeo4jNodeAttribute(Neo4jNodeAttributeEntity neo4jNodeAttributeEntity4Sql);

  int  deleteNeo4jNodeAttribute(Long id);
}
