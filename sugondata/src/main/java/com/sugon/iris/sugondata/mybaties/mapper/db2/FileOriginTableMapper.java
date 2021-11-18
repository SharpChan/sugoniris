package com.sugon.iris.sugondata.mybaties.mapper.db2;


import com.sugon.iris.sugondomain.entities.mybatiesEntity.db2.FileOriginTableEntity;
import java.util.List;

public interface FileOriginTableMapper {

    List<FileOriginTableEntity> findFileOriginTableList(FileOriginTableEntity fileOriginTableEntity);

    Integer saveFileOriginTable(FileOriginTableEntity fileOriginTableEntity);

    Integer deleteFileOriginTableByCaseId(String[] idArr);

}
