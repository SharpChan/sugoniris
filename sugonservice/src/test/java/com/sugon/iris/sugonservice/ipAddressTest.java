package com.sugon.iris.sugonservice;

import com.sugon.iris.sugoncommon.ipAddress.IPSeeker;
import junit.framework.TestCase;
public class ipAddressTest extends TestCase{

    public void testIp(){

        //指定纯真数据库的文件名，所在文件夹
        IPSeeker ip=new IPSeeker("qqwry.dat","src/main/resources");

        //测试IP 58.20.43.13
        System.out.println(ip.getIPLocation("36.152.127.18").getCountry()+":"+ip.getIPLocation("36.152.127.18").getArea());
    }
}
