package com.sugon.iris.sugondata.mybaties.mapper.db1;



import com.sugon.iris.sugondomain.entities.mybatiesEntity.db1.UserEntity;

import java.util.List;

public interface UserMapper {

    List<UserEntity> selectUserList();

    void saveUser(UserEntity user);

}