package com.sugon.iris.sugonweb.demoTest;

import com.sugon.iris.sugondomain.entities.mybatiesEntity.db1.UserEntity;
import com.sugon.iris.sugonservice.service.userInfoService.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/webuser")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/select/list")
    public List<UserEntity> selectUserList() {

        return this.userService.selectUserList();
    }



}