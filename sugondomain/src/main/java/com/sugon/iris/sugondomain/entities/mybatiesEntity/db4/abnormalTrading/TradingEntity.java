package com.sugon.iris.sugondomain.entities.mybatiesEntity.db4.abnormalTrading;

import com.sugon.iris.sugondomain.beans.abnormalTradingBeans.TradingBean;
import lombok.Data;

import java.io.Serializable;
import java.util.Objects;

@Data
public class TradingEntity extends TradingBean implements Serializable {

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TradingEntity that = (TradingEntity) o;
        return Objects.equals(this.getAccountNo(), that.getAccountNo()) &&
                Objects.equals(this.getReciprocalAccount(), that.getReciprocalAccount());
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.getAccountNo(), this.getReciprocalAccount());
    }


}
