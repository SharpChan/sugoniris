package com.sugon.iris.sugondata.mybaties.mapper.db2;

import com.sugon.iris.sugondomain.entities.mybatiesEntity.db2.RinseBusinessIpEntity;
import java.util.List;

public interface RinseBusinessIpMapper {

    int saveRinseBusinessIp(RinseBusinessIpEntity rinseBusinessIpEntity4Sql);

    List<RinseBusinessIpEntity> getRinseBusinessIpListByTemplateId(Long fileTemplateId);

    List<RinseBusinessIpEntity> getAllRinseBusinessIpList();

    int deleteByPrimaryKey(Long id);
}
