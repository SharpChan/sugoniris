package com.sugon.iris.sugonservice.service.FileService;

import com.sugon.iris.sugondomain.beans.baseBeans.Error;
import com.sugon.iris.sugondomain.beans.system.User;
import com.sugon.iris.sugondomain.dtos.fileDtos.FileDataGroupDetailDto;
import com.sugon.iris.sugondomain.dtos.systemDtos.UserDto;
import java.util.List;

public interface FileDataGroupDetailService {

    List<UserDto> findFileDataGroupUsersByGroupId(Long groupId, List<Error> errorList);

    List<UserDto> findUsersNotInDataGroupsByUserId(Long createUserId, List<Error> errorList);

    Integer saveUserFromDataGroupDetail(User user,List<FileDataGroupDetailDto> fileDataGroupDetailDtoList, List<Error> errorList);

    Integer deleteUserFromDataGroup(List<FileDataGroupDetailDto> fileDataGroupDetailDtoList, List<Error> errorList);
}
