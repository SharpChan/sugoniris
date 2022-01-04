package com.sugon.iris.sugondata.mybaties.mapper.db2;

import com.sugon.iris.sugondomain.entities.mybatiesEntity.db2.RinseBusinessIdCardNoEntity;
import java.util.List;

public interface RinseBusinessIdCardNoMapper {

    int saveRinseBusinessIdCardNo(RinseBusinessIdCardNoEntity rinseBusinessIdCardNoEntity4Sql);

    List<RinseBusinessIdCardNoEntity> getRinseBusinessIdCardNoListByTemplateId(Long fileTemplateId);

    List<RinseBusinessIdCardNoEntity> getAllRinseBusinessIdCardNoList();

    int deleteByPrimaryKey(Long id);
}
