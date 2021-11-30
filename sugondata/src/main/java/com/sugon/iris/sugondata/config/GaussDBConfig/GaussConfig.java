package com.sugon.iris.sugondata.config.GaussDBConfig;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import java.sql.Connection;
import java.sql.DriverManager;
import org.springframework.context.annotation.Primary;

@Configuration
public class GaussConfig {

    @Value("datasource.druid.db4.driverClassName")
    private String driverClassName;

    @Value("datasource.druid.db4.username")
    private String username;

    @Value("datasource.druid.db4.password")
    private String password;

    @Value("datasource.druid.db4.jdbc-url")
    private String jdbcUrl;



    @Primary
    @Bean(name = "GaussDbConnection")
   public Connection  getMppConfig(){
       Connection conn = null;
       try {
           Class.forName(driverClassName);
           conn = DriverManager.getConnection(jdbcUrl, username, password);
       }catch (Exception e){
           e.printStackTrace();
       }
       return conn;
   }
}
