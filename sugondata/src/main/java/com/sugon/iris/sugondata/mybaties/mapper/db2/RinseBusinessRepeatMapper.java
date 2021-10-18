package com.sugon.iris.sugondata.mybaties.mapper.db2;

import com.sugon.iris.sugondomain.entities.mybatiesEntity.db2.RinseBusinessRepeatEntity;
import java.util.List;

public interface RinseBusinessRepeatMapper {

    int saveRinseBusinessRepeat(RinseBusinessRepeatEntity rinseBusinessRepeatEntity4Sql);

    List<RinseBusinessRepeatEntity> getRinseBusinessRepeatListByTemplateId(Long fileTemplateId);

    int deleteByPrimaryKey(Long id);
}
