package com.sugon.iris.sugonservice.service.regularService;

import com.sugon.iris.sugondomain.beans.baseBeans.Error;
import com.sugon.iris.sugondomain.dtos.regularDtos.RegularDetailDto;

import java.util.List;

public interface RegularDetailService {

    Integer addDetail(RegularDetailDto regularDetailDto, List<Error> errorList) throws IllegalAccessException;

    List<RegularDetailDto> findRegularDetailsByGroupId(Long regularGroupId, List<Error> errorList) throws IllegalAccessException;

    List<String> findRegularDetailsByUserId(Long userId, List<Error> errorList) throws IllegalAccessException;

    Integer deleteDetailByPrimaryKey(Long id, List<Error> errorList);

    Integer deleteDetailByGroupId(Long groupId, List<Error> errorList);

    Integer modifyDetailByPrimaryKey(RegularDetailDto regularDetailDto, List<Error> errorList) throws IllegalAccessException;
}
