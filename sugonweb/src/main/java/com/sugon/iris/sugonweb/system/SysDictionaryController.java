package com.sugon.iris.sugonweb.system;

import com.sugon.iris.sugonannotation.annotation.system.CurrentUser;
import com.sugon.iris.sugonannotation.annotation.system.LogInCheck;
import com.sugon.iris.sugondomain.beans.baseBeans.Error;
import com.sugon.iris.sugondomain.beans.baseBeans.RestResult;
import com.sugon.iris.sugondomain.beans.system.User;
import com.sugon.iris.sugondomain.dtos.configDtos.SysDictionaryDto;
import com.sugon.iris.sugonservice.service.configService.SysDictionaryService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/sysDictionary")
public class SysDictionaryController {

    private static final Logger LOGGER = LogManager.getLogger(ConfigController.class);

    private static final String FAILED = "FAILED";

    @Resource
    private SysDictionaryService sysDictionaryServiceImpl;


    @PostMapping("/saveSysDictionary")
    @LogInCheck(doLock = true,doProcess = true)
    public RestResult<Integer> saveSysDictionary(@CurrentUser User user, @RequestBody SysDictionaryDto sysDictionaryDto){
        sysDictionaryDto.setUserId(user.getId());
        RestResult<Integer> restResult = new RestResult();
        List<Error> errorList = new ArrayList<>();
        try{
            restResult.setObj(sysDictionaryServiceImpl.saveSysDictionary(sysDictionaryDto,errorList));
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

    @PostMapping("/getAllSysDictionary")
    @LogInCheck(doLock = true,doProcess = true)
    public RestResult<List<SysDictionaryDto>> getAllSysDictionary(){
        RestResult<List<SysDictionaryDto>> restResult = new RestResult();
        List<Error> errorList = new ArrayList<>();
        try{
            restResult.setObj(sysDictionaryServiceImpl.getAllSysDictionaries(errorList));
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

    @PostMapping("/getSysDictionaryByDicGroup")
    @LogInCheck(doLock = true,doProcess = true)
    public RestResult<List<SysDictionaryDto>> getSysDictionaryByDicGroup(@RequestParam(value = "dicGroup") String dicGroup){
        RestResult<List<SysDictionaryDto>> restResult = new RestResult();
        List<Error> errorList = new ArrayList<>();
        try{
            restResult.setObj(sysDictionaryServiceImpl.getSysDictionariesByDicGroup(dicGroup,errorList));
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



    @PostMapping("/updateSysDictionary")
    @LogInCheck(doLock = true,doProcess = true)
    public RestResult<Integer> getUserNameAndPassword(@CurrentUser User user,@RequestBody SysDictionaryDto sysDictionaryDto){
        sysDictionaryDto.setUserId(user.getId());
        RestResult<Integer> restResult = new RestResult();
        List<Error> errorList = new ArrayList<>();
        try{
            restResult.setObj(sysDictionaryServiceImpl.updateSysDictionary(sysDictionaryDto,errorList));
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

    @PostMapping("/deleteSysDictionary")
    @LogInCheck(doLock = true,doProcess = true)
    public RestResult<Integer> deleteWhiteIp(@RequestParam(value = "id") Long id) {
        RestResult<Integer> restResult = new RestResult();
        List<Error> errorList = new ArrayList<>();
        try {
            restResult.setObj(sysDictionaryServiceImpl.deleteSysDictionary(id,errorList));
        }catch(Exception e){
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
