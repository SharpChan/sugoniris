package com.sugon.iris.sugonservice.service.configService;

import com.sugon.iris.sugondomain.beans.baseBeans.Error;
import com.sugon.iris.sugondomain.beans.configBeans.ConfigBean;
import com.sugon.iris.sugondomain.dtos.configDtos.ConfigDto;

import java.util.List;

public interface ConfigService {

    List<ConfigBean> getAllConfigs(List<Error> errorList) throws IllegalAccessException;

    int updateConfig(ConfigDto configDto, List<Error> errorList) throws IllegalAccessException;

    int saveConfig(ConfigDto configDto, List<Error> errorList) throws IllegalAccessException;

    int deleteConfig(Long id, List<Error> errorList);
}
