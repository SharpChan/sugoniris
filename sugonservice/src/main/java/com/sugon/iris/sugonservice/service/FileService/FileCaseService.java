package com.sugon.iris.sugonservice.service.FileService;


import com.sugon.iris.sugondomain.beans.baseBeans.Error;
import com.sugon.iris.sugondomain.beans.system.User;
import com.sugon.iris.sugondomain.dtos.fileDtos.FileCaseDto;

import java.util.List;

public interface FileCaseService {

    Integer saveCase(User user, FileCaseDto fileCaseDto,List<Error> errorList) throws IllegalAccessException;

    Integer updateCase(User user,FileCaseDto fileCaseDto,List<Error> errorList) throws IllegalAccessException;

    List<FileCaseDto> selectCaseList(FileCaseDto fileCaseDto,List<Error> errorList) throws IllegalAccessException;

    Integer deleteCase(User user,String[] arr,List<Error> errorList) throws Exception;
}
