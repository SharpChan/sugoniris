package com.sugon.iris.sugondata.mybaties.mapper.db2;

import com.sugon.iris.sugondomain.entities.mybatiesEntity.db2.FileDataGroupEntity;

import java.util.List;

public interface FileDataGroupMapper {

    List<FileDataGroupEntity> findFileDataGroups(FileDataGroupEntity fileDataGroupEntity);

    Integer saveFileDataGroup(FileDataGroupEntity fileDataGroupEntity);

    Integer updateFileDataGroup(FileDataGroupEntity fileDataGroupEntity);

    Integer deleteFileDataGroup(Long id);


}
