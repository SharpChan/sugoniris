package com.sugon.iris.sugondomain.dtos.abnormalTradingDtos;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class InOutDto {

    //进账交易
    private TradingDto tradingIn;

    //对应的多笔出账交易
    private List<TradingDto> tradingOut;

    public InOutDto(){
        tradingOut = new ArrayList<>();
    }
}
