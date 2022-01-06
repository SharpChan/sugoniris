package com.sugon.iris.sugonweb;

import com.sugon.iris.sugondomain.beans.baseBeans.Error;
import com.sugon.iris.sugondomain.beans.system.User;
import com.sugon.iris.sugondomain.dtos.systemDtos.UserDto;
import com.sugon.iris.sugonservice.service.systemService.AccountService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@SpringBootTest
class SugonwebApplicationTests {

    @Resource
    private AccountService accountServiceImpl;

    @Test
    void contextLoads() {
    }

    @Test
    void caLongInTest() throws IllegalAccessException {
        UserDto userDto = new UserDto();
        userDto.setPoliceNo("111111");
        userDto.setIdCard("320683198810192038");
        List<Error> errorList = new ArrayList<>();
        User user = accountServiceImpl.getUserInfoForCa(userDto,errorList);
        System.out.println(user);
    }
}
