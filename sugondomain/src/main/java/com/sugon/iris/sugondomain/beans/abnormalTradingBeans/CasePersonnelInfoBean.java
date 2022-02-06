package com.sugon.iris.sugondomain.beans.abnormalTradingBeans;

import lombok.Data;

@Data
public class CasePersonnelInfoBean {

    /**
     * 人员姓名
     */
    private String accountName;

    /**
     * 人员证件号
     */
    private String idNo;

    /**
     * 卡号
     */
    private String cardId;

    /**
     * 账号
     */
    private String accountNo;

    /**
     * 大额交易数量
     */
    private String blockTradeQuantity;
}
