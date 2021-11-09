package com.sugon.iris.sugonweb.file;

import com.sugon.iris.sugonannotation.annotation.system.CurrentUser;
import com.sugon.iris.sugonannotation.annotation.system.LogInCheck;
import com.sugon.iris.sugondomain.beans.baseBeans.Error;
import com.sugon.iris.sugondomain.beans.baseBeans.RestResult;
import com.sugon.iris.sugondomain.beans.system.User;
import com.sugon.iris.sugondomain.dtos.systemDtos.MenuDto;
import com.sugon.iris.sugondomain.dtos.systemDtos.OwnerMenuDto;
import com.sugon.iris.sugonservice.service.fileService.FileDataGroupTableService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/fileDataGroupTable")
@Api(value = "数据组", tags = "数据组信息接口")
public class FileDataGroupTableController {

    private static final String FAILED = "FAILED";

    @Resource
    private FileDataGroupTableService FileDataGroupTableServiceImpl;

    /**
     * 通过数据组id获取数据组信息
     */
    @PostMapping("/getFileDataGroupTable")
    @LogInCheck(doLock = true,doProcess = true)
    @ApiOperation(value = "通过数据组id获取数据组信息")
    @ApiImplicitParam(name = "dataGroupId", value = "数据组id")
    public RestResult<List<MenuDto>> getFileDataGroup(@CurrentUser User user, @RequestParam(value = "dataGroupId") Long  dataGroupId){
        RestResult<List<MenuDto>> restResult = new RestResult();
        List<Error> errorList = new ArrayList<>();
        try{
            restResult.setObj(FileDataGroupTableServiceImpl.findDataGroupTableByUserId(user,dataGroupId,errorList));
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

    @PostMapping("/saveFileDataGroupTables")
    @LogInCheck(doLock = true,doProcess = true)
    @ApiOperation(value = "通过数据组id获取数据组信息")
    @ApiImplicitParam(name = "dataGroupId", value = "数据组id")
    public RestResult<Integer> saveFileDataGroupTables(@CurrentUser User user, @RequestBody List<OwnerMenuDto> fileUserTableList){
        RestResult<Integer> restResult = new RestResult();
        List<Error> errorList = new ArrayList<>();
        try{
            restResult.setObj(FileDataGroupTableServiceImpl.saveFileDataGroupTables(user,fileUserTableList,errorList));
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

    @PostMapping("/deleteFileDataGroupTables")
    @LogInCheck(doLock = true,doProcess = true)
    @ApiOperation(value = "删除数组和数据表的绑定关系")
    @ApiImplicitParam(name = "fileUserTableList", value = "除数组和数据表的绑定关系")
    public RestResult<Integer> deleteFileDataGroupTables(@CurrentUser User user, @RequestBody List<OwnerMenuDto> fileUserTableList){
        RestResult<Integer> restResult = new RestResult();
        List<Error> errorList = new ArrayList<>();
        try{
            restResult.setObj(FileDataGroupTableServiceImpl.removeFileDataGroupTables(user,fileUserTableList,errorList));
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
