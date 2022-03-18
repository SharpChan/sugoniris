package com.sugon.iris.sugonweb.dataSync;

import com.sugon.iris.sugondata.jdbcTemplate.intf.system.AccountServiceDao;
import com.sugon.iris.sugondomain.beans.baseBeans.Error;
import com.sugon.iris.sugondomain.entities.jdbcTemplateEntity.systemEntities.UserEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import java.util.ArrayList;
import java.util.List;


@WebListener
@Configuration
@EnableScheduling   // 2.开启定时任务
public class JianMoListener implements ServletContextListener {

    @Autowired
    private AccountServiceDao accountServiceDaoImpl;





    //springboot启动时执行
    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        WebApplicationContextUtils.getRequiredWebApplicationContext(servletContextEvent.getServletContext())
                .getAutowireCapableBeanFactory().autowireBean(this);
        try {


        }catch (Exception e){
            e.printStackTrace();
        }
    }


    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        // TODO Auto-generated method stub

    }

    @Scheduled(fixedRate=10000)
  public void getConfigBean() throws Exception{

        try {
            List<Error> errorList = new ArrayList<>();
            //获取该平台的所有分配（经侦）角色的用户
            List<UserEntity> userEntityList = accountServiceDaoImpl.getUserEntityIsEconomic(errorList);
            //获取建模平台的所有用户


        }catch (Exception e) {
            e.printStackTrace ();
        }
  }

}
