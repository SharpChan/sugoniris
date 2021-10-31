package com.sugon.iris.sugonweb.system;

import com.sugon.iris.sugonannotation.annotation.system.CurrentUser;
import com.sugon.iris.sugonannotation.annotation.system.LogInCheck;
import com.sugon.iris.sugondomain.beans.baseBeans.Error;
import com.sugon.iris.sugondomain.beans.baseBeans.RestResult;
import com.sugon.iris.sugondomain.beans.system.User;
import com.sugon.iris.sugondomain.dtos.systemDtos.UserDto;
import com.sugon.iris.sugondomain.dtos.systemDtos.UserGroupDetailDto;
import com.sugon.iris.sugondomain.dtos.systemDtos.UserGroupDto;
import com.sugon.iris.sugonservice.service.systemService.UserGroupService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/userGroup")
@Api(value = "后台管理", tags = "用户组")
public class UserGroupController {

    private static final String FAILED = "FAILED";

    @Resource
    private UserGroupService userGroupServiceImpl;

    @PostMapping("/saveUserGroup")
    @LogInCheck(doLock = true,doProcess = true)
    @ApiOperation(value = "保存用户组")
    @ApiImplicitParam(name = "userGroupDto", value = "用户组信息")
    public RestResult<Integer> saveUserGroup(@CurrentUser User user, @RequestBody UserGroupDto userGroupDto){
        userGroupDto.setUserId(user.getId());
        RestResult<Integer> restResult = new RestResult();
        List<Error> errorList = new ArrayList<>();
        try{
            restResult.setObj(userGroupServiceImpl.addUserGroup(userGroupDto,errorList));
        }catch (Exception e){
            e.printStackTrace();
        }
        if(!CollectionUtils.isEmpty(errorList)){
            restResult.setFlag(FAILED);
            restResult.setErrorList(errorList);
            restResult.setMessage("操作失败");
            return  restResult;
        }else{
            restResult.setMessage("操作成功");
        }
        return restResult;
    }

    @PostMapping("/getAllUserGroup")
    @LogInCheck(doLock = true,doProcess = true)
    @ApiOperation(value = "获取所有的用户组信息")
    public RestResult<List<UserGroupDto>> getAllUserGroup(){
        RestResult<List<UserGroupDto>> restResult = new RestResult();
        List<Error> errorList = new ArrayList<>();
        try{
            restResult.setObj(userGroupServiceImpl.userGroupList(errorList));
        }catch (Exception e){
            e.printStackTrace();
        }
        if(!CollectionUtils.isEmpty(errorList)){
            restResult.setFlag(FAILED);
            restResult.setErrorList(errorList);
            restResult.setMessage("操作失败");
            return  restResult;
        }else{
            restResult.setMessage("操作成功");
        }
        return restResult;
    }

    @PostMapping("/getUserGroupById")
    @LogInCheck(doLock = true,doProcess = true)
    @ApiOperation(value = "通过id获取用户组信息")
    @ApiImplicitParam(name = "id", value = "用户组id")
    public RestResult<UserGroupDto> getUserGroupById(@RequestParam(value = "id") Long id){
        RestResult<UserGroupDto> restResult = new RestResult();
        List<Error> errorList = new ArrayList<>();
        try{
            restResult.setObj(userGroupServiceImpl.userGroupById(id,errorList));
        }catch (Exception e){
            e.printStackTrace();
        }
        if(!CollectionUtils.isEmpty(errorList)){
            restResult.setFlag(FAILED);
            restResult.setErrorList(errorList);
            restResult.setMessage("操作失败");
            return  restResult;
        }else{
            restResult.setMessage("操作成功");
        }
        return restResult;
    }

    @PostMapping("/modifyUserGroup")
    @LogInCheck(doLock = true,doProcess = true)
    @ApiOperation(value = "修改用户组信息")
    @ApiImplicitParam(name = "userGroupDto", value = "用户组信息")
    public RestResult<Integer> modifyUserGroup( @RequestBody UserGroupDto userGroupDto){
        RestResult<Integer> restResult = new RestResult();
        List<Error> errorList = new ArrayList<>();
        try{
            restResult.setObj(userGroupServiceImpl.modifyUserGroup(userGroupDto,errorList));
        }catch (Exception e){
            e.printStackTrace();
        }
        if(!CollectionUtils.isEmpty(errorList)){
            restResult.setFlag(FAILED);
            restResult.setErrorList(errorList);
            restResult.setMessage("操作失败");
            return  restResult;
        }else{
            restResult.setMessage("操作成功");
        }
        return restResult;
    }

    @PostMapping("/removeUserGroup")
    @LogInCheck(doLock = true,doProcess = true)
    @ApiOperation(value = "删除用户组信息")
    @ApiImplicitParam(name = "id", value = "用户组id")
    public RestResult<Integer> removeUserGroup( @RequestParam(value = "id") Long id){
        RestResult<Integer> restResult = new RestResult();
        List<Error> errorList = new ArrayList<>();
        try{
            restResult.setObj(userGroupServiceImpl.removeUserGroup(id,errorList));
        }catch (Exception e){
            e.printStackTrace();
        }
        if(!CollectionUtils.isEmpty(errorList)){
            restResult.setFlag(FAILED);
            restResult.setErrorList(errorList);
            restResult.setMessage("操作失败");
            return  restResult;
        }else{
            restResult.setMessage("操作成功");
        }
        return restResult;
    }

    /**
     * 获取未分配到该组的所有用户
     */
    @PostMapping("/getUsers")
    @LogInCheck(doLock = true,doProcess = true)
    @ApiOperation(value = "获取未分配到该组的所有用户")
    @ApiImplicitParam(name = "userGroupId", value = "用户组id")
    public RestResult<List<UserDto>> getUsers(@RequestParam(value = "userGroupId") long userGroupId ){
        RestResult<List<UserDto>> restResult = new RestResult<List<UserDto>>();
        List<Error> errorList = new ArrayList<>();
        try {
            restResult.setObj(userGroupServiceImpl.getUsers(userGroupId,errorList));
        }catch (Exception e){
            e.printStackTrace();
        }
        if(!CollectionUtils.isEmpty(errorList)){
            restResult.setFlag(FAILED);
            restResult.setMessage("操作失败");
            restResult.setErrorList(errorList);
        }else{
            restResult.setMessage("操作成功");
        }
        return restResult;
    }

    /**
     * 获取该用户组的所有用户
     */
    @PostMapping("/getGroupUsers")
    @LogInCheck(doLock = true,doProcess = true)
    @ApiOperation(value = "获取该用户组的所有用户")
    @ApiImplicitParam(name = "userGroupId", value = "用户组id")
        public RestResult<List<UserDto>> getGroupUsers(@RequestParam(value = "userGroupId") long userGroupId ){
            RestResult<List<UserDto>> restResult = new RestResult<List<UserDto>>();
            List<Error> errorList = new ArrayList<>();
            try {
                restResult.setObj(userGroupServiceImpl.getGroupUsers(userGroupId,errorList));
            }catch (Exception e){
                e.printStackTrace();
            }
            if(!CollectionUtils.isEmpty(errorList)){
                restResult.setFlag(FAILED);
                restResult.setMessage("操作失败");
                restResult.setErrorList(errorList);
            }else{
                restResult.setMessage("操作成功");
            }
            return restResult;
    }


    @PostMapping("/addUserToUserGroup")
    @LogInCheck(doLock = true,doProcess = true)
    @ApiOperation(value = "用户添加到用户组")
    @ApiImplicitParam(name = "userGroupDetailDto", value = "用户用户组对应信息")
    public RestResult<Integer> addUserToUserGroup(@CurrentUser User user, @RequestBody UserGroupDetailDto userGroupDetailDto){
        RestResult<Integer> restResult = new RestResult();
        List<Error> errorList = new ArrayList<>();
        userGroupDetailDto.setCreateUserId(user.getId());
        try{
            restResult.setObj(userGroupServiceImpl.addUserToUserGroup(userGroupDetailDto,errorList));
        }catch (Exception e){
            e.printStackTrace();
        }
        if(!CollectionUtils.isEmpty(errorList)){
            restResult.setFlag(FAILED);
            restResult.setErrorList(errorList);
            restResult.setMessage("操作失败");
            return  restResult;
        }else{
            restResult.setMessage("操作成功");
        }
        return restResult;
    }

    @PostMapping("/addUserToUserGroupBatch")
    @LogInCheck(doLock = true,doProcess = true)
    @ApiOperation(value = "批量用户添加到用户组")
    @ApiImplicitParam(name = "userGroupDetailDtoArr", value = "用户用户组对应信息列表")
    public RestResult<Integer> addUserToUserGroupBatch(@CurrentUser User user,@RequestBody  List<UserGroupDetailDto> userGroupDetailDtoArr){
        RestResult<Integer> restResult = new RestResult();
        List<Error> errorList = new ArrayList<>();
        try{
            restResult.setObj(userGroupServiceImpl.addUserToUserGroupBatch(user,userGroupDetailDtoArr,errorList));
        }catch (Exception e){
            e.printStackTrace();
        }
        if(!CollectionUtils.isEmpty(errorList)){
            restResult.setFlag(FAILED);
            restResult.setErrorList(errorList);
            restResult.setMessage("操作失败");
            return  restResult;
        }else{
            restResult.setMessage("操作成功");
        }
        return restResult;
    }

    @PostMapping("/removeUserFromUserGroup")
    @LogInCheck(doLock = true,doProcess = true)
    @ApiOperation(value = "用户从用户组中移除")
    @ApiImplicitParam(name = "userGroupDetailDto", value = "用户组内详细信息")
    public RestResult<Integer> removeUserFromUserGroup(@RequestBody UserGroupDetailDto userGroupDetailDto){
        RestResult<Integer> restResult = new RestResult();
        List<Error> errorList = new ArrayList<>();
        try{
            restResult.setObj(userGroupServiceImpl.removeUserFromUserGroup(userGroupDetailDto,errorList));
        }catch (Exception e){
            e.printStackTrace();
        }
        if(!CollectionUtils.isEmpty(errorList)){
            restResult.setFlag(FAILED);
            restResult.setErrorList(errorList);
            restResult.setMessage("操作失败");
            return  restResult;
        }else{
            restResult.setMessage("操作成功");
        }
        return restResult;
    }

    @PostMapping("/removeUserFromUserGroupBatch")
    @LogInCheck(doLock = true,doProcess = true)
    @ApiOperation(value = "批量用户从用户组中移除")
    @ApiImplicitParam(name = "userGroupDetailDto", value = "用户组内详细信息列表")
    public RestResult<Integer> removeUserFromUserGroupBatch(@RequestBody List<UserGroupDetailDto> userGroupDetailDtoArr){
        RestResult<Integer> restResult = new RestResult();
        List<Error> errorList = new ArrayList<>();
        try{
            restResult.setObj(userGroupServiceImpl.removeUserFromUserGroupBatch(userGroupDetailDtoArr,errorList));
        }catch (Exception e){
            e.printStackTrace();
        }
        if(!CollectionUtils.isEmpty(errorList)){
            restResult.setFlag(FAILED);
            restResult.setErrorList(errorList);
            restResult.setMessage("操作失败");
            return  restResult;
        }else{
            restResult.setMessage("操作成功");
        }
        return restResult;
    }
}
