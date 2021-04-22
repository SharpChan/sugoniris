package com.sugon.iris.sugondomain.dtos.sdmModelInfosDtos;

import lombok.Data;
import com.sugon.iris.sugondomain.dtos.moChuangGongchangDtos.*;

import java.util.List;

@Data
public class UsersBeanDTO {

    private List<JmUserBeanDTO> publicUserList;

    private JmUserBeanDTO privateUser;

    private JmUserBeanDTO createUser;

}
