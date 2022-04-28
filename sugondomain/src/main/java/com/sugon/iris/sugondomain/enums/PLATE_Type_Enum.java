package com.sugon.iris.sugondomain.enums;

public enum PLATE_Type_Enum {

    PLATE_TYPE_01("1","大型汽车"),
    PLATE_TYPE_01_01("01","大型汽车"),
    PLATE_TYPE_02("2","小型汽车"),
    PLATE_TYPE_02_02("02","小型汽车"),
    PLATE_TYPE_03("3","使馆汽车"),
    PLATE_TYPE_04("4","领馆汽车"),
    PLATE_TYPE_05("5","境外汽车"),
    PLATE_TYPE_06("6","外籍汽车"),
    PLATE_TYPE_07("7","两、三轮摩托车"),
    PLATE_TYPE_08("8","轻便摩托车"),
    PLATE_TYPE_09("9","使馆摩托车"),
    PLATE_TYPE_10("10","领馆摩托车"),
    PLATE_TYPE_11("11","境外摩托车"),
    PLATE_TYPE_12("12","外籍摩托车"),
    PLATE_TYPE_13("13","农用运输车类"),
    PLATE_TYPE_14("14","拖拉机"),
    PLATE_TYPE_15("15","挂车"),
    PLATE_TYPE_16("16","教练汽车"),
    PLATE_TYPE_17("17","教练摩托车"),
    PLATE_TYPE_18("18","试验汽车"),
    PLATE_TYPE_19("19","试验摩托车"),
    PLATE_TYPE_20("20","临时入境汽车"),
    PLATE_TYPE_21("21","临时入境摩托车"),
    PLATE_TYPE_22("22","临时行驶车"),
    PLATE_TYPE_23("23","警用汽车号牌"),
    PLATE_TYPE_24("24","警用摩托车号牌"),
    PLATE_TYPE_25("25","军用车辆号牌"),
    PLATE_TYPE_26("26","香港入出境车号牌"),
    PLATE_TYPE_27("27","澳门入出境车号牌"),
    PLATE_TYPE_28("31","武警号牌"),
    PLATE_TYPE_29("32","军队号牌"),
    PLATE_TYPE_30("99","其他号牌"),
    PLATE_TYPE_03_03("03","使馆汽车"),
    PLATE_TYPE_04_04("04","领馆汽车"),
    PLATE_TYPE_05_05("05","境外汽车"),
    PLATE_TYPE_06_06("06","外籍汽车"),
    PLATE_TYPE_07_07("07","两、三轮摩托车"),
    PLATE_TYPE_08_08("08","轻便摩托车"),
    PLATE_TYPE_09_09("09","使馆摩托车");



    private String zddm;
    private String zdmc;

    private PLATE_Type_Enum(String zddm, String zdmc){
        this.zddm = zddm;
        this.zdmc = zdmc;
    }

    public String getZddm(){
        return zddm;
    }

    public String getZdmc(){
        return zdmc;
    }


    public static PLATE_Type_Enum getZdmc(String zddm){
        for(PLATE_Type_Enum c: PLATE_Type_Enum.values()){
            if(c.getZddm().equals(zddm)){
                return c;
            }
        }
        return null;
    }
}
