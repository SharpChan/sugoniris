package com.sugon.iris.sugondata.jdbcTemplate.intf.system;

import com.sugon.iris.sugondomain.entities.jdbcTemplateEntity.systemEntities.UserEntity;
import com.sugon.iris.sugondomain.beans.baseBeans.Error;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;

public interface AccountServiceDao {

    List<UserEntity> getUserEntitys (Long id,String userName,String password,Integer flag,String policeno, List<Error> errorList);

    List<UserEntity> getUserEntitysForCheck(String keyWord,Integer flag, List<Error> errorList);

    int insertAccount(UserEntity user, List<Error> errorList);

    int update (UserEntity user, List<Error> errorList);

    Long getUser_seq(List<Error> errorList);

    int deleteUser(Long id, List<Error> errorList);

    void initSeq();

    int updateForCheck (Long id,Integer flag, List<Error> errorList);

    JdbcTemplate getJdbcTemplate();

}
