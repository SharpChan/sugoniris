package com.sugon.iris.sugondata.mybaties.mapper.db2;

import com.sugon.iris.sugondomain.entities.mybatiesEntity.db2.RinseBusinessSuffixEntity;
import java.util.List;

public interface RinseBusinessSuffixMapper {

    int saveRinseBusinessSuffix(RinseBusinessSuffixEntity rinseBusinessSuffixEntity4Sql);

    List<RinseBusinessSuffixEntity> getRinseBusinessSuffixListByTemplateId(Long fileTemplateId);

    int deleteByPrimaryKey(Long id);
}
