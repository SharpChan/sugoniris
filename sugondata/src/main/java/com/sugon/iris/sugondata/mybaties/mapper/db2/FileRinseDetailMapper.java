package com.sugon.iris.sugondata.mybaties.mapper.db2;

import com.sugon.iris.sugondomain.entities.mybatiesEntity.db2.FileRinseDetailEntity;

import java.util.List;

public interface FileRinseDetailMapper {
    int deleteByPrimaryKey(Long id);

    int insert(FileRinseDetailEntity record);

    int insertSelective(FileRinseDetailEntity record);

    List<FileRinseDetailEntity> selectByGroupId(Long fileRinseGroupId);

    FileRinseDetailEntity selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(FileRinseDetailEntity record);

    int updateByPrimaryKey(FileRinseDetailEntity record);
}