package com.sugon.iris.sugondata.mybaties.mapper.db1;

import com.sugon.iris.sugondomain.entities.mybatiesEntity.db1.JmUserEntity;
import java.util.List;

public interface SdmUserMapper {

    List<JmUserEntity> selectAllUserList();

}