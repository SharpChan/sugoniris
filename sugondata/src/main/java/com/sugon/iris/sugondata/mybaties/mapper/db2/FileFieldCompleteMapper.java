package com.sugon.iris.sugondata.mybaties.mapper.db2;

import com.sugon.iris.sugondomain.entities.mybatiesEntity.db2.FileFieldCompleteEntity;

import java.util.List;

public interface FileFieldCompleteMapper {

    FileFieldCompleteEntity selectFileFieldCompleteByPrimaryKey(Long id);

    List<FileFieldCompleteEntity> selectFileFieldCompleteList(FileFieldCompleteEntity fileFieldCompleteEntity);

    int fileFieldCompleteInsert(FileFieldCompleteEntity fileFieldCompleteEntity);

    int deleteFieldCompleteById(Long id);

    List<FileFieldCompleteEntity> selectFileFieldCompleteByTemplateGroupId(Long templateGroupId);

    int updateByPrimaryKey(FileFieldCompleteEntity fileFieldCompleteEntity);
}
