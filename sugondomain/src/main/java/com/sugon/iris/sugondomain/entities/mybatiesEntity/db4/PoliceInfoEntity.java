package com.sugon.iris.sugondomain.entities.mybatiesEntity.db4;

import lombok.Data;

@Data
public class PoliceInfoEntity {

    /**
     * 唯一表示
     */
    private String wybs;

    /**
     * 警号
     */
    private String jinghao;

    /**
     * 姓名
     */

    private String xm;

    /**
     * 身份证号
     */
    private String sfzh;

    /**
     * 是否启用标识
     */
    private String sfzybs;
}
