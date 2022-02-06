package com.sugon.iris.sugonservice.service.statisticsService;

import com.sugon.iris.sugondomain.beans.baseBeans.Error;
import com.sugon.iris.sugondomain.dtos.abnormalTradingDtos.CasePersonnelInfoDto;
import com.sugon.iris.sugondomain.dtos.abnormalTradingDtos.CentrostigmaDto;

import java.util.List;

public interface StatisticsService {

    /**
     * 从0时开始，以一个小时为步长统计转入交易次数
     */
    List<CentrostigmaDto> centrostigmaInAnalyse(Long userId, Long caseId , List<Error> errorList);


    /**
     * 从0时开始，以一个小时为步长统计转出交易次数
     */
    List<CentrostigmaDto> centrostigmaOutAnalyse(Long userId, Long caseId , List<Error> errorList );

    /**
     * 快进快出交易点分析
     */
    List<CasePersonnelInfoDto> getInOutTrading(Long userId, Long caseId , List<Error> errorList );

    /**
     * 只进只出账户分析
     */
    List<CasePersonnelInfoDto> getInOrOutOnly(Long userId, Long caseId , List<Error> errorList );

}
