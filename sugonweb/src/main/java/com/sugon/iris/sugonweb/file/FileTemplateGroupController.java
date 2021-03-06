package com.sugon.iris.sugonweb.file;

import com.sugon.iris.sugonannotation.annotation.system.CurrentUser;
import com.sugon.iris.sugonannotation.annotation.system.LogInCheck;
import com.sugon.iris.sugondomain.beans.baseBeans.Error;
import com.sugon.iris.sugondomain.beans.baseBeans.RestResult;
import com.sugon.iris.sugondomain.beans.system.User;
import com.sugon.iris.sugondomain.dtos.fileDtos.FileFieldCompleteDto;
import com.sugon.iris.sugondomain.dtos.fileDtos.FileTemplateDto;
import com.sugon.iris.sugondomain.dtos.fileDtos.FileTemplateGroupDto;
import com.sugon.iris.sugonservice.service.fileService.FileFieldCompleteService;
import com.sugon.iris.sugonservice.service.fileService.FileTemplateGroupService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/fileTemplateGroup")
@Api(value = "模板组", tags = "模板组相关接口")
public class FileTemplateGroupController {

    private static final String FAILED = "FAILED";

    @Resource
    private FileTemplateGroupService fileTemplateGroupServiceImpl;

    @Resource
    private FileFieldCompleteService fileFieldCompleteServiceImpl;


    /**
     * 查询模板组信息
     */

    @PostMapping("/getFileTemplateGroupDtoListByThisUserId")
    @LogInCheck(doLock = true,doProcess = true)
    @ApiOperation(value = "通过模板组id获取用户组内模板组")
    public RestResult<List<FileTemplateGroupDto>> getFileTemplateGroupDtoListByThisUserId(@CurrentUser User user){
        RestResult<List<FileTemplateGroupDto>> restResult = new RestResult();
        List<Error> errorList = new ArrayList<>();
        try{
            restResult.setObj(fileTemplateGroupServiceImpl.getFileTemplateGroupDtoListByThisUserId(user,errorList));
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


    /**
     * 查询模板组信息
     */

    @PostMapping("/getFileTemplateGroups")
    @LogInCheck(doLock = true,doProcess = true)
    @ApiOperation(value = "通过模板组id获取模板")
    @ApiImplicitParam(name = "fileTemplateGroupDto", value = "模板组信息")
    public RestResult<List<FileTemplateGroupDto>> getFileTemplates(@CurrentUser User user,@RequestBody FileTemplateGroupDto fileTemplateGroupDto){
        RestResult<List<FileTemplateGroupDto>> restResult = new RestResult();
        List<Error> errorList = new ArrayList<>();
        try{
            restResult.setObj(fileTemplateGroupServiceImpl.getFileTemplateGroupDtoList(user,fileTemplateGroupDto,errorList));
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

    @PostMapping("/fileTemplateGroupInsert")
    @LogInCheck(doLock = true,doProcess = true)
    @ApiOperation(value = "通过模板组id获取模板")
    @ApiImplicitParam(name = "fileTemplateGroupDto", value = "模板组信息")
    public RestResult<Integer> fileTemplateInsert(@CurrentUser User user,@RequestBody FileTemplateGroupDto fileTemplateGroupDto){
        RestResult<Integer> restResult = new RestResult();
        List<Error> errorList = new ArrayList<>();
        try{
            restResult.setObj(fileTemplateGroupServiceImpl.fileTemplateGroupInsert(user,fileTemplateGroupDto,errorList));
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


    @PostMapping("/deleteFileTemplateGroup")
    @LogInCheck(doLock = true,doProcess = true)
    @ApiOperation(value = "删除模板组信息")
    @ApiImplicitParam(name = "selected", value = "勾选的")
    public RestResult<Integer> deleteFileTemplateGroup(@RequestParam(value = "selected") String selected) throws Exception {
        RestResult<Integer> restResult = new RestResult();
        List<Error> errorList = new ArrayList<>();
        String[] selectedArr = selected.split(",");
        try {
            restResult.setObj(fileTemplateGroupServiceImpl.deleteFileTemplateGroup(selectedArr,errorList));
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

    @PostMapping("/getFileFieldCompletes")
    @LogInCheck(doLock = true,doProcess = true)
    @ApiOperation(value = "通过模板组id获取数据补全配置信息")
    @ApiImplicitParam(name = "groupId", value = "模板组id")
    public RestResult<List<FileFieldCompleteDto>> getFileFieldCompletes(@CurrentUser User user, @RequestParam(value = "groupId") Long groupId){
        RestResult<List<FileFieldCompleteDto>> restResult = new RestResult();
        List<Error> errorList = new ArrayList<>();
        try{
            restResult.setObj(fileFieldCompleteServiceImpl.getFileFieldCompletesList(groupId,errorList));
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

    @PostMapping("/modifyCompletesSortNoById")
    @LogInCheck(doLock = true,doProcess = true)
    @ApiOperation(value = "通过id修改排序编号")
    @ApiImplicitParam(name = "groupId", value = "模板组id")
    public RestResult<Boolean> modifyCompletesSortNoById(@RequestParam(value = "id") Long id,@RequestParam(value = "sortNo") String sortNo){
        RestResult<Boolean> restResult = new RestResult();
        List<Error> errorList = new ArrayList<>();
        try{
            restResult.setObj(fileFieldCompleteServiceImpl.modifyCompletesSortNoById(id,sortNo,errorList));
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

    @PostMapping("/removeFileFieldComplete")
    @LogInCheck(doLock = true,doProcess = true)
    @ApiOperation(value = "通过模板组id获取数据补全配置信息")
    @ApiImplicitParam(name = "groupId", value = "模板组id")
    public RestResult<Integer> removeFileFieldComplete(@RequestParam(value = "id") Long id){
        RestResult<Integer> restResult = new RestResult();
        List<Error> errorList = new ArrayList<>();
        try{
            restResult.setObj(fileFieldCompleteServiceImpl.removeFileFieldComplete(id,errorList));
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

    @PostMapping("/getFileTemplateByGroupId")
    @LogInCheck(doLock = true,doProcess = true)
    @ApiOperation(value = "通过模板组id获取数据补全配置信息")
    @ApiImplicitParam(name = "groupId", value = "模板组id")
    public RestResult<List<FileTemplateDto>> getFileTemplateByGroupId(@RequestParam(value = "groupId") Long groupId){
        RestResult<List<FileTemplateDto>> restResult = new RestResult();
        List<Error> errorList = new ArrayList<>();
        try{
            restResult.setObj(fileTemplateGroupServiceImpl.getFileTemplateByFileTemplateGroupId(groupId,errorList));
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

    @PostMapping("/saveFileFieldCompletes")
    @LogInCheck(doLock = true,doProcess = true)
    @ApiOperation(value = "保存数据补全配置信息")
    @ApiImplicitParam(name = "fileFieldCompleteDto", value = "数据补全配置信息")
    public RestResult<Integer> saveFileFieldCompletes(@CurrentUser User user,@RequestBody FileFieldCompleteDto fileFieldCompleteDto){
        RestResult<Integer> restResult = new RestResult();
        List<Error> errorList = new ArrayList<>();
        try{
            restResult.setObj(fileFieldCompleteServiceImpl.saveFileFieldComplete(fileFieldCompleteDto,errorList));
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
