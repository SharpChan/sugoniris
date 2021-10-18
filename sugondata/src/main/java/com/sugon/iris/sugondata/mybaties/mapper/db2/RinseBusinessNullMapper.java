package com.sugon.iris.sugondata.mybaties.mapper.db2;

import com.sugon.iris.sugondomain.entities.mybatiesEntity.db2.RinseBusinessNullEntity;

import java.util.List;

public interface RinseBusinessNullMapper {

    int saveRinseBusinessNull(RinseBusinessNullEntity rinseBusinessNullEntity4Sql);

    List<RinseBusinessNullEntity> getRinseBusinessNullListByTemplateId(Long fileTemplateId);

    int deleteByPrimaryKey(Long id);
}
