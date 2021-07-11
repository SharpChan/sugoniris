package com.sugon.iris.sugonweb.system;

import com.sugon.iris.sugonannotation.annotation.system.LogInCheck;
import com.sugon.iris.sugondomain.beans.baseBeans.Error;
import com.sugon.iris.sugondomain.beans.baseBeans.RestResult;
import com.sugon.iris.sugondomain.dtos.systemDtos.GroupRoleDto;
import com.sugon.iris.sugondomain.dtos.systemDtos.RoleDto;
import com.sugon.iris.sugonservice.service.systemService.GroupRoleService;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/groupRole")
public class GroupRoleController {

    private static final String FAILED = "FAILED";
    @Resource
    private GroupRoleService groupRoleServiceImpl;

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
