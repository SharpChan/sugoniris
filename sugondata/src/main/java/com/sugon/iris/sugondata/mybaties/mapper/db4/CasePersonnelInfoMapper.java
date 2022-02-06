package com.sugon.iris.sugondata.mybaties.mapper.db4;

import com.sugon.iris.sugondomain.entities.mybatiesEntity.db4.abnormalTrading.CasePersonnelInfoEntity;
import com.sugon.iris.sugondomain.entities.mybatiesEntity.db4.abnormalTrading.TradingEntity;
import org.apache.ibatis.annotations.Select;
import java.util.List;

public interface CasePersonnelInfoMapper {

    //获取超交易金额阈值的账号
    @Select("select zhkhmc accountName,khrzjhm idNo,jykh cardId,jyzh accountNo, " +
            "(select count(*) from ${tableName_jymx} c where to_number(c.jyje,'99999999999.99')  >= ${threshold} and a.khrzjhm = c.jyzjhm) as blockTradeQuantity "+
            "from ${tableName_zhxx} a " +
            "where exists (select * from ${tableName_jymx} b where to_number(b.jyje,'99999999999.99')  >= ${threshold} and a.khrzjhm = b.jyzjhm);")
    public List<CasePersonnelInfoEntity> getCasePersonnelInfo(String  tableName_zhxx,String  tableName_jymx,String threshold );


    //获取案件下人员信息
    @Select("select zhkhmc accountName,khrzjhm idNo,jykh cardId,jyzh accountNo " +
            "from ${tableName_zhxx}")
    public List<CasePersonnelInfoEntity> getCasePersonnelInfo4InOut(String  tableName_zhxx);

    //获取超交易金额阈值交易明细
    @Select("select " +
            "jyzh accountNo,fkjg feedback,jysj tradingDate," +
            "jyje tradingAmount,jyye tradingBalance,sfbz receiptsOrPaid," +
            "jydszh reciprocalAccount,dshm reciprocalName, " +
            "dssfzh reciprocalIdNo,dskhyh reciprocalBank,jybz currency,jylx tradingType " +
            "from ${tableName_jymx} where jykh = ${cardId} and to_number(jyje,'999999999.99')  >= ${threshold};")
    public List<TradingEntity> getTradingDetail(String  tableName_jymx, String threshold, String cardId );

    //通过证件号获取交易明细
    @Select("select " +
            "jyzh accountNo,fkjg feedback,jysj tradingDate," +
            "jyje tradingAmount,jyye tradingBalance,sfbz receiptsOrPaid," +
            "jydszh reciprocalAccount,dshm reciprocalName, " +
            "dssfzh reciprocalIdNo,dskhyh reciprocalBank,jybz currency,jylx tradingType " +
            "from ${tableName_jymx} where jyzjhm = '${idNo}' order by jysj asc")
    public List<TradingEntity> getTradingDetailByIdNo(String tableName_jymx,String  idNo);

}
