package com.sugon.iris.sugonservice.impl.userInfoImpl;


import com.sugon.iris.sugondata.mybaties.mapper.db1.UserMapper;
import com.sugon.iris.sugondomain.entities.mybatiesEntity.db1.UserEntity;
import com.sugon.iris.sugonservice.service.userInfoService.UserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    @Resource
    private UserMapper userMapper;

    @Override
    public List<UserEntity> selectUserList() {
        return this.userMapper.selectUserList();
    }

    @Transactional
    @Override
    public void saveUser(UserEntity user) {
        this.userMapper.saveUser(user);
//        throw new RuntimeException();
    }
}