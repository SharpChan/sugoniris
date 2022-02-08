package com.sugon.iris.sugondomain.beans.abnormalTradingBeans;

import lombok.Data;

@Data
public class TradingBean {

    /**
     * 交易账号
     */
     private String accountNo;

    /**
     * 反馈结果
     */
    private String feedback;

    /**
     * 交易时间
     */
    private String tradingDate;

    /**
     * 交易金额
     */
    private String tradingAmount;

    /**
     * 交易余额
     */
    private String tradingBalance;

    /**
     * 收付标志
     */
    private String receiptsOrPaid;

    /**
     * 交易对手账卡号
     */
    private String reciprocalAccount;

    /**
     * 对手户名
     */
    private String reciprocalName;

    /**
     * 对手证件号
     */
    private String reciprocalIdNo;

    /**
     * 对手开户银行
     */
    private String reciprocalBank;

    /**
     * 交易币种
     */
    private String currency;

    /**
     * 交易类型
     */
    private String tradingType;

}
