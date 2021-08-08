package com.sugon.iris.sugondata.neo4j;

import com.sugon.iris.sugondomain.entities.neo4j.RelationShip;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RelationShipRepository extends Neo4jRepository<RelationShip, Long> {

}

