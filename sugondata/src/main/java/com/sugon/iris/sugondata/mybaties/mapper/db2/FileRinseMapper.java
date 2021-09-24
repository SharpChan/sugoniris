package com.sugon.iris.sugondata.mybaties.mapper.db2;


import com.sugon.iris.sugondomain.entities.mybatiesEntity.db2.FileRinseEntity;

import java.util.List;

public interface FileRinseMapper {

    int deleteByPrimaryKey(Long id);

    int insert(FileRinseEntity fileRinseEntity4Sql);

    int insertSelective(FileRinseEntity fileRinseEntity4Sql);

    FileRinseEntity selectByPrimaryKey(Long id);

    List<FileRinseEntity>  getFileRinsesByUserId(Long userId);

    int updateByPrimaryKeySelective(FileRinseEntity fileRinseEntity4Sql);

    int updateByPrimaryKey(FileRinseEntity fileRinseEntity4Sql);
}