package com.sugon.iris.sugondata.mybaties.mapper.db2;

import com.sugon.iris.sugondomain.entities.mybatiesEntity.db2.RinseBusinessPhoneEntity;
import java.util.List;

public interface RinseBusinessPhoneMapper {

    int saveRinseBusinessPhone(RinseBusinessPhoneEntity rinseBusinessPhoneEntity4Sql);

    List<RinseBusinessPhoneEntity> getRinseBusinessPhoneListByTemplateId(Long fileTemplateId);

    List<RinseBusinessPhoneEntity> getAllRinseBusinessPhoneList();

    int deleteByPrimaryKey(Long id);
}
