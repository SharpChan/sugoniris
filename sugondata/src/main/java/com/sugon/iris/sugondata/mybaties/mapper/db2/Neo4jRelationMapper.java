package com.sugon.iris.sugondata.mybaties.mapper.db2;


import com.sugon.iris.sugondomain.entities.mybatiesEntity.db2.Neo4jRelationEntity;

public interface Neo4jRelationMapper {
    int deleteByPrimaryKey(Long id);

    int insert(Neo4jRelationEntity record);

    int insertSelective(Neo4jRelationEntity record);

    Neo4jRelationEntity selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(Neo4jRelationEntity record);

    int updateByPrimaryKey(Neo4jRelationEntity record);
}