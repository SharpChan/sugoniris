package com.sugon.iris.sugondata.mybaties.mapper.db2;

import com.sugon.iris.sugondomain.entities.mybatiesEntity.db2.FileDataGroupTableEntity;

import java.util.List;

public interface FileDataGroupTableMapper {

    List<FileDataGroupTableEntity> findFileDataGroupTable(FileDataGroupTableEntity fileDataGroupTableEntitySqlParm);

    Integer saveFileDataGroupTables(List<FileDataGroupTableEntity> fileDataGroupTableEntitySqlParmList);

    Integer deleteFileDataGroupTables(List<Long> idList);

}
