package com.sugon.iris.sugoncommon.SSHRemote;

import com.sugon.iris.sugoncommon.publicUtils.PublicUtils;
import java.util.Properties;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;

public class SSHConfig {

    private Session session;

    public SSHConfig() throws Exception{

       String userName = PublicUtils.getConfigMap().get("linux.userName");
       String password = PublicUtils.getConfigMap().get("linux.password");
       String ipAddress = PublicUtils.getConfigMap().get("linux.ipAddress");
       int port =Integer.parseInt(PublicUtils.getConfigMap().get("linux.port"));

       JSch jSch = new JSch();
       Session session = null;
       try {
           session = jSch.getSession(userName, ipAddress, port);
           session.setPassword(password);
           Properties config = new Properties();
           config.put("StrictHostKeyChecking", "no");
           session.setConfig(config);// 为Session对象设置properties
           session.setTimeout(300000);// 设置超时
           session.connect();//// 通过Session建立连接
       }catch (JSchException e){
           throw new Exception(
                   "主机登录失败, IP = " + ipAddress + ", USERNAME = " + userName + ", Exception:" + e.getMessage());
       }
       this.session = session ;
    }

    public SSHConfig(String userName,String password,String ipAddress,int port) throws Exception{
        JSch jSch = new JSch();
        Session session = null;
        try {
            session = jSch.getSession(userName, ipAddress, port);
            session.setPassword(password);
            Properties config = new Properties();
            config.put("StrictHostKeyChecking", "no");
            session.setConfig(config);// 为Session对象设置properties
            session.setTimeout(300000);// 设置超时
            session.connect();//// 通过Session建立连接
        }catch (JSchException e){
            throw new Exception(
                    "主机登录失败, IP = " + ipAddress + ", USERNAME = " + userName + ", Exception:" + e.getMessage());
        }
        this.session = session ;
    }

    public Session getSession() {
        return session;
    }
}
