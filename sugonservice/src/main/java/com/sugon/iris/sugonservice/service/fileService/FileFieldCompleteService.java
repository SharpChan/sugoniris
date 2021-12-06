package com.sugon.iris.sugonservice.service.fileService;

import com.sugon.iris.sugondomain.beans.baseBeans.Error;
import com.sugon.iris.sugondomain.dtos.fileDtos.FileFieldCompleteDto;
import java.util.List;

public interface FileFieldCompleteService {

    List<FileFieldCompleteDto> getFileFieldCompletesList(Long groupId, List<Error> errorList) throws IllegalAccessException;

    int saveFileFieldComplete(FileFieldCompleteDto  fileFieldCompleteDto, List<Error> errorList) throws IllegalAccessException;

    int removeFileFieldComplete(Long id , List<Error> errorList);

    boolean modifyCompletesSortNoById(Long id,String sortNo, List<Error> errorList);

}
