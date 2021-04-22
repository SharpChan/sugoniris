package com.sugon.iris.sugondata.mybaties.mapper.db1;


import com.sugon.iris.sugondomain.entities.mybatiesEntity.db1.JmUserEntity;

import java.util.List;

public interface ModelManagerShareMapper {

    List<JmUserEntity>  findPublicUserList(String modelId);

    JmUserEntity findPrivateUser(String modelId);

    JmUserEntity findCreateUser(String modelId);
}
