package com.sugon.iris.sugondata.mybaties.mapper.db2;

import com.sugon.iris.sugondomain.entities.mybatiesEntity.db2.FileCaseEntity;

import java.util.List;

public interface FileCaseMapper {

    List<FileCaseEntity> selectFileCaseEntityList(FileCaseEntity fileCaseEntity);

    int fileCaseInsert(FileCaseEntity fileCaseEntity);

    int updateFileCase(FileCaseEntity fileCaseEntity);

    int deleteFileCaseById(String[] idArr);
}
