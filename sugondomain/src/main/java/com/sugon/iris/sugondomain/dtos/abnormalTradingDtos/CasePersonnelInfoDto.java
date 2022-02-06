package com.sugon.iris.sugondomain.dtos.abnormalTradingDtos;

import com.sugon.iris.sugondomain.beans.abnormalTradingBeans.CasePersonnelInfoBean;
import lombok.Data;
import java.util.List;

@Data
public class CasePersonnelInfoDto extends CasePersonnelInfoBean {


    /**
     * 人员下对应的快进快出交易
     */
    private List<InOutDto> inOutList;

    /**
     * 该人员相对其他人员为只进不出账户
     */
    private List<CasePersonnelInfoDto> casePersonnelInfoInList;

    /**
     * 该人员相对其他人员为只出不进账户
     */
    private List<CasePersonnelInfoDto> casePersonnelInfoOutList;
}
