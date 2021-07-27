package com.sugon.iris.sugondata.mybaties.mapper.db2;

import com.sugon.iris.sugondomain.entities.mybatiesEntity.db2.FileTableEntity;

import java.util.List;

public interface FileTableMapper {

    String selectTableName(FileTableEntity fileTableEnity);

    Integer saveFileTable(FileTableEntity fileTableEntity);

    List<FileTableEntity> findFileTableList(FileTableEntity fileTableEntity);

    Integer deleteFileTableByCaseId(String[] idArr);

    List<FileTableEntity>   findAllFileTablesByUserId(Long userId);
}
