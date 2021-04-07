package com.sugon.iris.sugondomain.beans.modelInfos;

import com.sugon.iris.sugondomain.beans.moChuangGongchang.JmUserBean;
import lombok.Data;

import java.util.List;

@Data
public class UsersBean {

    List<JmUserBean> publicUserList;

    List<JmUserBean> privateUserList;
}
