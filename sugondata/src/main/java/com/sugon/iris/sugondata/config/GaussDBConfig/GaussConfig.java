package com.sugon.iris.sugondata.config.GaussDBConfig;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import java.sql.Connection;
import java.sql.DriverManager;
import java.io.IOException;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.sql.SQLException;
import org.postgresql.copy.CopyManager;
import org.postgresql.core.BaseConnection;
import org.springframework.context.annotation.Primary;

@Configuration
public class GaussConfig {

    @Primary
    @Bean(name = "GaussDbConnection")
   public Connection  getMppConfig(){
       String urls = new String("jdbc:postgresql://192.168.217.140:5432/iris?useUnicode=true&characterEncoding=utf-8&serverTimezone=UTC"); //数据库URL
       String username = new String("postgres");            //用户名
       String password = new String("123456");             //密码
       String driver = "org.postgresql.Driver";
       Connection conn = null;
       try {
           Class.forName(driver);
           conn = DriverManager.getConnection(urls, username, password);
       }catch (Exception e){
           e.printStackTrace();
       }
       return conn;
   }
}
