package com.sugon.iris.sugonweb.system;

import com.sugon.iris.sugonannotation.annotation.system.LogInCheck;
import com.sugon.iris.sugondomain.beans.baseBeans.Error;
import com.sugon.iris.sugondomain.beans.baseBeans.RestResult;
import com.sugon.iris.sugondomain.dtos.systemDtos.GroupRoleDto;
import com.sugon.iris.sugondomain.dtos.systemDtos.RoleDto;
import com.sugon.iris.sugonservice.service.systemService.GroupRoleService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/groupRole")
@Api(value = "后台管理", tags = "用户组角色对应关系相关的接口", description = "用户组角色对应关系相关信息")
public class GroupRoleController {

    private static final String FAILED = "FAILED";
    @Resource
    private GroupRoleService groupRoleServiceImpl;


    @ApiImplicitParam(name = "GroupRoleDto", value = "用户组角色对应关系")
    @ApiOperation(value = "用户组角色对应关系新增保存", notes = "用户组角色对应关系新增")
    @PostMapping("/saveGroupRole")
    @LogInCheck(doLock = true,doProcess = true)
    public RestResult<Integer> saveGroupRole(@RequestBody  GroupRoleDto groupRoleDto){
        RestResult<Integer> restResult = new RestResult();
        List<Error> errorList = new ArrayList<>();
        try{
            restResult.setObj(groupRoleServiceImpl.saveGroupRole(groupRoleDto,errorList));
        }catch (Exception e){
            e.printStackTrace();
        }
        if(!CollectionUtils.isEmpty(errorList)){
            restResult.setFlag(FAILED);
            restResult.setMessage("操作失败！");
            restResult.setErrorList(errorList);
        }else{
            restResult.setMessage("操作成功");
        }
        return restResult;
    }

    @ApiOperation(value = "获取用户组角色对应关系", notes = "获取用户组角色对应关系")
    @PostMapping("/getGroupRole")
    @LogInCheck(doLock = true,doProcess = true)
    public RestResult<List<RoleDto>> getGroupRole(@RequestParam(value = "id") Long id){
        RestResult<List<RoleDto>> restResult = new RestResult();
        List<Error> errorList = new ArrayList<>();
        try{
            restResult.setObj(groupRoleServiceImpl.getGroupRoleByGroupId(id,errorList));
        }catch (Exception e){
            e.printStackTrace();
        }
        if(!CollectionUtils.isEmpty(errorList)){
            restResult.setFlag(FAILED);
            restResult.setMessage("操作失败！");
            restResult.setErrorList(errorList);
        }else{
            restResult.setMessage("操作成功");
        }
        return restResult;
    }


    @ApiImplicitParam(name = "GroupRoleDto", value = "用户组角色对应关系")
    @ApiOperation(value = "删除用户组角色对应关系", notes = "删除用户组角色对应关系")
    @PostMapping("/deleteGroupRole")
    @LogInCheck(doLock = true,doProcess = true)
    public RestResult<Integer> deleteGroupRole(@RequestBody  GroupRoleDto groupRoleDto){
        RestResult<Integer> restResult = new RestResult();
        List<Error> errorList = new ArrayList<>();
        try{
            restResult.setObj(groupRoleServiceImpl.deleteGroupRole(groupRoleDto,errorList));
        }catch (Exception e){
            e.printStackTrace();
        }
        if(!CollectionUtils.isEmpty(errorList)){
            restResult.setFlag(FAILED);
            restResult.setMessage("操作失败！");
            restResult.setErrorList(errorList);
        }else{
            restResult.setMessage("操作成功");
        }
        return restResult;
    }
}
