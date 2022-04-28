package com.sugon.iris.sugondata.mybaties.mapper.db1;

import com.sugon.iris.sugondomain.entities.mybatiesEntity.db1.JM_t_model_local_tableEntity;

import java.util.List;

public interface JM_t_model_local_tableMapper {

   List<JM_t_model_local_tableEntity> queryAllModelLocalTableByUserId(Integer userId,String areaType);

   Integer batchTModelLocalTableInsert(List<JM_t_model_local_tableEntity> jM_t_model_local_tableEntityList);

   Integer deleteTModelLocalTableByIds(List<Integer> ids);
}
