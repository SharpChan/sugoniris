package com.sugon.iris.sugondata.mybaties.mapper.db4;

import com.sugon.iris.sugondomain.entities.mybatiesEntity.db4.calculate.JymxEntity;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import java.util.List;

public interface JymxMapper {

    //获取卡号为空的账号
    @Select("select distinct jyzh  " +
            " from  ${tableName} where jyzh  is not null  and jyye  is not null  and jykh is null and jyzh  is not null ")
    public List<String> selectJyzh(String tableName);

    //获取账号为空的卡号
    @Select("select distinct jykh  " +
            " from  ${tableName} where jyzh  is not null  and jyye  is not null  and jyzh  is null and jykh  is not null ")
    public List<String> selectJykh(String tableName);



    //查询 该账号下 所有交易金额和交易余额不为空的数据，并且按时间排序，打上序号
    @Select("select id,jykh,jyzh,jyje,jyye,jysj,ROW_NUMBER () OVER (ORDER BY jysj,rzh ASC) AS  rownum  " +
            " from  ${tableName} where jyzh  is not null  and jyye  is not null  and jyzh = #{jyzh} order by jysj,rzh")
    public List<JymxEntity> selectJymxMapperAllByJyzh(String tableName, String jyzh);

    //查询 该卡号下 所有交易金额和交易余额不为空的数据，并且按时间排序，打上序号
    @Select("select id,jykh,jyzh,jyje,jyye,jysj,ROW_NUMBER () OVER (ORDER BY jysj,rzh ASC) AS  rownum  " +
            " from  ${tableName} where jyzh  is not null  and jyye  is not null  and jykh = #{jykh} order by jysj,rzh")
    public List<JymxEntity> selectJymxMapperAllByJykh(String tableName, String jykh);



    //查询所有交易金额和交易余额不为空的数据，并且卡号为空需要补全卡号的
    @Select("select b.id,b.jykh,b.jyzh,b.jyje,b.jyye,b.jysj,b.rownum from  (select id,jykh,jyzh,jyje,jyye,jysj,ROW_NUMBER () OVER (ORDER BY jysj,rzh ASC) AS  rownum  " +
            " from  ${tableName} where jyzh  is not null  and jyye  is not null  and jyzh = #{jyzh} order by jysj,rzh) b where b.jykh is null ")
    public List<JymxEntity> selecJymxMapperForJykh(String tableName,String jyzh);

    //查询所有交易金额和交易余额不为空的数据，并且交易账号为空需要补全账号的
    @Select("select b.id,b.jykh,b.jyzh,b.jyje,b.jyye,b.jysj,b.rownum from  (select id,jykh,jyzh,jyje,jyye,jysj,ROW_NUMBER () OVER (ORDER BY jysj,rzh ASC) AS  rownum  " +
            " from  ${tableName} where jyzh  is not null  and jyye  is not null  and jykh = #{jykh} order by jysj,rzh) b where b.jyzh is null ")
    public List<JymxEntity> selectJymxMapperForJyzh(String tableName,String jykh);



    //修改交易卡号
    @Update("update ${tableName} set  jykh=#{jykh}  where id=#{id}")
    public int UpdateJykh(String tableName,String jykh,Long id);

    //修改交易账号
    @Update("update ${tableName} set  jyzh=#{jyzh}  where id=#{id}")
    public int UpdateJyzh(String tableName,String jyzh,Long id);
}
