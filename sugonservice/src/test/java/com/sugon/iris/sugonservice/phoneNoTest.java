package com.sugon.iris.sugonservice;

import com.sugon.iris.sugoncommon.ipAddress.IPSeeker;
import com.sugon.iris.sugoncommon.phoneNo.PhoneUtil;
import junit.framework.TestCase;

public class phoneNoTest extends TestCase{

    public void testPhoneNO() throws Exception{

        // {"number":"85268476749","regionCode":"HK","nationalNumber":68476749,"countryCode":852,"description":"香港","fullNumber":"+85268476749"}
        //System.out.println(PhoneUtil.getPhoneNumberInfo("+85268476749").toJSONString());

        // {"number":"16467879865","regionCode":"US","nationalNumber":6467879865,"countryCode":1,"description":"美国","fullNumber":"+16467879865"}
        //System.out.println(PhoneUtil.getPhoneNumberInfo("+16467879865").toJSONString());

        System.out.println(PhoneUtil.getDescription("18618761707610"));
    }
}
