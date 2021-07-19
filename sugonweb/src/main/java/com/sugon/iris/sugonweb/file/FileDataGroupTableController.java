package com.sugon.iris.sugonweb.file;

import com.sugon.iris.sugonannotation.annotation.system.CurrentUser;
import com.sugon.iris.sugonannotation.annotation.system.LogInCheck;
import com.sugon.iris.sugondomain.beans.baseBeans.Error;
import com.sugon.iris.sugondomain.beans.baseBeans.RestResult;
import com.sugon.iris.sugondomain.beans.system.User;
import com.sugon.iris.sugondomain.dtos.systemDtos.MenuDto;
import com.sugon.iris.sugondomain.dtos.systemDtos.OwnerMenuDto;
import com.sugon.iris.sugonservice.service.FileService.FileDataGroupTableService;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/fileDataGroupTable")
public class FileDataGroupTableController {

    private static final String FAILED = "FAILED";

    @Resource
    private FileDataGroupTableService FileDataGroupTableServiceImpl;

    /**
     * 查询模板信息
     */

    @RequestMapping("/getFileDataGroupTable")
    @LogInCheck(doLock = true,doProcess = true)
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

    @RequestMapping("/saveFileDataGroupTables")
    @LogInCheck(doLock = true,doProcess = true)
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

    @RequestMapping("/deleteFileDataGroupTables")
    @LogInCheck(doLock = true,doProcess = true)
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
