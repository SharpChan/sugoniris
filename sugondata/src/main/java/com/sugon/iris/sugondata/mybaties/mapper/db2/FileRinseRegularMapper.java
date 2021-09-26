package com.sugon.iris.sugondata.mybaties.mapper.db2;


import com.sugon.iris.sugondomain.entities.mybatiesEntity.db2.FileRinseRegularEntity;

import java.util.List;

public interface FileRinseRegularMapper {
    int deleteByPrimaryKey(Long id);

    int deleteByFileRinseDetailId(Long fileRinseDetailId);

    int insert(FileRinseRegularEntity record);

    int insertSelective(FileRinseRegularEntity record);

    FileRinseRegularEntity selectByPrimaryKey(Long id);

    List<FileRinseRegularEntity> selectByFileRinseDetailId(Long fileRinseDetailId);

    int updateByPrimaryKeySelective(FileRinseRegularEntity record);

    int updateByPrimaryKey(FileRinseRegularEntity record);
}