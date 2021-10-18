package com.sugon.iris.sugonservice.service.fileService;

import com.sugon.iris.sugondomain.beans.baseBeans.Error;
import com.sugon.iris.sugondomain.beans.system.User;
import com.sugon.iris.sugondomain.dtos.fileDtos.FileTemplateGroupDto;
import java.util.List;

public interface FileTemplateGroupService {

    List<FileTemplateGroupDto> getFileTemplateGroupDtoList(User user, FileTemplateGroupDto fileTemplateGroupDto,List<Error> errorList) throws IllegalAccessException;

    int fileTemplateGroupInsert(User user, FileTemplateGroupDto fileTemplateGroupDto,List<Error> errorList);

    int deleteFileTemplateGroup(String[] idArr, List<Error> errorList);

}
