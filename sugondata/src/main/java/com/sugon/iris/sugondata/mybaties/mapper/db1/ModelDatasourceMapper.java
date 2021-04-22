package com.sugon.iris.sugondata.mybaties.mapper.db1;

import com.sugon.iris.sugondomain.entities.mybatiesEntity.db1.ModelDatasourceEntity;

import java.util.List;

public interface ModelDatasourceMapper {

    List<ModelDatasourceEntity> getModelDatasourceByModelId(String modelId);
}
