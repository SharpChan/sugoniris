package com.sugon.iris.sugondomain.enums;

public enum CertificateEnum {

    Certificate_TYpe_01("A","居民身份证"),
    Certificate_TYpe_02("B","组织机构代码证书"),
    Certificate_TYpe_03("C","军官证"),
    Certificate_TYpe_04("D","士兵证"),
    Certificate_TYpe_05("E","军官离退休证"),
    Certificate_TYpe_06("F","境外人员身份证明"),
    Certificate_TYpe_07("G","外交人员身份证明"),
    Certificate_TYpe_08("H","居民户口簿"),
    Certificate_TYpe_09("J","单位注销证明"),
    Certificate_TYpe_10("K","居住暂住证明"),
    Certificate_TYpe_11("L","驻华机构证明"),
    Certificate_TYpe_12("M","临时居民身份证"),
    Certificate_TYpe_13("N","统一社会信用代码"),
    Certificate_TYpe_14("P","个体工商户营业执照注册号"),
    Certificate_TYpe_15("Z","其他证件");

    private String zddm;
    private String zdmc;

    private CertificateEnum(String zddm, String zdmc){
        this.zddm = zddm;
        this.zdmc = zdmc;
    }

    public String getZddm(){
        return zddm;
    }

    public String getZdmc(){
        return zdmc;
    }


    public static CertificateEnum getZdmc(String zddm){
        for(CertificateEnum c: CertificateEnum.values()){
            if(c.getZddm().equals(zddm)){
                return c;
            }
        }
        return null;
    }
}
