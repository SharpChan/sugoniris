package com.sugon.iris.sugondata.mybaties.mapper.db2;


import com.sugon.iris.sugondomain.entities.mybatiesEntity.db2.FileRinseRegularEntity;

public interface FileRinseRegularMapper {
    int deleteByPrimaryKey(Long id);

    int insert(FileRinseRegularEntity record);

    int insertSelective(FileRinseRegularEntity record);

    FileRinseRegularEntity selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(FileRinseRegularEntity record);

    int updateByPrimaryKey(FileRinseRegularEntity record);
}