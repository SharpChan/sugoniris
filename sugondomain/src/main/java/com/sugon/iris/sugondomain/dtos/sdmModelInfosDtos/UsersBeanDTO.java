package com.sugon.iris.sugondomain.dtos.sdmModelInfosDtos;

import lombok.Data;
import com.sugon.iris.sugondomain.dtos.moChuangGongchangDtos.*;

import java.util.List;

@Data
public class UsersBeanDTO {

    /**
     *公共用户
     */
    private List<JmUserBeanDTO> publicUserList;

    /**
     *私有用户
     */
    private JmUserBeanDTO privateUser;

    /**
     *创建者
     */
    private JmUserBeanDTO createUser;

}
