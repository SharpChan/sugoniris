package com.sugon.iris.sugonservice.impl.dataRinseServiceImpl;

import com.sugon.iris.sugondata.mybaties.mapper.db2.FileCaseMapper;
import com.sugon.iris.sugondata.mybaties.mapper.db4.MppMapper;
import com.sugon.iris.sugondomain.entities.mybatiesEntity.db2.FileCaseEntity;
import com.sugon.iris.sugonservice.service.dataRinseService.DataRinseService;
import com.sugon.iris.sugonservice.service.fileService.FileDoParsingService;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;

@Service
public class DataRinseServiceImpl implements DataRinseService {

    @Resource
    private MppMapper  mppMapper;

    @Resource
    private FileCaseMapper fileCaseMapper;

    @Resource
    private FileDoParsingService fileDoParsingServiceImpl;

    /**
     * 进行数据补全
     * @param userId
     * @param caseId
     */
    @Override
    public void completeRinse(String userId,Long caseId) {
                //进行交易明细的交易账号的补全
                //获取这些数据的mppid2errorid，以便重新进行校验
                String selectSql = "select a.* from base_bank_jymx_"+caseId+"_"+userId+" a where a.jyhm is null \n" +
                        "             or trim(a.jyhm) is null \n" +
                        "             and exists(select * from base_bank_zhxx_"+caseId+"_"+userId+" b \n" +
                        "                             where a.jykh =b.jykh \n" +
                        "                             and a.jyzh = b.jyzh\n" +
                        "                             and b.zhkhmc is not null \n" +
                        "                             and trim(b.zhkhmc) is not null )";

                String updateSql = "update  " +
                        "base_bank_jymx_"+caseId+"_"+userId+" a " +
                        "set jyhm = (select zhkhmc from base_bank_zhxx_"+caseId+"_"+userId+" b where a.jykh =b.jykh and a.jyzh = b.jyzh  and b.zhkhmc is not null and trim(b.zhkhmc) is not null ) " +
                        "where a.jyzjhm is null or trim(a.jyzjhm) is null ";
        mppMapper.mppSqlExec(updateSql);
        //对清楚次数进行修改
        updateRinseTimes(caseId);
    }

    /**
     * 进行数据补全
     * @param userId
     * @param caseId
     */
    @Override
    public void completeRinse2(String userId,Long caseId) {



    }

    private void updateRinseTimes(Long caseId) {
        FileCaseEntity fileCaseEntity = fileCaseMapper.selectFileCaseByPrimaryKey(caseId);
        fileCaseEntity.setRinseTimes(fileCaseEntity.getRinseTimes() == null ? 1: fileCaseEntity.getRinseTimes()+1);
        fileCaseMapper.updateFileCase(fileCaseEntity);
    }
}
