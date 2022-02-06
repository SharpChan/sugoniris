package com.sugon.iris.sugonweb.statistics;


import com.sugon.iris.sugonannotation.annotation.system.BussLog;
import com.sugon.iris.sugonannotation.annotation.system.CurrentUser;
import com.sugon.iris.sugonannotation.annotation.system.LogInCheck;
import com.sugon.iris.sugondomain.beans.baseBeans.Error;
import com.sugon.iris.sugondomain.beans.baseBeans.RestResult;
import com.sugon.iris.sugondomain.beans.system.User;
import com.sugon.iris.sugondomain.dtos.abnormalTradingDtos.CasePersonnelInfoDto;
import com.sugon.iris.sugondomain.dtos.abnormalTradingDtos.CentrostigmaDto;
import com.sugon.iris.sugonservice.service.statisticsService.StatisticsService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/statistics")
@Api(value = "异常交易分析", tags = "")
public class StatisticsController {

    private static final String FAILED = "FAILED";

    @Resource
    private StatisticsService statisticsServiceImpl;


    @RequestMapping("/centrostigmaInAnalyse")
    @BussLog
    @LogInCheck(doLock = true,doProcess = true)
    @ApiOperation(value = "集中转入点")
    @ApiImplicitParam(name = "caseId", value = "案件自增序列")
    public RestResult<List<CentrostigmaDto>> centrostigmaInAnalyse(@CurrentUser User user, @RequestParam(value = "caseId") Long  caseId){
        RestResult<List<CentrostigmaDto>> restResult = new RestResult();
        List<Error> errorList = new ArrayList<>();
        try{
            restResult.setObj(statisticsServiceImpl.centrostigmaInAnalyse(user.getId(),caseId,errorList));
        }catch (Exception e){
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

    @RequestMapping("/centrostigmaOutAnalyse")
    @BussLog
    @LogInCheck(doLock = true,doProcess = true)
    @ApiOperation(value = "集中转出点")
    @ApiImplicitParam(name = "caseId", value = "案件自增序列")
    public RestResult<List<CentrostigmaDto>> centrostigmaOutAnalyse(@CurrentUser User user, @RequestParam(value = "caseId") Long  caseId){
        RestResult<List<CentrostigmaDto>> restResult = new RestResult();
        List<Error> errorList = new ArrayList<>();
        try{
            restResult.setObj(statisticsServiceImpl.centrostigmaOutAnalyse(user.getId(),caseId,errorList));
        }catch (Exception e){
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

    @RequestMapping("/getInOutTrading")
    @BussLog
    @LogInCheck(doLock = true,doProcess = true)
    @ApiOperation(value = "快进快出交易点")
    @ApiImplicitParam(name = "caseId", value = "案件自增序列")
    public RestResult<List<CasePersonnelInfoDto>> getInOutTrading(@CurrentUser User user, @RequestParam(value = "caseId") Long  caseId){
        RestResult<List<CasePersonnelInfoDto>> restResult = new RestResult();
        List<Error> errorList = new ArrayList<>();
        try{
            restResult.setObj(statisticsServiceImpl.getInOutTrading(user.getId(),caseId,errorList));
        }catch (Exception e){
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

    @RequestMapping("/getInOrOutOnly")
    @BussLog
    @LogInCheck(doLock = true,doProcess = true)
    @ApiOperation(value = "只进只出账户分析")
    @ApiImplicitParam(name = "caseId", value = "案件自增序列")
    public RestResult<List<CasePersonnelInfoDto>> getInOrOutOnly(@CurrentUser User user, @RequestParam(value = "caseId") Long  caseId){
        RestResult<List<CasePersonnelInfoDto>> restResult = new RestResult();
        List<Error> errorList = new ArrayList<>();
        try{
            restResult.setObj(statisticsServiceImpl.getInOrOutOnly(user.getId(),caseId,errorList));
        }catch (Exception e){
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
