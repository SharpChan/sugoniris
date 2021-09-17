package com.sugon.iris.sugonservice.service.neo4jService;

import com.sugon.iris.sugondomain.beans.baseBeans.Error;
import com.sugon.iris.sugondomain.dtos.fileDtos.FileCaseDto;
import java.util.List;

public interface Neo4jModelOneService {

   List<FileCaseDto> getAllNeo4jInfos(Long userId, List<Error> errorList) throws IllegalAccessException;

}
