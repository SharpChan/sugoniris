package com.sugon.iris.sugondomain.entities.mybatiesEntity.db4.calculate;

import lombok.Data;

import java.util.Objects;

@Data
public class JymxEntity {

    private Long id;

    /**
     * 交易卡号
     */
    private String jykh;

    /**
     * 交易账号
     */
    private String jyzh;

    /**
     * 交易金额
     */
    private String jyje;

    /**
     * 交易余额
     */
    private String jyye;

    /**
     * 交易时间
     */
    private String jysj;

    /**
     * 排序号
     */
    private Integer rownum;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        JymxEntity that = (JymxEntity) o;
        return Objects.equals(jykh, that.jykh) &&
                Objects.equals(jyzh, that.jyzh) &&
                Objects.equals(jyje, that.jyje) &&
                Objects.equals(jyye, that.jyye) &&
                Objects.equals(jysj, that.jysj);
    }

    @Override
    public int hashCode() {
        return Objects.hash(jykh, jyzh, jyje, jyye, jysj);
    }
}
