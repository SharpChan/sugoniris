package com.sugon.iris.sugonservice.impl.dataRinseServiceImpl;

import com.sugon.iris.sugondomain.enums.SZ_JZ_BusinessType_Enum;
import com.sugon.iris.sugonservice.service.dataRinseService.DataRinseService;
import java.util.List;
import java.util.Map;

public class DataRinseServiceImpl implements DataRinseService {
    @Override
    public void glzzhxxRinse(Map<String,Map<String,List<List<String>>>> map) {
           for(Map.Entry<String,Map<String,List<List<String>>>> entry : map.entrySet()){
               //关联子子账户信息,根据字段去重
               if(entry.getKey().equals(SZ_JZ_BusinessType_Enum.BUSINESS_01.getCode())){
                   for(Map.Entry<String,List<List<String>>> entry2:  entry.getValue().entrySet()){
                       String sql = "select id,mppid2errorid from " +
                               "(select row_number() OVER(PARTITION BY khmc,file_detail_id) AS rownum ,a.khmc,a.*" +
                               "        from "+entry2.getKey()+" a)b where b.rownum>1";
                   }
               }
           }
    }

    @Override
    public void jymxRinse() {

    }

    @Override
    public void qzcsRinse() {

    }

    @Override
    public void rylxfsRinse() {

    }

    @Override
    public void ryxxRinse() {

    }

    @Override
    public void ryzzRinse() {

    }

    @Override
    public void rwxxRinseSucess() {

    }

    @Override
    public void rwxxRinseFailed() {

    }

    @Override
    public void zhxxRinse() {

    }
}
