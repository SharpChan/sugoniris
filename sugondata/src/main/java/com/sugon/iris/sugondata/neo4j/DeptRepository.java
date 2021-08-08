package com.sugon.iris.sugondata.neo4j;


import com.sugon.iris.sugondomain.entities.neo4j.Dept;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DeptRepository extends Neo4jRepository<Dept,Long> {

}
