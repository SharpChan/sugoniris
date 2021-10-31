package com.sugon.iris.sugonweb.file;

import com.sugon.iris.sugonannotation.annotation.system.CurrentUser;
import com.sugon.iris.sugonannotation.annotation.system.LogInCheck;
import com.sugon.iris.sugondomain.beans.baseBeans.Error;
import com.sugon.iris.sugondomain.beans.baseBeans.RestResult;
import com.sugon.iris.sugondomain.beans.system.User;
import com.sugon.iris.sugondomain.dtos.fileDtos.FileTemplateGroupDto;
import com.sugon.iris.sugonservice.service.fileService.FileTemplateGroupService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
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


    /**
     * 查询模板信息
     */

    @RequestMapping("/getFileTemplateGroups")
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

    @RequestMapping("/fileTemplateGroupInsert")
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


    @RequestMapping("/deleteFileTemplateGroup")
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

}
