package com.sugon.iris.sugondata.mybaties.mapper.db4;

import com.sugon.iris.sugondomain.entities.mybatiesEntity.db4.PoliceInfoEntity;
import org.apache.ibatis.annotations.Select;
import java.util.List;

public interface UserCheckMapper {


    //查询
    @Select("select a.wybs wybs,a.jinghao jinghao,a.xm xm,a.sfzh sfzh,a.sfqybs sfqybs,b.xtyhbmmc,b.xtyhbmbh from tdata.std_per_tydlpt_t_sys_user_core a,tdata.std_org_tydlpt_t_sys_dept b,tdata.std_org_tydlpt_t_sys_user_dept_uums c where a.wybs = c.yhwybs and b.xtyhbmbh = c.bmbh")
    public List<PoliceInfoEntity> findAllPoliceInfo();

    //查询
    @Select("select a.wybs wybs,a.jinghao jinghao,a.xm xm,a.sfzh sfzh,a.sfqybs sfqybs,b.xtyhbmmc,b.xtyhbmbh from std_per_tydlpt_t_sys_user_core a,std_org_tydlpt_t_sys_dept b,std_org_tydlpt_t_sys_user_dept_uums c where a.wybs = c.yhwybs and b.xtyhbmbh = c.bmbh")
    public List<PoliceInfoEntity> findAllPoliceInfoDev();
}
