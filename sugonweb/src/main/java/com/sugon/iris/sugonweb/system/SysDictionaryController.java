package com.sugon.iris.sugonweb.system;

import com.sugon.iris.sugonannotation.annotation.system.CurrentUser;
import com.sugon.iris.sugonannotation.annotation.system.LogInCheck;
import com.sugon.iris.sugondomain.beans.baseBeans.Error;
import com.sugon.iris.sugondomain.beans.baseBeans.RestResult;
import com.sugon.iris.sugondomain.beans.system.User;
import com.sugon.iris.sugondomain.dtos.configDtos.SysDictionaryDto;
import com.sugon.iris.sugonservice.service.configService.SysDictionaryService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/sysDictionary")
@Api(value = "后台管理", tags = "系统字典配置")
public class SysDictionaryController {

    private static final Logger LOGGER = LogManager.getLogger(ConfigController.class);

    private static final String FAILED = "FAILED";

    @Resource
    private SysDictionaryService sysDictionaryServiceImpl;


    @PostMapping("/saveSysDictionary")
    @LogInCheck(doLock = true,doProcess = true)
    @ApiImplicitParam(name = "SysDictionaryDto", value = "字典信息")
    @ApiOperation(value = "保存字典信息")
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
    @ApiOperation(value = "获取所有字典信息")
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
    @ApiImplicitParam(name = "dicGroup", value = "字典组名称")
    @ApiOperation(value = "通过字典组名称获取字典信息")
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

    @PostMapping("/getSysDictionaryByDicGroupLike")
    @LogInCheck(doLock = true,doProcess = true)
    @ApiImplicitParam(name = "dicGroup", value = "字典组名称")
    @ApiOperation(value = "通过字典组名称模糊查詢")
    public RestResult<List<SysDictionaryDto>> getSysDictionaryByDicGroupLike(@RequestParam(value = "dicGroup") String dicGroup){
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
    @ApiImplicitParam(name = "sysDictionaryDto", value = "字典信息")
    @ApiOperation(value = "更新字典信息")
    public RestResult<Integer> updateSysDictionary(@CurrentUser User user,@RequestBody SysDictionaryDto sysDictionaryDto){
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
    @ApiImplicitParam(name = "id", value = "字典信息id")
    @ApiOperation(value = "通过id删除字典信息")
    public RestResult<Integer> deleteSysDictionary(@RequestParam(value = "id") Long id) {
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
