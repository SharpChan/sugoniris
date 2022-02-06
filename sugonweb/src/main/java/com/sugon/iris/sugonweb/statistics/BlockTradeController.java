package com.sugon.iris.sugonweb.statistics;

import com.sugon.iris.sugonannotation.annotation.system.BussLog;
import com.sugon.iris.sugonannotation.annotation.system.CurrentUser;
import com.sugon.iris.sugonannotation.annotation.system.LogInCheck;
import com.sugon.iris.sugondomain.beans.baseBeans.Error;
import com.sugon.iris.sugondomain.beans.baseBeans.RestResult;
import com.sugon.iris.sugondomain.beans.system.User;
import com.sugon.iris.sugondomain.dtos.abnormalTradingDtos.CasePersonnelInfoDto;
import com.sugon.iris.sugondomain.dtos.abnormalTradingDtos.TradingDto;
import com.sugon.iris.sugonservice.service.casePersonnelInfoService.CasePersonnelInfoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/blockTrade")
@Api(value = "大额交易", tags = "案件相关接口")
public class BlockTradeController {

    private static final String FAILED = "FAILED";

    @Resource
    private CasePersonnelInfoService casePersonnelInfoServiceImpl;

    @PostMapping("/getPersonnelInfoByCaseId")
    @BussLog
    @LogInCheck(doLock = true,doProcess = true)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "caseId", value = "案件序列id"),
            @ApiImplicitParam(name = "threshold", value = "阈值")
    })
    @ApiOperation(value = "通过案件序列id获取案件人员信息")
    public RestResult<List<CasePersonnelInfoDto>> deleteCase(@CurrentUser User user, @RequestParam(value = "caseId") Long caseId, @RequestParam(value = "threshold") String threshold) throws Exception {
        RestResult<List<CasePersonnelInfoDto>> restResult = new RestResult();
        List<Error> errorList = new ArrayList<>();
        try {
            restResult.setObj(casePersonnelInfoServiceImpl.getPersonnelInfoByCaseId(user.getId(),caseId,threshold,errorList));
        }catch(Exception e){
            e.printStackTrace();
        }
        if(!CollectionUtils.isEmpty(errorList)){
            restResult.setFlag(FAILED);
            restResult.setMessage("执行失败！");
            restResult.setErrorList(errorList);
        }else{
            restResult.setMessage("执行成功");
        }
        return restResult;
    }

    @PostMapping("/getBlockTradeDetailByAccountNo")
    @BussLog
    @LogInCheck(doLock = true,doProcess = true)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "caseId", value = "案件序列id"),
            @ApiImplicitParam(name = "threshold", value = "阈值"),
            @ApiImplicitParam(name = "accountNo", value = "账号")
    })
    @ApiOperation(value = "通过账户编号获取交易明细")
    public RestResult<List<TradingDto>> getBlockTradeDetailByAccountNo(@CurrentUser User user, @RequestParam(value = "caseId") Long caseId, @RequestParam(value = "threshold") String threshold, @RequestParam(value = "cardId") String cardId) throws Exception {
        RestResult<List<TradingDto>> restResult = new RestResult();
        List<Error> errorList = new ArrayList<>();
        try {
            restResult.setObj(casePersonnelInfoServiceImpl.getTradingDetail(user.getId(),caseId,threshold,cardId,errorList));
        }catch(Exception e){
            e.printStackTrace();
        }
        if(!CollectionUtils.isEmpty(errorList)){
            restResult.setFlag(FAILED);
            restResult.setMessage("执行失败！");
            restResult.setErrorList(errorList);
        }else{
            restResult.setMessage("执行成功");
        }
        return restResult;
    }
}
