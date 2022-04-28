package com.sugon.iris.sugonservice.impl.casePersonnelServiceImpl;

import com.sugon.iris.sugoncommon.publicUtils.PublicUtils;
import com.sugon.iris.sugondata.mybaties.mapper.db4.CasePersonnelInfoMapper;
import com.sugon.iris.sugondomain.beans.baseBeans.Error;
import com.sugon.iris.sugondomain.dtos.abnormalTradingDtos.CasePersonnelInfoDto;
import com.sugon.iris.sugondomain.dtos.abnormalTradingDtos.TradingDto;
import com.sugon.iris.sugondomain.entities.mybatiesEntity.db4.abnormalTrading.CasePersonnelInfoEntity;
import com.sugon.iris.sugondomain.entities.mybatiesEntity.db4.abnormalTrading.TradingEntity;
import com.sugon.iris.sugondomain.enums.ErrorCode_Enum;
import com.sugon.iris.sugonservice.service.casePersonnelInfoService.CasePersonnelInfoService;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Service
public class CasePersonnelInfoServiceImpl implements CasePersonnelInfoService {

    @Resource
    private CasePersonnelInfoMapper casePersonnelInfoMapper;
    /**
     * 获取案件人员
     *
     * @param caseId
     */
    @Override
    public List<CasePersonnelInfoDto> getPersonnelInfoByCaseId(Long userId, Long caseId, String threshold,List<Error> errorList){

        String  tableName_zhxx = "base_bank_zhxx_"+caseId+"_"+userId+"_localfile";
        String  tableName_jymx = "base_bank_jymx_"+caseId+"_"+userId+"_localfile";
        List<CasePersonnelInfoDto> casePersonnelInfoDtoList = new ArrayList<>();
        List<CasePersonnelInfoEntity> casePersonnelInfoEntityList = null;
        try {
            casePersonnelInfoEntityList = casePersonnelInfoMapper.getCasePersonnelInfo(tableName_zhxx, tableName_jymx, threshold);
        }catch (Exception e){
            e.printStackTrace();
            errorList.add(new Error(ErrorCode_Enum.SYS_DB_001.getCode(),"获取案件人员信息失败",e.toString()));
        }
        if(!CollectionUtils.isEmpty(casePersonnelInfoEntityList)) {
            casePersonnelInfoEntityList.forEach(bean -> casePersonnelInfoDtoList.add(PublicUtils.trans(bean, new CasePersonnelInfoDto())));
        }
        return casePersonnelInfoDtoList;
    }

    /**
     * 获取交易记录
     *
     */
    @Override
    public List<TradingDto> getTradingDetail(Long userId, Long caseId, String threshold, String cardId, List<Error> errorList) throws IllegalAccessException {

        String  tableName_jymx = "base_bank_jymx_"+caseId+"_"+userId+"_localfile";
        List<TradingDto> tradingDtoList = new ArrayList<>();
        List<TradingEntity> tradingEntityList = null;
        try {
            tradingEntityList =  casePersonnelInfoMapper.getTradingDetail(tableName_jymx, threshold, cardId);
        }catch (Exception e){
            e.printStackTrace();
            errorList.add(new Error(ErrorCode_Enum.SYS_DB_001.getCode(),"获取案件交易信息失败",e.toString()));
        }
        if(!CollectionUtils.isEmpty(tradingEntityList)){
            tradingEntityList.forEach(bean -> tradingDtoList.add(PublicUtils.trans(bean,new TradingDto())));
        }

        return tradingDtoList;
    }
}
