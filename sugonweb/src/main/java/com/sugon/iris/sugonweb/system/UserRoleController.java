package com.sugon.iris.sugonweb.system;

import com.sugon.iris.sugonannotation.annotation.system.CurrentUser;
import com.sugon.iris.sugonannotation.annotation.system.LogInCheck;
import com.sugon.iris.sugondomain.beans.baseBeans.Error;
import com.sugon.iris.sugondomain.beans.baseBeans.RestResult;
import com.sugon.iris.sugondomain.beans.system.User;
import com.sugon.iris.sugondomain.dtos.systemDtos.RoleDto;
import com.sugon.iris.sugonservice.service.systemService.UserRoleService;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/userRole")
public class UserRoleController {

    private static final String FAILED = "FAILED";

    @Resource
    private UserRoleService userRoleServiceImpl;

    @PostMapping("/saveUserRole")
    @LogInCheck(doLock = true,doProcess = true)
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

    @PostMapping("/getAllUserRole")
    @LogInCheck(doLock = true,doProcess = true)
    public RestResult<List<RoleDto>> getAllUserGroup(){
        RestResult<List<RoleDto>> restResult = new RestResult();
        List<Error> errorList = new ArrayList<>();
        try{
            restResult.setObj(userRoleServiceImpl.getAllRoles(errorList));
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
