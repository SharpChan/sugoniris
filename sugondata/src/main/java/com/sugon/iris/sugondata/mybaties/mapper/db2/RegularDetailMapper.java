package com.sugon.iris.sugondata.mybaties.mapper.db2;

import com.sugon.iris.sugondomain.entities.mybatiesEntity.db2.RegularDetailEntity;

import java.util.List;

public interface RegularDetailMapper {
    int deleteByPrimaryKey(Long id);

    int deleteByGroupId(Long groupId);

    int insert(RegularDetailEntity regularDetailEntity4Sql);

    int insertSelective(RegularDetailEntity regularDetailEntity4Sql);

    List<RegularDetailEntity> selectByGroupId(Long id);

    List<String> selectByUserId(Long userId);

    int updateByPrimaryKeySelective(RegularDetailEntity regularDetailEntity4Sql);

}