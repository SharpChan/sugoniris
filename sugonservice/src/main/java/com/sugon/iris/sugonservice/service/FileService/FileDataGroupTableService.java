package com.sugon.iris.sugonservice.service.FileService;

import com.sugon.iris.sugondomain.beans.baseBeans.Error;
import com.sugon.iris.sugondomain.beans.system.User;
import com.sugon.iris.sugondomain.dtos.fileDtos.FileDataGroupTableDto;
import com.sugon.iris.sugondomain.dtos.systemDtos.MenuDto;

import java.util.List;

public interface FileDataGroupTableService {

    List<MenuDto> findDataGroupTableByUserId(User user, Long dataGroupId, List<Error> errorList) throws IllegalAccessException;

    Integer removeFileDataGroupTables(List<Long>  idList, List<Error> errorList);

    Integer saveFileDataGroupTables(List<FileDataGroupTableDto> fileUserTableList, List<Error> errorList);

}
