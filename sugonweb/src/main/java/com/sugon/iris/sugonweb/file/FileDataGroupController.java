package com.sugon.iris.sugonweb.file;

import com.sugon.iris.sugonannotation.annotation.system.CurrentUser;
import com.sugon.iris.sugonannotation.annotation.system.LogInCheck;
import com.sugon.iris.sugondomain.beans.baseBeans.Error;
import com.sugon.iris.sugondomain.beans.baseBeans.RestResult;
import com.sugon.iris.sugondomain.beans.system.User;
import com.sugon.iris.sugondomain.dtos.fileDtos.FileDataGroupDto;
import com.sugon.iris.sugonservice.service.fileService.FileDataGroupService;
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
@RequestMapping("/fileDataGroup")
@Api(value = "文件管理", tags = "数据组信息接口")
public class FileDataGroupController {

    private static final String FAILED = "FAILED";

    @Resource
    private FileDataGroupService  fileDataGroupServiceImpl;

    /**
     * 获取数据组信息
     */

    @RequestMapping("/getFileDataGroup")
    @LogInCheck(doLock = true,doProcess = true)
    @ApiOperation(value = "获取数据组信息")
    @ApiImplicitParam(name = "fileDataGroupDto", value = "数据组信息")
    public RestResult<List<FileDataGroupDto>> getFileDataGroup(@CurrentUser User user, @RequestBody FileDataGroupDto fileDataGroupDto){
        RestResult<List<FileDataGroupDto>> restResult = new RestResult();
        List<Error> errorList = new ArrayList<>();
        try{
            restResult.setObj(fileDataGroupServiceImpl.getFileDataGroupDtoList(user,fileDataGroupDto,errorList));
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

    @RequestMapping("/fileDataGroupSave")
    @LogInCheck(doLock = true,doProcess = true)
    @ApiOperation(value = "数据组信息保存")
    @ApiImplicitParam(name = "fileDataGroupDto", value = "数据组信息")
    public RestResult<Integer> fileDataGroupSave(@CurrentUser User user,@RequestBody FileDataGroupDto fileDataGroupDto){
        RestResult<Integer> restResult = new RestResult();
        List<Error> errorList = new ArrayList<>();
        try{
            restResult.setObj(fileDataGroupServiceImpl.fileDataGroupInsert(user,fileDataGroupDto,errorList));
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

    @RequestMapping("/updateFileDataGroup")
    @LogInCheck(doLock = true,doProcess = true)
    @ApiOperation(value = "数据组信息更新")
    @ApiImplicitParam(name = "fileDataGroupDto", value = "数据组信息")
    public RestResult<Integer> updateFileTemplate(@RequestBody FileDataGroupDto fileDataGroupDto){
        RestResult<Integer> restResult = new RestResult();
        List<Error> errorList = new ArrayList<>();
        try{
            restResult.setObj(fileDataGroupServiceImpl.modifyGroupInsert(fileDataGroupDto,errorList));
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

    @RequestMapping("/deleteFileDataGroup")
    @LogInCheck(doLock = true,doProcess = true)
    @ApiOperation(value = "数据组信息删除")
    @ApiImplicitParam(name = "id", value = "数据组信息id")
    public RestResult<Integer> deleteFileDataGroup(@RequestParam(value = "id") Long  id) throws Exception {
        RestResult<Integer> restResult = new RestResult();
        List<Error> errorList = new ArrayList<>();
        try {
            restResult.setObj(fileDataGroupServiceImpl.deleteFileDataGroup(id,errorList));
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
