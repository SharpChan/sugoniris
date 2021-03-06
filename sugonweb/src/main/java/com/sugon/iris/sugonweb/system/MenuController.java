package com.sugon.iris.sugonweb.system;

import com.sugon.iris.sugonannotation.annotation.system.CurrentUser;
import com.sugon.iris.sugonannotation.annotation.system.LogInCheck;
import com.sugon.iris.sugondomain.beans.baseBeans.Error;
import com.sugon.iris.sugondomain.beans.baseBeans.RestResult;
import com.sugon.iris.sugondomain.beans.system.User;
import com.sugon.iris.sugondomain.dtos.systemDtos.MenuDto;
import com.sugon.iris.sugonservice.service.systemService.MenuService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/menu")
@Api(value = "后台管理", tags = "菜单信息接口")
public class MenuController {

    private static final String FAILED = "FAILED";

    @Resource
    private MenuService menuServiceImpl;

    /**
     * 菜单注册
     */
    @ApiImplicitParam(name = "MenuDto", value = "菜单信息")
    @ApiOperation(value = "菜单注册")
    @PostMapping("/saveMenu")
    @LogInCheck(doLock = true,doProcess = true)
    public RestResult<Integer> saveMenu(@CurrentUser User user, @RequestBody MenuDto menuDto){
        menuDto.setUserId(user.getId());
        RestResult<Integer> restResult = new RestResult();
        List<Error> errorList = new ArrayList<>();
        try{
            restResult.setObj(menuServiceImpl.saveMenu(menuDto,errorList));
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
     * 选择父节点
     */
    @ApiImplicitParam(name = "MenuDto", value = "菜单信息")
    @ApiOperation(value = "选择父节点")
    @PostMapping("/getMenu")
    @LogInCheck(doLock = true,doProcess = true)
    public RestResult<List<MenuDto>> getrMenu(@CurrentUser User user, @RequestBody MenuDto menuDto){
        menuDto.setUserId(user.getId());
        RestResult<List<MenuDto>> restResult = new RestResult();
        List<Error> errorList = new ArrayList<>();
        try{
            restResult.setObj(menuServiceImpl.getMenu(menuDto,errorList));
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
     * 获取节点信息
     */
    @ApiImplicitParam(name = "id", value = "节点id")
    @ApiOperation(value = "获取节点信息")
    @PostMapping("/getNodeInfo")
    @LogInCheck(doLock = true,doProcess = true)
    public RestResult<MenuDto> getNodeInfo(@RequestParam(value = "id") Long id){
        RestResult<MenuDto> restResult = new RestResult();
        List<Error> errorList = new ArrayList<>();
        try{
            restResult.setObj(menuServiceImpl.getNodeInfo(id,errorList));
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
     * 获取所有菜单节点
     */
    @ApiOperation(value = "获取所有菜单节点信息")
    @PostMapping("/getSiderBarMenu")
    @LogInCheck(doLock = true,doProcess = true)
    public RestResult<List<MenuDto>> getSiderBarMenu(@CurrentUser User user){
        RestResult<List<MenuDto>> restResult = new RestResult();
        List<Error> errorList = new ArrayList<>();
        try{
            restResult.setObj(menuServiceImpl.getSiderBarMenu(user.getId(),errorList));
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

    @ApiOperation(value = "获取所有菜单节点信息")
    @PostMapping("/getAllSiderBarMenu")
    @LogInCheck(doLock = true,doProcess = true)
    public RestResult<List<MenuDto>> getAllSiderBarMenu(){
        RestResult<List<MenuDto>> restResult = new RestResult();
        List<Error> errorList = new ArrayList<>();
        try{
            restResult.setObj(menuServiceImpl.getAllSiderBarMenu(errorList));
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
     * 更新菜单
     */
    @ApiImplicitParam(name = "MenuDto", value = "菜单信息")
    @ApiOperation(value = "更新菜单")
    @PostMapping("/updateMenu")
    @LogInCheck(doLock = true,doProcess = true)
    public RestResult<Integer> updateMenu(@CurrentUser User user, @RequestBody MenuDto menuDto){
        menuDto.setUserId(user.getId());
        RestResult<Integer> restResult = new RestResult();
        List<Error> errorList = new ArrayList<>();
        try{
            restResult.setObj(menuServiceImpl.modifyMenu(menuDto,errorList));
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
     * 菜单删除
     */
    @PostMapping("/deleteMenu")
    @LogInCheck(doLock = true,doProcess = true)
    @ApiImplicitParam(name = "id", value = "菜单id")
    @ApiOperation(value = "菜单删除")
    public RestResult<Integer> deleteMenudeleteMenu(@RequestParam("id") Long id){
        RestResult<Integer> restResult = new RestResult();
        List<Error> errorList = new ArrayList<>();
        try{
            restResult.setObj(menuServiceImpl.deleteMenu(id,errorList));
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
