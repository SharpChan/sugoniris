package com.sugon.iris.sugondata.mybaties.mapper.db1;

import com.sugon.iris.sugondomain.entities.mybatiesEntity.db1.JM_t_system_userEntity;
import java.util.List;

public interface JM_t_system_userMapper {

   List<JM_t_system_userEntity> queryAllJMUsers();

   Integer batchTSystemUserInsert(List<JM_t_system_userEntity> jM_t_system_userEntityList);

}
