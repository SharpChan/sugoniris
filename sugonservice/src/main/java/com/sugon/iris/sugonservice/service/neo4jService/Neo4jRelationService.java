package com.sugon.iris.sugonservice.service.neo4jService;

import com.sugon.iris.sugondomain.beans.baseBeans.Error;
import com.sugon.iris.sugondomain.beans.system.User;
import com.sugon.iris.sugondomain.dtos.systemDtos.MenuDto;

import java.util.List;

public interface Neo4jRelationService {

    List<MenuDto> findNeo4jNodeAttributeByUserId(User user, List<Error> errorList) throws IllegalAccessException;
}
