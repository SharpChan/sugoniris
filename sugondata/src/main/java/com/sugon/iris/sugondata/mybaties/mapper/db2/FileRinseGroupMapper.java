package com.sugon.iris.sugondata.mybaties.mapper.db2;


import com.sugon.iris.sugondomain.entities.mybatiesEntity.db2.FileRinseGroupEntity;

import java.util.List;

public interface FileRinseGroupMapper {

    int deleteByPrimaryKey(Long id);

    int insert(FileRinseGroupEntity fileRinseEntity4Sql);

    int insertSelective(FileRinseGroupEntity fileRinseEntity4Sql);

    FileRinseGroupEntity selectByPrimaryKey(Long id);

    List<FileRinseGroupEntity>  getFileRinsesByUserId(Long userId);

    int updateByPrimaryKeySelective(FileRinseGroupEntity fileRinseEntity4Sql);

    int updateByPrimaryKey(FileRinseGroupEntity fileRinseEntity4Sql);
}