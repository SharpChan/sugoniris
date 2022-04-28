package com.sugon.iris.sugondomain.enums;

public enum UserType_Enum {

    USER_Type_01("Z","其他"),
    USER_Type_02("R","危化品运输"),
    USER_Type_03("Q","其他校车"),
    USER_Type_04("P","小学生校车"),
    USER_Type_05("O","幼儿校车"),
    USER_Type_06("N","教练"),
    USER_Type_07("M","出租转非"),
    USER_Type_08("L","营转非"),
    USER_Type_09("K","工抢险"),
    USER_Type_10("J","救护"),
    USER_Type_11("I","消防"),
    USER_Type_12("H","警用"),
    USER_Type_13("G","租赁"),
    USER_Type_14("F","货运"),
    USER_Type_15("E","旅游客运"),
    USER_Type_16("D","出租客运"),
    USER_Type_17("C","公交客运"),
    USER_Type_18("B","公路客运"),
    USER_Type_19("A","非营运");

    private String zddm;
    private String zdmc;

    private UserType_Enum(String zddm, String zdmc){
        this.zddm = zddm;
        this.zdmc = zdmc;
    }

    public String getZddm(){
        return zddm;
    }

    public String getZdmc(){
        return zdmc;
    }


    public static UserType_Enum getZdmc(String zddm){
        for(UserType_Enum c: UserType_Enum.values()){
            if(c.getZddm().equals(zddm)){
                return c;
            }
        }
        return null;
    }

}
