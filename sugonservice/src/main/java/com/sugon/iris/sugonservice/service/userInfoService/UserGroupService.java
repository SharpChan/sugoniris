package com.sugon.iris.sugonservice.service.userInfoService;

import com.sugon.iris.sugondomain.beans.baseBeans.Error;
import com.sugon.iris.sugondomain.beans.system.User;
import com.sugon.iris.sugondomain.dtos.systemDtos.UserDto;
import com.sugon.iris.sugondomain.dtos.systemDtos.UserGroupDetailDto;
import com.sugon.iris.sugondomain.dtos.systemDtos.UserGroupDto;

import java.util.List;

public interface UserGroupService {

    List<UserGroupDto> userGroupList(List<Error> errorList) throws IllegalAccessException;

    UserGroupDto userGroupById(Long id ,List<Error> errorList) throws IllegalAccessException;

    Integer addUserGroup( UserGroupDto userGroupDto, List<Error> errorList) throws IllegalAccessException;

    Integer modifyUserGroup(UserGroupDto userGroupDto,List<Error> errorList) throws IllegalAccessException;

    Integer removeUserGroup(Long id,List<Error> errorList);

    List<UserDto>  getUsers(Long groupId,List<Error> errorList) throws IllegalAccessException;

    List<UserDto>  getGroupUsers(Long groupId,List<Error> errorList) throws IllegalAccessException;

    Integer addUserToUserGroup(UserGroupDetailDto userGroupDetailDto, List<Error> errorList) throws IllegalAccessException;

    Integer addUserToUserGroupBatch(User user,List<UserGroupDetailDto> userGroupDetailDtoList, List<Error> errorList) throws IllegalAccessException;

    Integer removeUserFromUserGroup(UserGroupDetailDto userGroupDetailDto, List<Error> errorList) throws IllegalAccessException;

    Integer removeUserFromUserGroupBatch(List<UserGroupDetailDto> userGroupDetailDtoList, List<Error> errorList) throws IllegalAccessException;
}
