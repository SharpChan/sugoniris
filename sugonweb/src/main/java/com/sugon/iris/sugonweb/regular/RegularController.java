package com.sugon.iris.sugonweb.regular;

import com.sugon.iris.sugonannotation.annotation.system.CurrentUser;
import com.sugon.iris.sugonannotation.annotation.system.LogInCheck;
import com.sugon.iris.sugondomain.beans.baseBeans.Error;
import com.sugon.iris.sugondomain.beans.baseBeans.RestResult;
import com.sugon.iris.sugondomain.beans.system.User;
import com.sugon.iris.sugondomain.dtos.regularDtos.RegularDetailDto;
import com.sugon.iris.sugondomain.dtos.regularDtos.RegularGroupDto;
import com.sugon.iris.sugonservice.service.regularService.RegularDetailService;
import com.sugon.iris.sugonservice.service.regularService.RegularService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/regular")
@Api(value = "正则表达式", tags = "正则表达式相关接口")
public class RegularController {

    private static final String FAILED = "FAILED";

    @Resource
    private RegularService regularServiceImpl;

    @Resource
    private RegularDetailService regularDetailServiceImpl;

    @PostMapping("/groupAdd")
    @LogInCheck(doLock = true,doProcess = true)
    @ApiOperation(value = "清洗正则表达式组新增")
    @ApiImplicitParam(name = "regularGroupDto", value = "清洗正则表达式组信息")
    public RestResult<Integer> groupAdd(@CurrentUser User user, @RequestBody RegularGroupDto regularGroupDto){
        RestResult<Integer> restResult = new RestResult();
        List<Error> errorList = new ArrayList<>();
        regularGroupDto.setUserId(user.getId());
        try{
            restResult.setObj(regularServiceImpl.addGroup(regularGroupDto,errorList));
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

    @PostMapping("/groupRemoveByPrimaryKey")
    @LogInCheck(doLock = true,doProcess = true)
    @ApiOperation(value = "通过id删除正则表达式组")
    @ApiImplicitParam(name = "id", value = "id")
    public RestResult<Integer> groupRemoveByPrimaryKey(@CurrentUser User user,Long id){
        RestResult<Integer> restResult = new RestResult();
        List<Error> errorList = new ArrayList<>();
        try{
            restResult.setObj(regularServiceImpl.deleteGroupByPrimaryKey(id,errorList));
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

    @PostMapping("/getGroups")
    @LogInCheck(doLock = true,doProcess = true)
    @ApiOperation(value = "通过用户id获取正则表达式组")
    public RestResult<List<RegularGroupDto>> findRegularGroupByUserId(@CurrentUser User user){
        RestResult<List<RegularGroupDto>> restResult = new RestResult();
        List<Error> errorList = new ArrayList<>();
        try{
            restResult.setObj(regularServiceImpl.findRegularGroup(user.getId(),errorList));
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

    /*
    @PostMapping("/deleteGroupByPrimaryKey")
    @LogInCheck(doLock = true,doProcess = true)
    @ApiOperation(value = "通过id删除清洗组")
    @ApiImplicitParam(name = "id", value = "id")
    public RestResult<Integer> deleteGroupByPrimaryKey(@CurrentUser User user,Long id){
        RestResult<Integer> restResult = new RestResult();
        List<Error> errorList = new ArrayList<>();
        try{
            restResult.setObj(regularServiceImpl.deleteGroupByPrimaryKey(id,errorList));
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
  */
    @PostMapping("/updateGroupByPrimaryKey")
    @LogInCheck(doLock = true,doProcess = true)
    @ApiOperation(value = "修改正则表达式组基础信息")
    @ApiImplicitParam(name = "regularGroupDto", value = "清洗组信息")
    public RestResult<Integer> updateGroupByPrimaryKey(@CurrentUser User user,@RequestBody RegularGroupDto regularGroupDto){
        RestResult<Integer> restResult = new RestResult();
        List<Error> errorList = new ArrayList<>();
        try{
            restResult.setObj(regularServiceImpl.modifyGroup(regularGroupDto,errorList));
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

    @PostMapping("/getRegularDetailsByGroupId")
    @LogInCheck(doLock = true,doProcess = true)
    @ApiOperation(value = "通过id获取组内详细信息")
    @ApiImplicitParam(name = "regularGroupId", value = "清洗组id")
    public RestResult<List<RegularDetailDto>> getRegularDetails(@CurrentUser User user, Long regularGroupId){
        RestResult<List<RegularDetailDto>> restResult = new RestResult();
        List<Error> errorList = new ArrayList<>();
        try{
           restResult.setObj(regularDetailServiceImpl.findRegularDetailsByGroupId(regularGroupId,errorList));
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

    @PostMapping("/getRegularDetailsByUserId")
    @LogInCheck(doLock = true,doProcess = true)
    @ApiOperation(value = "通过用户id获取组内详细信息")
    public RestResult<List<String>> getRegularDetailsByUserId(@CurrentUser User user){
        RestResult<List<String>> restResult = new RestResult();
        List<Error> errorList = new ArrayList<>();
        try{
            restResult.setObj(regularDetailServiceImpl.findRegularDetailsByUserId(user.getId(),errorList));
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

    @PostMapping("/addRegularDetails")
    @LogInCheck(doLock = true,doProcess = true)
    @ApiOperation(value = "新增正则表达式规则")
    @ApiImplicitParam(name = "regularDetailDto", value = "正则表达式信息")
    public RestResult<Integer> addRegularDetails(@CurrentUser User user, @RequestBody  RegularDetailDto regularDetailDto){
        RestResult<Integer> restResult = new RestResult();
        List<Error> errorList = new ArrayList<>();
        regularDetailDto.setUserId(user.getId());
        try{
            restResult.setObj(regularDetailServiceImpl.addDetail(regularDetailDto,errorList));
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

    @PostMapping("/modifyRegularDetails")
    @LogInCheck(doLock = true,doProcess = true)
    @ApiOperation(value = "修改正则表达式规则")
    @ApiImplicitParam(name = "regularDetailDto", value = "正则表达式信息")
    public RestResult<Integer> modifyRegularDetails(@CurrentUser User user, @RequestBody  RegularDetailDto regularDetailDto){
        RestResult<Integer> restResult = new RestResult();
        List<Error> errorList = new ArrayList<>();
        regularDetailDto.setUserId(user.getId());
        try{
            restResult.setObj(regularDetailServiceImpl.modifyDetailByPrimaryKey(regularDetailDto,errorList));
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

    @PostMapping("/removeRegularDetailByPrimaryKey")
    @LogInCheck(doLock = true,doProcess = true)
    @ApiOperation(value = "删除正则表达式规则")
    @ApiImplicitParam(name = "id", value = "正则表达式信息id")
    public RestResult<Integer> removeRegularDetailByPrimaryKey(@CurrentUser User user,Long id){
        RestResult<Integer> restResult = new RestResult();
        List<Error> errorList = new ArrayList<>();
        try{
            restResult.setObj(regularDetailServiceImpl.deleteDetailByPrimaryKey(id,errorList));
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
