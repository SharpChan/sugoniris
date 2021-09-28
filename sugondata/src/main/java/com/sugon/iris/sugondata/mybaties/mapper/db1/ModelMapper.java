package com.sugon.iris.sugondata.mybaties.mapper.db1;

import com.sugon.iris.sugondomain.entities.mybatiesEntity.db1.ModelEntity;

import java.util.List;

public interface ModelMapper {

    /**
     * 获取所有的模型信息
     */
    List<ModelEntity> selectModelList();

    /**
     *通过id获取一个模型
     */
    ModelEntity findOneByUserNameAndModelId(String policeNo, String ModelId);

    List<ModelEntity> findPrivateByUserNameAndModelId(String policeNo);

    List<ModelEntity> findBublicByUserNameAndModelId(String policeNo);

}
