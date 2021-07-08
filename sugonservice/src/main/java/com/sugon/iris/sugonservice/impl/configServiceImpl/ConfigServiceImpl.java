package com.sugon.iris.sugonservice.impl.configServiceImpl;

import com.sugon.iris.sugoncommon.publicUtils.PublicUtils;
import com.sugon.iris.sugondata.jdbcTemplate.intf.config.ConfigServiceDao;
import com.sugon.iris.sugondomain.beans.configBeans.ConfigBean;
import com.sugon.iris.sugondomain.dtos.configDtos.ConfigDto;
import com.sugon.iris.sugondomain.entities.jdbcTemplateEntity.configEntities.ConfigEntity;
import com.sugon.iris.sugonservice.service.configService.ConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.sugon.iris.sugondomain.beans.baseBeans.Error;
import java.util.ArrayList;
import java.util.List;

/**
 * 作者:SharpChan
 * 日期：2020.08.19
 * 描述：用于配置系统参数
 */
@Service
public class ConfigServiceImpl implements ConfigService {

    @Autowired
    private ConfigServiceDao configServiceDaoIntfImpl;

    /**
     * 作者:SharpChan
     * 日期：2020.08.19
     * 描述：用于获取所有的配置信息
     */
    public List<ConfigBean> getAllConfigs(List<Error> errorList) throws IllegalAccessException {
        List<ConfigBean> configBeanList = new ArrayList<ConfigBean>();
        List<ConfigEntity>  configEntityList = configServiceDaoIntfImpl.getAllConfig(errorList);
        ConfigBean configBean = null;
        for(ConfigEntity configEntity : configEntityList){
            configBean = new ConfigBean();
            PublicUtils.trans(configEntity,configBean);
            configBeanList.add(configBean);
        }
        return configBeanList;
    }

    /**
     * 作者:SharpChan
     * 日期：2020.08.19
     * 描述：用于更新配置信息
     */
    public int updateConfig(ConfigDto configDto, List<Error> errorList) throws IllegalAccessException {
        ConfigEntity configEntity = new ConfigEntity();
        PublicUtils.trans(configDto,configEntity);
        int result = configServiceDaoIntfImpl.updateConfig(configEntity,errorList);
        return result;
    }

    public int saveConfig(ConfigDto configDto, List<Error> errorList) throws IllegalAccessException {
        ConfigEntity configEntity = new ConfigEntity();
        PublicUtils.trans(configDto,configEntity);
        int result = configServiceDaoIntfImpl.saveConfig(configEntity,errorList);
        return result;
    }

    public int deleteConfig(Long id, List<Error> errorList){
       return configServiceDaoIntfImpl.deleteConfig(id,errorList);
    }
}
