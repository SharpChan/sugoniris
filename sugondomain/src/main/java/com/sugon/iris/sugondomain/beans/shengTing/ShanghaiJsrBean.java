package com.sugon.iris.sugondomain.beans.shengTing;


import lombok.Data;

@Data
public class ShanghaiJsrBean {

    /**
     *身份证明号码
     */
    private String SFZMHM;
    /**
     *初次领证日期
     */
    private String CCLZRQ;
    /**
     *初次发证机关
     */
    private String CCFZJG;
    /**
     *有效期始
     */
    private String YXQS;
    /**
     *有效期止
     */
    private String YXQZ;
    /**
     *姓名
     */
    private String XM;
    /**
     *性别1男2女
     */
    private String XB;
    /**
     *出生日期
     */
    private String CSRQ;
    /**
     *登记住所详细地址
     */
    private String DJZSXXDZ;
    /**
     *联系住所详细地址
     */
    private String LXZSXXDZ;
    /**
     *联系电话
     */
    private String LXDH;
    /**
     *手机号码
     */
    private String SJHM;
    /**
     *暂住证明
     */
    private String ZZZM;
    /**
     *暂住发证机关
     */
    private String ZZFZJG;
    /**
     *暂住发证日期
     */
    private String ZZFZRQ;
}
