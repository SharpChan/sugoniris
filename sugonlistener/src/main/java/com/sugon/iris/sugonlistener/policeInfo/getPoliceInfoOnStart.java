package com.sugon.iris.sugonlistener.policeInfo;

import com.sugon.iris.sugoncommon.publicUtils.PublicUtils;
import com.sugon.iris.sugondata.mybaties.mapper.db4.UserCheckMapper;
import com.sugon.iris.sugondomain.entities.mybatiesEntity.db4.PoliceInfoEntity;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.annotation.Resource;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@WebListener
@Configuration
public class getPoliceInfoOnStart implements ServletContextListener {

    @Resource
    private UserCheckMapper userCheckMapper;

    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        WebApplicationContextUtils.getRequiredWebApplicationContext(servletContextEvent.getServletContext())
                .getAutowireCapableBeanFactory().autowireBean(this);

        try {
            if ("0".equals(PublicUtils.getConfigMap().get("environment"))) {
                List<PoliceInfoEntity> policeInfoEntityList = userCheckMapper.findAllPoliceInfo();
                Map<String,PoliceInfoEntity> policeInfoMap = new HashMap<>();
                for(PoliceInfoEntity policeInfoEntity : policeInfoEntityList){
                    policeInfoMap.put(policeInfoEntity.getJinghao()+policeInfoEntity.getSfzh(),policeInfoEntity);
                }
                PublicUtils.policeInfoMap = policeInfoMap;
            }else if("1".equals(PublicUtils.getConfigMap().get("environment"))){
                List<PoliceInfoEntity> policeInfoEntityList = userCheckMapper.findAllPoliceInfoDev();
                Map<String,PoliceInfoEntity> policeInfoMap = new HashMap<>();
                for(PoliceInfoEntity policeInfoEntity : policeInfoEntityList){
                    policeInfoMap.put(policeInfoEntity.getJinghao()+policeInfoEntity.getSfzh(),policeInfoEntity);
                }
                PublicUtils.policeInfoMap = policeInfoMap;
            }
        }catch (Exception e){
            e.printStackTrace();
        }

    }


    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        // TODO Auto-generated method stub

    }

}
