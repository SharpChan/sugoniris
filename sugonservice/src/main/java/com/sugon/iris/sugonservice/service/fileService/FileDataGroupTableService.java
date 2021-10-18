package com.sugon.iris.sugonservice.service.fileService;

import com.sugon.iris.sugondomain.beans.baseBeans.Error;
import com.sugon.iris.sugondomain.beans.system.User;
import com.sugon.iris.sugondomain.dtos.systemDtos.MenuDto;
import com.sugon.iris.sugondomain.dtos.systemDtos.OwnerMenuDto;
import java.util.List;

public interface FileDataGroupTableService {

    List<MenuDto> findDataGroupTableByUserId(User user, Long dataGroupId, List<Error> errorList) throws IllegalAccessException;

    Integer removeFileDataGroupTables( User user,List<OwnerMenuDto> fileDataGroupTableList, List<Error> errorList);

    Integer saveFileDataGroupTables(User user,List<OwnerMenuDto> fileDataGroupTableList, List<Error> errorList);

}
