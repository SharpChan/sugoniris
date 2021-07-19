package com.sugon.iris.sugonweb.system;

import com.sugon.iris.sugonannotation.annotation.system.LogInCheck;
import com.sugon.iris.sugondomain.beans.baseBeans.Error;
import com.sugon.iris.sugondomain.beans.baseBeans.RestResult;
import com.sugon.iris.sugondomain.dtos.systemDtos.MenuDto;
import com.sugon.iris.sugondomain.dtos.systemDtos.OwnerMenuDto;
import com.sugon.iris.sugonservice.service.systemService.RolePageService;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/rolePage")
public class RolePageController {

    private static final String FAILED = "FAILED";
    @Resource
    private RolePageService rolePageServiceImpl;

    @PostMapping("/saveRolePage")
    @LogInCheck(doLock = true,doProcess = true)
    public RestResult<int[]> saveRolePage(@RequestBody  List<OwnerMenuDto> rolePageDtoList){
        RestResult<int[]> restResult = new RestResult();
        List<Error> errorList = new ArrayList<>();
        try{
            restResult.setObj(rolePageServiceImpl.saveRolePage(rolePageDtoList,errorList));
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

    @PostMapping("/getRolePages")
    @LogInCheck(doLock = true,doProcess = true)
    public RestResult<List<MenuDto>> getRolePages(@RequestParam(value = "id") Long id){
        RestResult<List<MenuDto>> restResult = new RestResult();
        List<Error> errorList = new ArrayList<>();
        try{
            restResult.setObj(rolePageServiceImpl.getPagesByRoleId(id,errorList));
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

    @PostMapping("/deleteRolePage")
    @LogInCheck(doLock = true,doProcess = true)
    public RestResult<int[]> deleteRolePage(@RequestBody  List<OwnerMenuDto> rolePageDtoList){
        RestResult<int[]> restResult = new RestResult();
        List<Error> errorList = new ArrayList<>();
        try{
            restResult.setObj(rolePageServiceImpl.deleteRolePage(rolePageDtoList,errorList));
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
