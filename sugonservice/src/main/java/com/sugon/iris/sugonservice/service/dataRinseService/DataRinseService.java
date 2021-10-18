package com.sugon.iris.sugonservice.service.dataRinseService;

import java.util.List;
import java.util.Map;

public interface DataRinseService {

    //关联子子账户信息
    void glzzhxxRinse(Map<String,Map<String,List<List<String>>>> map);

    //交易明细分析
    void jymxRinse();

    //强制措施
    void qzcsRinse();

    //人员联系方式
    void rylxfsRinse();

    //人员信息
    void ryxxRinse();

    //人员住址
    void ryzzRinse();

    //任务信息成功
    void rwxxRinseSucess();

    //任务信息失败
    void rwxxRinseFailed();

    //账户信息
    void zhxxRinse();
}
