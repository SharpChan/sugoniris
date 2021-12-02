package com.sugon.iris.sugonfilerest.FileData2Mpp;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Properties;
import org.postgresql.copy.CopyManager;
import org.postgresql.core.BaseConnection;
import java.io.StringReader;

public class Copy_test {

    /**
     * @param args
     */

    public static void main(String[] args) {
        // TODO Auto-generated method stub
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            System.out.println("can not find Driver");
            e.printStackTrace();
        }

        String url = "jdbc:postgresql://192.168.217.137:20001/postgres";

        Properties props = new Properties();
        props.setProperty("user", "gaussdb");
        props.setProperty("password", "Shuguang_szga");

        try {
            Connection con = DriverManager.getConnection(url, props);
            con.setAutoCommit(false);

            CopyManager cm = null;
            StringReader sr = null;
            String quote = "$";
            String delimter = "|";

            String sql = "copy aaa from STDIN with (format 'CSV', delimiter '|', quote '$')";
            System.out.println(sql);
            cm = new CopyManager((BaseConnection) con);
            StringBuffer tuples = new StringBuffer();
            for (int j = 0; j < 3; j++) {
                tuples.append(quote + "1" + quote + delimter + quote + "a" + quote+ delimter + quote + "" + quote+"\n");
            }
            sr = new StringReader(tuples.toString());
            long rows = cm.copyIn(sql, sr);// 执行copy入库
            con.commit();// 提交
            con.close();
        } catch (Exception se) {
            se.printStackTrace();
        }
    }
}