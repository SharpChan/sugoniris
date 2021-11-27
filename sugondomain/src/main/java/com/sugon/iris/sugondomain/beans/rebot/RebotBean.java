package com.sugon.iris.sugondomain.beans.rebot;

import lombok.Data;

@Data
public class RebotBean {

    /**
     * 自增序列
     */
    private Long id;

    /**
     * token
     */
    private String token;

    /**
     *时间
     */
    private String date;

    /**
     *案件编号
     */
    private Long caseId;

    /**
     * 警号
     */
    private String policeno;


}



