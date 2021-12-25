package com.sugon.iris.sugondata.service;

import com.sugon.iris.sugondata.mybaties.mapper.db4.MppMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class DataCommonService {

    @Resource
    MppMapper mppMapper;

    public int mppSqlExec(String updateStr){
        int i = 0;
        synchronized (updateStr.substring(0,updateStr.indexOf("set"))){
            i = mppMapper.mppSqlExec(updateStr);
        }
        return i;
    }
}
