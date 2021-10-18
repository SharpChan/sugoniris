package com.sugon.iris.sugonservice.service.fileService;

import com.sugon.iris.sugondomain.beans.baseBeans.Error;
import com.sugon.iris.sugondomain.beans.system.User;
import com.sugon.iris.sugondomain.dtos.fileDtos.FileDataGroupDto;
import java.util.List;

public interface FileDataGroupService {

    List<FileDataGroupDto> getFileDataGroupDtoList(User user, FileDataGroupDto fileDataGroupDto, List<Error> errorList) throws IllegalAccessException;

    int fileDataGroupInsert(User user, FileDataGroupDto fileDataGroupDto, List<Error> errorList) throws IllegalAccessException;

    int modifyGroupInsert(FileDataGroupDto fileDataGroupDto, List<Error> errorList) throws IllegalAccessException;

    int deleteFileDataGroup(Long  id, List<Error> errorList);
}
