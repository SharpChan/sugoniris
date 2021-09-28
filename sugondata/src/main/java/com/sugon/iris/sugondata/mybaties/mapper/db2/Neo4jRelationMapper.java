package com.sugon.iris.sugondata.mybaties.mapper.db2;


import com.sugon.iris.sugondomain.entities.mybatiesEntity.db2.Neo4jRelationEntity;

import java.util.List;

public interface Neo4jRelationMapper {

    int deleteByPrimaryKey(Long id);

    int insert(Neo4jRelationEntity record);

    Neo4jRelationEntity selectByPrimaryKey(Long id);

    List<Neo4jRelationEntity> selectByUserId(Long id);

    List<Neo4jRelationEntity> findRelations(Neo4jRelationEntity neo4jRelationEntity4Sql);

}