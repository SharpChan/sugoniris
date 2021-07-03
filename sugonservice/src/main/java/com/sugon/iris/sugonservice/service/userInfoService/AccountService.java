package com.sugon.iris.sugonservice.service.userInfoService;

import com.sugon.iris.sugondomain.beans.baseBeans.Error;
import com.sugon.iris.sugondomain.beans.system.User;
import com.sugon.iris.sugondomain.dtos.systemDtos.UserDto;

import java.util.List;

public interface AccountService {
     /**
      * 账户保存
      * @param userDto
      * @param errorList
      * @return
      */
     int saveAccount(UserDto userDto, List<Error> errorList);

     /**
      * 登录后获取账户信息
      * @param userDto
      * @param errorList
      * @return
      */
     User getUserInfo(UserDto userDto, List<Error> errorList) throws IllegalAccessException;

     int updateUser(UserDto userDto, List<Error> errorList) throws IllegalAccessException;

     List<User> getUserInfoCheck(String keyWord,Integer flag, List<Error> errorList ) throws IllegalAccessException;

     int alterUserStatus(Long id ,Integer flag, List<Error> errorList);

     int deleteUser(Long id , List<Error> errorList);
}
