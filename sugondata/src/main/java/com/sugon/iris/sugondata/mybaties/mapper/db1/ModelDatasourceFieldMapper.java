package com.sugon.iris.sugondata.mybaties.mapper.db1;



import com.sugon.iris.sugondomain.entities.mybatiesEntity.db1.ResultColumnEntity;

import java.util.List;

public interface ModelDatasourceFieldMapper {

    List<ResultColumnEntity>  getTableAliasByTableId(String tableId);
}
