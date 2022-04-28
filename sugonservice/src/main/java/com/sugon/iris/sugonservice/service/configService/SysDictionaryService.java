package com.sugon.iris.sugonservice.service.configService;

import com.sugon.iris.sugondomain.beans.baseBeans.Error;
import com.sugon.iris.sugondomain.dtos.configDtos.SysDictionaryDto;

import java.util.List;

public interface SysDictionaryService {

    List<SysDictionaryDto> getAllSysDictionaries(List<Error> errorList) throws IllegalAccessException;

    List<SysDictionaryDto> getSysDictionariesByDicGroupLike(String dicGroup,List<Error> errorList) throws IllegalAccessException;

    List<SysDictionaryDto> getSysDictionariesByDicGroup(String dicGroup,List<Error> errorList) throws IllegalAccessException;

    int updateSysDictionary(SysDictionaryDto sysDictionaryDto,List<Error> errorList) throws IllegalAccessException;

    int saveSysDictionary(SysDictionaryDto sysDictionaryDto,List<Error> errorList) throws IllegalAccessException;

    int deleteSysDictionary(Long id, List<Error> errorList);
}
