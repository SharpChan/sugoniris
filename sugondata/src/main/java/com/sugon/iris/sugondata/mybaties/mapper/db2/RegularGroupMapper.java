package com.sugon.iris.sugondata.mybaties.mapper.db2;


import com.sugon.iris.sugondomain.entities.mybatiesEntity.db2.RegularGroupEntity;

import java.util.List;

public interface RegularGroupMapper {
    int deleteByPrimaryKey(Long id);

    int insert(RegularGroupEntity regularGroupEntity4Sql);

    int insertSelective(RegularGroupEntity regularGroupEntity4Sql);

    RegularGroupEntity selectByPrimaryKey(Long id);

    List<RegularGroupEntity> selectGroupList();

    int updateByPrimaryKeySelective(RegularGroupEntity regularGroupEntity4Sql);
}