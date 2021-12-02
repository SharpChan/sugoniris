package com.sugon.iris.sugondata.config.GaussDBConfig;

import com.sugon.iris.sugoncommon.publicUtils.PublicUtils;
import lombok.Data;

import java.sql.Connection;
import java.sql.DriverManager;


@Data
public class GaussConnection {

    private   Connection connection;

    public  GaussConnection(){

         String driverClassName = PublicUtils.getConfigMap().get("gauss-driverClassName");


         String username = PublicUtils.getConfigMap().get("gaussdb-username");


         String password = PublicUtils.getConfigMap().get("gaussdb-password");

         String jdbcUrl = PublicUtils.getConfigMap().get("jdbcUrl");

         Connection conn = null;
        try {
           Class.forName(driverClassName);
           conn = DriverManager.getConnection(jdbcUrl, username, password);
           conn.setAutoCommit(false);
       }catch (Exception e){
           e.printStackTrace();
       }
       this.connection = conn;
   }
}
