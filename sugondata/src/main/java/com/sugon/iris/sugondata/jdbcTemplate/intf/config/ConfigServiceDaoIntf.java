package com.sugon.iris.sugondata.jdbcTemplate.intf.config;

import com.sugon.iris.sugondomain.entities.jdbcTemplateEntity.configEntities.ConfigEntity;
import com.sugon.iris.sugondomain.beans.baseBeans.Error;

import java.util.List;

public interface ConfigServiceDaoIntf {

    List<ConfigEntity> getAllConfig(List<Error> errorList);

    int  updateConfig(ConfigEntity configEntity,List<Error> errorList);

    int  saveConfig(ConfigEntity configEntity,List<Error> errorList);

    int deleteConfig(Long id, List<Error> errorList);
}
