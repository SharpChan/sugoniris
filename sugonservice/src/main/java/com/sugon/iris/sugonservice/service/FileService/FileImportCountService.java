package com.sugon.iris.sugonservice.service.FileService;

import com.sugon.iris.sugondomain.beans.baseBeans.Error;
import com.sugon.iris.sugondomain.dtos.fileDtos.FileCaseDto;
import java.util.List;

public interface FileImportCountService {

    public List<FileCaseDto> getImportCount(Long userId,List<Error> errorList) throws IllegalAccessException;

}
