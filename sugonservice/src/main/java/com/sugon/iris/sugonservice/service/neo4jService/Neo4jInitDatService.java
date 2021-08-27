package com.sugon.iris.sugonservice.service.neo4jService;

import com.sugon.iris.sugondomain.beans.baseBeans.Error;
import com.sugon.iris.sugondomain.beans.system.User;
import com.sugon.iris.sugondomain.dtos.fileDtos.FileTableDto;
import java.util.List;


public interface Neo4jInitDatService {

      List<FileTableDto> getAllFileTableByUserid(Long userId,List<Error> errorList) throws IllegalAccessException;

      Integer initData(User user,FileTableDto fileTableDto, List<Error> errorList) throws IllegalAccessException;
}
