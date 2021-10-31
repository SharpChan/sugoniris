package com.sugon.iris.sugonweb.system;

import com.sugon.iris.sugonannotation.annotation.system.CurrentUser;
import com.sugon.iris.sugonannotation.annotation.system.LogInCheck;
import com.sugon.iris.sugondomain.beans.baseBeans.Error;
import com.sugon.iris.sugondomain.beans.baseBeans.RestResult;
import com.sugon.iris.sugondomain.beans.system.User;
import com.sugon.iris.sugondomain.dtos.systemDtos.RoleDto;
import com.sugon.iris.sugonservice.service.systemService.UserRoleService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/userRole")
@Api(value = "后台管理", tags = "用户角色相关接口")
public class UserRoleController {

    private static final String FAILED = "FAILED";

    @Resource
    private UserRoleService userRoleServiceImpl;

    @PostMapping("/saveUserRole")
    @LogInCheck(doLock = true,doProcess = true)
    @ApiOperation(value = "保存角色信息")
    @ApiImplicitParam(name = "roleDto", value = "角色信息")
    public RestResult<Integer> saveUserRole(@CurrentUser User user, @RequestBody RoleDto roleDto){
        roleDto.setCreate_user_id(user.getId());
        RestResult<Integer> restResult = new RestResult();
        List<Error> errorList = new ArrayList<>();
        try{
            restResult.setObj(userRoleServiceImpl.saveRole(roleDto,errorList));
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

    @PostMapping("/getUserRole")
    @LogInCheck(doLock = true,doProcess = true)
    @ApiOperation(value = "获取角色信息列表")
    @ApiImplicitParam(name = "roleDto", value = "角色信息")
    public RestResult<List<RoleDto>> getAllUserGroup(@RequestBody RoleDto roleDto){
        RestResult<List<RoleDto>> restResult = new RestResult();
        List<Error> errorList = new ArrayList<>();
        try{
            restResult.setObj(userRoleServiceImpl.getAllRoles(roleDto,errorList));
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

    @PostMapping("/modifyUserRole")
    @LogInCheck(doLock = true,doProcess = true)
    @ApiOperation(value = "修改角色信息")
    @ApiImplicitParam(name = "roleDto", value = "角色信息")
    public RestResult<Integer> modifyUserGroup( @RequestBody RoleDto roleDto){
        RestResult<Integer> restResult = new RestResult();
        List<Error> errorList = new ArrayList<>();
        try{
            restResult.setObj(userRoleServiceImpl.modifyRole(roleDto,errorList));
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

    @PostMapping("/removeUserRole")
    @LogInCheck(doLock = true,doProcess = true)
    @ApiOperation(value = "删除角色信息")
    @ApiImplicitParam(name = "id", value = "角色id")
    public RestResult<Integer> removeUserGroup( @RequestParam(value = "id") Long id){
        RestResult<Integer> restResult = new RestResult();
        List<Error> errorList = new ArrayList<>();
        try{
            restResult.setObj(userRoleServiceImpl.deleteRole(id,errorList));
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
