package com.sugon.iris.sugonweb.actualCenter;

import com.sugon.iris.sugonannotation.annotation.system.CurrentUser;
import com.sugon.iris.sugonannotation.annotation.system.LogInCheck;
import com.sugon.iris.sugoncommon.publicUtils.PublicUtils;
import com.sugon.iris.sugondomain.beans.baseBeans.Error;
import com.sugon.iris.sugondomain.beans.baseBeans.RestResult;
import com.sugon.iris.sugondomain.beans.system.User;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/actualCenter")
@Api(value = "实战中心", tags = "实战中心相关接口")
public class ActualCenterController {
    private static final String FAILED = "FAILED";


    @PostMapping("/getMcgcUrl")
    @LogInCheck(doLock = true,doProcess = true)
    @ApiOperation(value = "获取数据魔方url地址")
    public RestResult<String> getMcgcUrl(@CurrentUser User user){
        RestResult<String> restResult = new RestResult();
        List<Error> errorList = new ArrayList<>();
        try{
            restResult.setObj(PublicUtils.getConfigMap().get("mcgc-url")+"index.jsp?userName="+user.getUserName()+"&password="+user.getPassword());
            //restResult.setObj(PublicUtils.getConfigMap().get("mcgc-url")+"index.jsp?userName="+"Sysadmin"+"&password="+"1bbd886460827015e5d605ed44252251");
        }catch (Exception e){
            e.printStackTrace();
        }
        if(!CollectionUtils.isEmpty(errorList)){
            restResult.setFlag(FAILED);
            restResult.setMessage("操作失败！");
            restResult.setErrorList(errorList);
        }else{
            restResult.setMessage("操作成功");
        }
        return restResult;
    }

    @PostMapping("/getSdmUrl")
    @LogInCheck(doLock = true,doProcess = true)
    @ApiOperation(value = "获取天网url地址")
    public RestResult<String> getSdmUrl(@CurrentUser User user){
        RestResult<String> restResult = new RestResult();
        List<Error> errorList = new ArrayList<>();
        try{
            restResult.setObj(PublicUtils.getConfigMap().get("sdm-url"));
            //restResult.setObj(PublicUtils.getConfigMap().get("mcgc-url")+"index.jsp?userName="+"Sysadmin"+"&password="+"1bbd886460827015e5d605ed44252251");
        }catch (Exception e){
            e.printStackTrace();
        }
        if(!CollectionUtils.isEmpty(errorList)){
            restResult.setFlag(FAILED);
            restResult.setMessage("操作失败！");
            restResult.setErrorList(errorList);
        }else{
            restResult.setMessage("操作成功");
        }
        return restResult;
    }

    @PostMapping("/getXxlJobUrl")
    @LogInCheck(doLock = true,doProcess = true)
    @ApiOperation(value = "获取天网url地址")
    public RestResult<String> getXxlJobUrl(@CurrentUser User user){
        RestResult<String> restResult = new RestResult();
        List<Error> errorList = new ArrayList<>();
        try{
            restResult.setObj(PublicUtils.getConfigMap().get("xxlJob-url"));
            //restResult.setObj(PublicUtils.getConfigMap().get("mcgc-url")+"index.jsp?userName="+"Sysadmin"+"&password="+"1bbd886460827015e5d605ed44252251");
        }catch (Exception e){
            e.printStackTrace();
        }
        if(!CollectionUtils.isEmpty(errorList)){
            restResult.setFlag(FAILED);
            restResult.setMessage("操作失败！");
            restResult.setErrorList(errorList);
        }else{
            restResult.setMessage("操作成功");
        }
        return restResult;
    }
}
