package com.sugon.iris.sugonweb.file;


import com.sugon.iris.sugonannotation.annotation.system.BussLog;
import com.sugon.iris.sugonannotation.annotation.system.CurrentUser;
import com.sugon.iris.sugonannotation.annotation.system.LogInCheck;
import com.sugon.iris.sugondomain.beans.baseBeans.Error;
import com.sugon.iris.sugondomain.beans.baseBeans.RestResult;
import com.sugon.iris.sugondomain.beans.system.User;
import com.sugon.iris.sugondomain.dtos.fileDtos.FileCaseDto;
import com.sugon.iris.sugonservice.service.fileService.FileCaseService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/fileCase")
@Api(value = "文件管理", tags = "案件相关接口")
public class FileCaseController {

    private static final String FAILED = "FAILED";

    @Resource
    private FileCaseService fileCaseServiceImpl;


    @PostMapping("/saveCase")
    @BussLog
    @LogInCheck(doLock = true,doProcess = true)
    @ApiImplicitParam(name = "fileCaseDto", value = "案件信息")
    @ApiOperation(value = "案件保存")
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

    @PostMapping("/getCases")
    @BussLog
    @LogInCheck(doLock = true,doProcess = true)
    @ApiImplicitParam(name = "fileCaseDto", value = "案件信息")
    @ApiOperation(value = "获取案件")
    public RestResult<List<FileCaseDto>> getCases(@CurrentUser User user, @RequestBody FileCaseDto fileCaseDto){
        RestResult<List<FileCaseDto>> restResult = new RestResult();
        List<Error> errorList = new ArrayList<>();
        fileCaseDto.setUserId(user.getId());
        try{
            restResult.setObj(fileCaseServiceImpl.selectCaseList(fileCaseDto,errorList));
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
    @ApiImplicitParam(name = "fileCaseDto", value = "案件信息")
    @ApiOperation(value = "案件更新")
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

    @PostMapping("/deleteCase")
    @BussLog
    @LogInCheck(doLock = true,doProcess = true)
    @ApiImplicitParam(name = "selected", value = "已选择的案件id")
    @ApiOperation(value = "案件删除")
    public RestResult<Integer> deleteCase(@CurrentUser User user,@RequestParam(value = "selected") String selected) throws Exception {
        RestResult<Integer> restResult = new RestResult();
        List<Error> errorList = new ArrayList<>();
        String[] selectedArr = selected.split(",");
        try {
            restResult.setObj(fileCaseServiceImpl.deleteCase(user,selectedArr,true,errorList));
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
