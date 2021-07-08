package com.sugon.iris.sugonlistener.table;

import com.sugon.iris.sugondata.jdbcTemplate.intf.system.TableInitServiceDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

@WebListener
public class TableListener implements ServletContextListener {

    @Autowired
    private TableInitServiceDao TableInitServiceDaoImpl;


    //项目启动执行,检查数据库有没有少表，少表的话自动添加
    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        WebApplicationContextUtils.getRequiredWebApplicationContext(servletContextEvent.getServletContext())
                .getAutowireCapableBeanFactory().autowireBean(this);

        TableInitServiceDaoImpl.createTables();
    }


    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        // TODO Auto-generated method stub

    }
}
