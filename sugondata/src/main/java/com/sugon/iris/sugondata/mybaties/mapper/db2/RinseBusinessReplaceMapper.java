package com.sugon.iris.sugondata.mybaties.mapper.db2;

import com.sugon.iris.sugondomain.entities.mybatiesEntity.db2.RinseBusinessReplaceEntity;
import java.util.List;

public interface RinseBusinessReplaceMapper {

    int saveRinseBusinessReplace(RinseBusinessReplaceEntity rinseBusinessReplaceEntity4Sql);

    List<RinseBusinessReplaceEntity> getRinseBusinessReplaceListByTemplateId(Long fileTemplateId);

    int deleteByPrimaryKey(Long id);
}
