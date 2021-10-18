package com.sugon.iris.sugonweb.file;

import com.sugon.iris.sugonannotation.annotation.system.CurrentUser;
import com.sugon.iris.sugonannotation.annotation.system.LogInCheck;
import com.sugon.iris.sugondomain.beans.baseBeans.Error;
import com.sugon.iris.sugondomain.beans.baseBeans.RestResult;
import com.sugon.iris.sugondomain.beans.system.User;
import com.sugon.iris.sugondomain.dtos.fileDtos.FileDataGroupDetailDto;
import com.sugon.iris.sugondomain.dtos.systemDtos.UserDto;
import com.sugon.iris.sugonservice.service.fileService.FileDataGroupDetailService;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/fileDataGroupDetail")
public class FileDataGroupDetailController {
    private static final String FAILED = "FAILED";

    @Resource
    private FileDataGroupDetailService fileDataGroupDetailServiceImpl;

    @RequestMapping("/findFileDataGroupUsersByGroupId")
    @LogInCheck(doLock = true,doProcess = true)
    public RestResult<List<UserDto>> findFileDataGroupUsersByGroupId(@RequestParam(value = "groupId") Long  groupId){
        RestResult<List<UserDto>> restResult = new RestResult();
        List<Error> errorList = new ArrayList<>();
        try{
            restResult.setObj(fileDataGroupDetailServiceImpl.findFileDataGroupUsersByGroupId(groupId,errorList));
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


    @RequestMapping("/findUsersNotInDataGroupsByUserId")
    @LogInCheck(doLock = true,doProcess = true)
    public RestResult<List<UserDto>> findUsersNotInDataGroupsByUserId(@RequestParam(value = "groupId") Long  groupId){
        RestResult<List<UserDto>> restResult = new RestResult();
        List<Error> errorList = new ArrayList<>();
        try{
            restResult.setObj(fileDataGroupDetailServiceImpl.findUsersNotInDataGroupsByUserId(groupId,errorList));
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

    @RequestMapping("/saveUserFromDataGroupDetail")
    @LogInCheck(doLock = true,doProcess = true)
    public RestResult<Integer> saveUserFromDataGroupDetail(@CurrentUser User user,@RequestBody List<FileDataGroupDetailDto> FileDataGroupDetailDtoList){
        RestResult<Integer> restResult = new RestResult();
        List<Error> errorList = new ArrayList<>();
        try{
            restResult.setObj(fileDataGroupDetailServiceImpl.saveUserFromDataGroupDetail(user,FileDataGroupDetailDtoList,errorList));
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

    @RequestMapping("/deleteUserFromDataGroup")
    @LogInCheck(doLock = true,doProcess = true)
    public RestResult<Integer> deleteUserFromDataGroup(@RequestBody List<FileDataGroupDetailDto> FileDataGroupDetailDtoList){
        RestResult<Integer> restResult = new RestResult();
        List<Error> errorList = new ArrayList<>();
        try{
            restResult.setObj(fileDataGroupDetailServiceImpl.deleteUserFromDataGroup(FileDataGroupDetailDtoList,errorList));
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
