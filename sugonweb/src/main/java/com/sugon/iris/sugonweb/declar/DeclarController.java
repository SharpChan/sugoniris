package com.sugon.iris.sugonweb.declar;

import com.sugon.iris.sugonannotation.annotation.system.CurrentUser;
import com.sugon.iris.sugonannotation.annotation.system.LogInCheck;
import com.sugon.iris.sugondomain.beans.baseBeans.Error;
import com.sugon.iris.sugondomain.beans.baseBeans.RestResult;
import com.sugon.iris.sugondomain.beans.system.User;
import com.sugon.iris.sugondomain.dtos.declarDtos.DeclarInfoDto;
import com.sugon.iris.sugondomain.dtos.declarDtos.DeclarationDetailDto;
import com.sugon.iris.sugonservice.impl.declarServiceImpl.DeclarServiceImpl;
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
@RequestMapping("/declar")
@Api(value = "业务申报", tags = "业务申报相关接口")
public class DeclarController {

    private static final String FAILED = "FAILED";

    private static final String UNDEFINED = "undefined";

    @Resource
    private DeclarServiceImpl declarServiceImpl;


    @PostMapping("/getDeclarInfo")
    @LogInCheck(doLock = true,doProcess = true)
    @ApiOperation(value = "获取申报信息")
    public RestResult<List<DeclarInfoDto>> getDeclarInfo(@CurrentUser User user){
        RestResult<List<DeclarInfoDto>> restResult = new RestResult();
        List<Error> errorList = new ArrayList<>();
        try{
            restResult.setObj(declarServiceImpl.getDeclarInfo(user.getId(),errorList));
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

    @PostMapping("/getDeclarDetail")
    @LogInCheck(doLock = true,doProcess = true)
    @ApiOperation(value = "获取申报详细信息")
    @ApiImplicitParam(name = "status", value = "申报状态")
    public RestResult<List<DeclarationDetailDto>> getDeclarDetail(@CurrentUser User user,@RequestParam(value = "status") String status){
        RestResult<List<DeclarationDetailDto>> restResult = new RestResult();
        List<Error> errorList = new ArrayList<>();
        try{
            restResult.setObj(declarServiceImpl.getDeclarDetail(user.getId(),status,errorList));
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

    @PostMapping("/getAllDeclarDetail")
    @LogInCheck(doLock = true,doProcess = true)
    @ApiOperation(value = "根据申报类型申报状态获取所有申报详细信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "declarType", value = "申报类型"),
            @ApiImplicitParam(name = "declarStatus", value = "申报状态")
    })
    public RestResult<List<DeclarationDetailDto>> getAllDeclarDetail(@RequestParam(value = "declarType") String declarType,@RequestParam(value = "declarStatus") String declarStatus){
        RestResult<List<DeclarationDetailDto>> restResult = new RestResult();
        List<Error> errorList = new ArrayList<>();
        DeclarationDetailDto declarationDetail = new DeclarationDetailDto();
        declarationDetail.setStatus(UNDEFINED.equals(declarStatus) ? null : declarStatus);
        declarationDetail.setBusinessType(UNDEFINED.equals(declarType)? null : declarType);
        try{
            restResult.setObj(declarServiceImpl.getAllDeclarDetail(declarationDetail,errorList));
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

    @RequestMapping("/deleteDeclarDetail")
    @LogInCheck(doLock = true,doProcess = true)
    @ApiOperation(value = "删除获取申报详细信息")
    @ApiImplicitParam(name = "selected", value = "勾选的申报详细信息")
    public RestResult<Integer> deleteDeclarDetail(@RequestParam(value = "selected") String selected) throws Exception {

        RestResult<Integer> restResult = new RestResult();
        List<Error> errorList = new ArrayList<>();
        String[] selectedArr = selected.split(",");
        try {
            restResult.setObj(declarServiceImpl.deleteDeclarDetail(selectedArr,errorList));
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

    @RequestMapping("/failedDeclar")
    @LogInCheck(doLock = true,doProcess = true)
    @ApiOperation(value = "审核不通过")
    @ApiImplicitParam(name = "selected", value = "勾选的申报详细信息")
    public RestResult<Integer> failedDeclar(@CurrentUser User user,@RequestParam(value = "selected") String selected) throws Exception {

        RestResult<Integer> restResult = new RestResult();
        List<Error> errorList = new ArrayList<>();
        String[] selectedArr = selected.split(",");
        try {
            restResult.setObj(declarServiceImpl.failedDeclar(user.getId(),selectedArr,errorList));
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

    @RequestMapping("/approveDeclar")
    @LogInCheck(doLock = true,doProcess = true)
    @ApiOperation(value = "审核通过")
    @ApiImplicitParam(name = "selected", value = "勾选的申报详细信息")
    public RestResult<Integer> approveDeclar(@CurrentUser User user,@RequestParam(value = "selected") String selected) throws Exception {

        RestResult<Integer> restResult = new RestResult();
        List<Error> errorList = new ArrayList<>();
        String[] selectedArr = selected.split(",");
        try {
            restResult.setObj(declarServiceImpl.approveDeclar(user.getId(),selectedArr,errorList));
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
