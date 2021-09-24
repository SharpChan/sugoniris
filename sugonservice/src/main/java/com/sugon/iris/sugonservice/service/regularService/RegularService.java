package com.sugon.iris.sugonservice.service.regularService;

import com.sugon.iris.sugondomain.beans.baseBeans.Error;
import com.sugon.iris.sugondomain.dtos.regularDtos.RegularDetailDto;
import com.sugon.iris.sugondomain.dtos.regularDtos.RegularGroupDto;

import java.util.List;

public interface RegularService {

    Integer addGroup(RegularGroupDto regularGroupDto, List<Error> errorList) throws IllegalAccessException;

    Integer modifyGroup(RegularGroupDto regularGroupDto, List<Error> errorList) throws IllegalAccessException;

    List<RegularGroupDto> findRegularGroup(Long userId,List<Error> errorList) throws IllegalAccessException;

    Integer deleteGroupByPrimaryKey(Long id, List<Error> errorList);

}
