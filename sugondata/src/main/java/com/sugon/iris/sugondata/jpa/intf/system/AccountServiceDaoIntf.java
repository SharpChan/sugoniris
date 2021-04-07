package com.sugon.iris.sugondata.jpa.intf.system;

import com.sugon.iris.sugondomain.entities.userEntities.UserEntity;
import com.sugon.iris.sugondomain.beans.baseBeans.Error;
import java.util.List;

public interface AccountServiceDaoIntf {

    List<UserEntity> getUserEntitys(Long id, String email, String password, Integer flag, List<Error> errorList);

    int insertAccount(UserEntity user, List<Error> errorList);

    int update(UserEntity user, List<Error> errorList);

    Long getUser_seq(List<Error> errorList);

    int deleteUser(UserEntity user, List<Error> errorList);

    void initUserSeq();


}
