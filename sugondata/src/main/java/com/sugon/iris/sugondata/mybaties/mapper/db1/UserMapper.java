package com.sugon.iris.sugondata.mybaties.mapper.db1;



import com.sugon.iris.sugondomain.entities.db1.User;

import java.util.List;

public interface UserMapper {

    List<User> selectUserList();

    void saveUser(User user);

}