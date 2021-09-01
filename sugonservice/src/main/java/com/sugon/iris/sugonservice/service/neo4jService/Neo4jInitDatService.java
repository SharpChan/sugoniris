package com.sugon.iris.sugonservice.service.neo4jService;

import com.sugon.iris.sugondomain.beans.baseBeans.Error;
import com.sugon.iris.sugondomain.beans.system.User;
import com.sugon.iris.sugondomain.dtos.fileDtos.FileTableDto;
import com.sugon.iris.sugondomain.dtos.neo4jDtos.Neo4jNodeAttributeDto;
import com.sugon.iris.sugondomain.dtos.neo4jDtos.Neo4jNodeInfoDto;

import java.util.List;


public interface Neo4jInitDatService {

      List<FileTableDto> getAllFileTableByUserid(Long userId,List<Error> errorList) throws IllegalAccessException;

      Integer initData(User user,FileTableDto fileTableDto, List<Error> errorList) throws IllegalAccessException;

      Integer attributeSave(Neo4jNodeAttributeDto neo4jNodeAttributeDto,List<Error> errorList) throws IllegalAccessException;

      List<Neo4jNodeAttributeDto> getAttributes(Long  fileTableId,List<Error> errorList) throws IllegalAccessException;

      Integer attributeUpdate(Neo4jNodeAttributeDto neo4jNodeAttributeDto,List<Error> errorList) throws IllegalAccessException;

      Integer deleteAttribute(Long  id,List<Error> errorList);
}
