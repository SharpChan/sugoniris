package com.sugon.iris.sugondomain.enums;

public enum Sex_Enum {
    SEX_01("1","男"),
    SEX_02("2","女");

    private String zddm;
    private String zdmc;

    private Sex_Enum(String zddm, String zdmc){
        this.zddm = zddm;
        this.zdmc = zdmc;
    }

    public String getZddm(){
        return zddm;
    }

    public String getZdmc(){
        return zdmc;
    }


    public static Sex_Enum getZdmc(String zddm){
        for(Sex_Enum c: Sex_Enum.values()){
            if(c.getZddm().equals(zddm)){
                return c;
            }
        }
        return null;
    }
}
