package com.sugon.iris.sugonweb.file;


import com.sugon.iris.sugonannotation.annotation.system.CurrentUser;
import com.sugon.iris.sugonannotation.annotation.system.LogInCheck;
import com.sugon.iris.sugondomain.beans.baseBeans.Error;
import com.sugon.iris.sugondomain.beans.baseBeans.RestResult;
import com.sugon.iris.sugondomain.beans.system.User;
import com.sugon.iris.sugondomain.dtos.fileDtos.FileCaseDto;
import com.sugon.iris.sugonservice.service.FileService.FileCaseService;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/fileCase")
public class FileCaseController {

    private static final String FAILED = "FAILED";

    @Resource
    private FileCaseService fileCaseServiceImpl;

    @PostMapping("/saveCase")
    @LogInCheck(doLock = true,doProcess = true)
    public RestResult<Integer> saveCase(@CurrentUser User user, @RequestBody FileCaseDto fileCaseDto){
        RestResult<Integer> restResult = new RestResult();
        List<Error> errorList = new ArrayList<>();
        try{
            restResult.setObj(fileCaseServiceImpl.saveCase(user,fileCaseDto,errorList));
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

    @RequestMapping("/getCases")
    @LogInCheck(doLock = true,doProcess = true)
    public RestResult<List<FileCaseDto>> getCases(@CurrentUser User user, @RequestBody FileCaseDto fileCaseDto){
        RestResult<List<FileCaseDto>> restResult = new RestResult();
        List<Error> errorList = new ArrayList<>();
        try{
            restResult.setObj(fileCaseServiceImpl.selectCaseList(user,fileCaseDto,errorList));
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

    @PostMapping("/updateCase")
    @LogInCheck(doLock = true,doProcess = true)
    public RestResult<Integer> updateCase(@CurrentUser User user, @RequestBody FileCaseDto fileCaseDto){
        RestResult<Integer> restResult = new RestResult();
        List<Error> errorList = new ArrayList<>();
        try{
            restResult.setObj(fileCaseServiceImpl.updateCase(user,fileCaseDto,errorList));
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

    @RequestMapping("/deleteCase")
    @LogInCheck(doLock = true,doProcess = true)
    public RestResult<Integer> deleteCase(@CurrentUser User user,@RequestParam(value = "selected") String selected) throws Exception {
        RestResult<Integer> restResult = new RestResult();
        List<Error> errorList = new ArrayList<>();
        String[] selectedArr = selected.split(",");
        try {
            restResult.setObj(fileCaseServiceImpl.deleteCase(user,selectedArr,errorList));
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
