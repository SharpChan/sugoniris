package com.sugon.iris.sugondata.mybaties.mapper.db2;

import com.sugon.iris.sugondomain.entities.mybatiesEntity.db2.RinseBusinessPrefixEntity;

import java.util.List;

public interface RinseBusinessPrefixMapper {

    int saveRinseBusinessPrefix(RinseBusinessPrefixEntity rinseBusinessPrefixEntity4Sql);

    List<RinseBusinessPrefixEntity> getRinseBusinessPrefixListByTemplateId(Long fileTemplateId);

    int deleteByPrimaryKey(Long id);
}
