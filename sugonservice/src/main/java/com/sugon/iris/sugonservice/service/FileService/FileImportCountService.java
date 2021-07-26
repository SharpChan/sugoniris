package com.sugon.iris.sugonservice.service.FileService;

import com.sugon.iris.sugondomain.beans.baseBeans.Error;
import com.sugon.iris.sugondomain.dtos.fileDtos.FileCaseDto;
import com.sugon.iris.sugondomain.dtos.fileDtos.FileParsingFailedDto;

import java.util.List;

public interface FileImportCountService {

     List<FileCaseDto> getImportCount(FileCaseDto fileCaseDto,List<Error> errorList) throws IllegalAccessException;

     List<FileParsingFailedDto> getFileParsingFailed(Long userId,Long fileDetailId,List<Error> errorList) throws IllegalAccessException;

}
