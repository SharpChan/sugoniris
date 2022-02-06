package com.sugon.iris.sugonservice.service.casePersonnelInfoService;

import com.sugon.iris.sugondomain.beans.baseBeans.Error;
import com.sugon.iris.sugondomain.dtos.abnormalTradingDtos.CasePersonnelInfoDto;
import com.sugon.iris.sugondomain.dtos.abnormalTradingDtos.TradingDto;
import java.util.List;

public interface CasePersonnelInfoService {

    /**
     * 获取案件人员
     */
    List<CasePersonnelInfoDto> getPersonnelInfoByCaseId(Long userId, Long caseId, String threshold,List<Error> errorList) throws IllegalAccessException;

    /**
     * 获取交易记录
     */
    List<TradingDto> getTradingDetail(Long userId, Long caseId, String threshold, String cardId, List<Error> errorList) throws IllegalAccessException;

}
