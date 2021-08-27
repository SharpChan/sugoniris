package com.sugon.iris.sugondata.mybaties.mapper.db2;

import com.sugon.iris.sugondomain.entities.mybatiesEntity.db2.Neo4jNodeInfoEntity;

import java.util.List;

public interface Neo4jNodeInfoMapper {

  int  saveNeo4jNodeInfo(Neo4jNodeInfoEntity neo4jNodeInfoEntity4Sql );

  Neo4jNodeInfoEntity getNeo4jNodeInfo(Neo4jNodeInfoEntity neo4jNodeInfoEntity4Sql );

  int  updateNeo4jNodeInfo(Neo4jNodeInfoEntity neo4jNodeInfoEntity4Sql );

  int  deleteNeo4jNodeInfos(List<Long> ids );
}
