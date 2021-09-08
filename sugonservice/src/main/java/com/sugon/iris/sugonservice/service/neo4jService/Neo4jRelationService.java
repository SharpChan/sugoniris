package com.sugon.iris.sugonservice.service.neo4jService;

import com.sugon.iris.sugondomain.beans.baseBeans.Error;
import com.sugon.iris.sugondomain.beans.system.User;
import com.sugon.iris.sugondomain.dtos.neo4jDtos.Neo4jRelationDto;
import com.sugon.iris.sugondomain.dtos.systemDtos.MenuDto;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface Neo4jRelationService {

    List<MenuDto> findNeo4jNodeAttributeByUserId(User user, List<Error> errorList) throws IllegalAccessException;

    Integer saveRelation(User user, Neo4jRelationDto neo4jRelationDto, List<Error> errorList) throws Exception;

    List<Neo4jRelationDto> getRelations(User user, List<Error> errorList) throws IllegalAccessException;

    Integer initRelation(User user, Neo4jRelationDto neo4jRelationDto, List<Error> errorList);
}
