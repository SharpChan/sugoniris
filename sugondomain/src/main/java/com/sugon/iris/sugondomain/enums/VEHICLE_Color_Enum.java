package com.sugon.iris.sugondomain.enums;

public enum VEHICLE_Color_Enum {

    VEHICLE_Color_01("Z","其他"),
    VEHICLE_Color_02("A","白"),
    VEHICLE_Color_03("B","灰"),
    VEHICLE_Color_04("C","黄"),
    VEHICLE_Color_05("D","粉"),
    VEHICLE_Color_06("E","红"),
    VEHICLE_Color_07("F","紫"),
    VEHICLE_Color_08("G","绿"),
    VEHICLE_Color_09("H","蓝"),
    VEHICLE_Color_10("I","棕"),
    VEHICLE_Color_11("J","黑");

    private String zddm;
    private String zdmc;

    private VEHICLE_Color_Enum(String zddm, String zdmc){
        this.zddm = zddm;
        this.zdmc = zdmc;
    }

    public String getZddm(){
        return zddm;
    }

    public String getZdmc(){
        return zdmc;
    }


    public static VEHICLE_Color_Enum getZdmc(String zddm){
        for(VEHICLE_Color_Enum c: VEHICLE_Color_Enum.values()){
            if(c.getZddm().equals(zddm)){
                return c;
            }
        }
        return null;
    }
}
