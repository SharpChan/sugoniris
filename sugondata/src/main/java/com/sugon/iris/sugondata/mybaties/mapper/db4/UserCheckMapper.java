package com.sugon.iris.sugondata.mybaties.mapper.db4;

import com.sugon.iris.sugondomain.entities.mybatiesEntity.db4.PoliceInfoEntity;
import org.apache.ibatis.annotations.Select;
import java.util.List;

public interface UserCheckMapper {


    //查询
    @Select("select wybs,jinghao,xm,sfzh,sfzybs from tdata.std_per_tydlpt_t_sys_user_core ")
    public List<PoliceInfoEntity> findAllPoliceInfo();

    //查询
    @Select("select wybs,jinghao,xm,sfzh,sfzybs from std_per_tydlpt_t_sys_user_core ")
    public List<PoliceInfoEntity> findAllPoliceInfoDev();
}
