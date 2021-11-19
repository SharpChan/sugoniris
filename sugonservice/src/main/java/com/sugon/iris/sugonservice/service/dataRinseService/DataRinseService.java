package com.sugon.iris.sugonservice.service.dataRinseService;

import java.util.List;
import java.util.Map;

public interface DataRinseService {

    //关联子子账户信息
    void completeRinse(String userId,Long caseId);

    //关联子子账户信息
    void completeRinse2(String userId,Long caseId);

}
