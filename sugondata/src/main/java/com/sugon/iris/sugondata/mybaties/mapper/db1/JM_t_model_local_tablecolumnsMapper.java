package com.sugon.iris.sugondata.mybaties.mapper.db1;

import com.sugon.iris.sugondomain.beans.jmBeans.JM_t_model_local_tablecolumnsBean;
import com.sugon.iris.sugondomain.entities.mybatiesEntity.db1.JM_t_model_local_tablecolumnsEntity;
import io.swagger.models.auth.In;

import java.util.List;

public interface JM_t_model_local_tablecolumnsMapper {

    List<JM_t_model_local_tablecolumnsBean> getLocalTableColumnsBytableId();

    Integer saveLocalTableColumns(List<JM_t_model_local_tablecolumnsEntity> local_tablecolumnList);

    Integer deleteLocalTableColumns(List<Integer> idList);

}
