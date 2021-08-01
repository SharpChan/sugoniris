package com.sugon.iris.sugonweb.declar;

import com.sugon.iris.sugonannotation.annotation.system.CurrentUser;
import com.sugon.iris.sugonannotation.annotation.system.LogInCheck;
import com.sugon.iris.sugondomain.beans.baseBeans.Error;
import com.sugon.iris.sugondomain.beans.baseBeans.RestResult;
import com.sugon.iris.sugondomain.beans.system.User;
import com.sugon.iris.sugondomain.dtos.declarDtos.DeclarInfoDto;
import com.sugon.iris.sugondomain.dtos.declarDtos.DeclarationDetailDto;
import com.sugon.iris.sugonservice.impl.declarServiceImpl.DeclarServiceImpl;
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
public class DeclarController {

    private static final String FAILED = "FAILED";

    @Resource
    private DeclarServiceImpl declarServiceImpl;


    @PostMapping("/getDeclarInfo")
    @LogInCheck(doLock = true,doProcess = true)
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
}