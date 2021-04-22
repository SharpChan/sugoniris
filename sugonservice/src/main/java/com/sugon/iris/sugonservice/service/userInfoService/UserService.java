package com.sugon.iris.sugonservice.service.userInfoService;



import com.sugon.iris.sugondomain.entities.mybatiesEntity.db1.UserEntity;

import java.util.List;

public interface UserService {

    List<UserEntity> selectUserList();

    void saveUser(UserEntity user);

}