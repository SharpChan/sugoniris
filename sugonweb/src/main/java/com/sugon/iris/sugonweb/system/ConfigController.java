package com.sugon.iris.sugonweb.system;

import com.sugon.iris.sugonannotation.annotation.system.CurrentUser;
import com.sugon.iris.sugonannotation.annotation.system.LogInCheck;
import com.sugon.iris.sugondomain.beans.baseBeans.RestResult;
import com.sugon.iris.sugondomain.beans.configBeans.ConfigBean;
import com.sugon.iris.sugondomain.beans.system.User;
import com.sugon.iris.sugondomain.dtos.configDtos.ConfigDto;
import com.sugon.iris.sugondomain.beans.baseBeans.Error;
import com.sugon.iris.sugonservice.service.configService.ConfigService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/config")
@Api(value = "后台管理", tags = "系统配置相关的接口", description = "系统参数配置，参数被存入PublicUtils类的configMap属性")
public class ConfigController {

    private static final Logger LOGGER = LogManager.getLogger(ConfigController.class);

    private static final String FAILED = "FAILED";

    @Autowired
    private ConfigService configServiceImpl;

    @PostMapping("/saveConfig")
    @LogInCheck(doLock = true,doProcess = true)
    @ApiImplicitParam(name = "configDto", value = "配置信息")
    @ApiOperation(value = "配置信息新增")
    public RestResult<Integer> saveConfig(@CurrentUser User user, @RequestBody ConfigDto configDto){
        configDto.setUserName(String.valueOf(user.getId()));
        RestResult<Integer> restResult = new RestResult();
        List<Error> errorList = new ArrayList<>();
        try{
            restResult.setObj(configServiceImpl.saveConfig(configDto,errorList));
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

    @ApiOperation(value = "获取所有的配置信息")
    @PostMapping("/getAllConfigs")
    @LogInCheck(doLock = true,doProcess = true)
    public RestResult<List<ConfigBean>> getAllConfigs(){
        RestResult<List<ConfigBean>> restResult = new RestResult();
        List<Error> errorList = new ArrayList<>();
        try{
            restResult.setObj(configServiceImpl.getAllConfigs(errorList));
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



    //方法参数说明，name参数名；value参数说明，备注；dataType参数类型；required 是否必传；defaultValue 默认值
    @ApiImplicitParam(name = "configDto", value = "配置信息")
    //说明是什么方法(可以理解为方法注释)
    @ApiOperation(value = "配置信息更新")
    @PostMapping("/updateConfig")
    @LogInCheck(doLock = true,doProcess = true)
    public RestResult<Integer> updateConfig(@CurrentUser User user,@RequestBody ConfigDto configDto){
        configDto.setUserName(String.valueOf(user.getId()));
        RestResult<Integer> restResult = new RestResult();
        List<Error> errorList = new ArrayList<>();
        try{
            restResult.setObj(configServiceImpl.updateConfig(configDto,errorList));
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

    @ApiImplicitParam(name = "id", value = "配置信息id")
    //说明是什么方法(可以理解为方法注释)
    @ApiOperation(value = "配置信息删除")
    @PostMapping("/deleteConfig")
    @LogInCheck(doLock = true,doProcess = true)
    public RestResult<Integer> deleteConfig(@RequestParam(value = "id") Long id) {
        RestResult<Integer> restResult = new RestResult();
        List<Error> errorList = new ArrayList<>();
        try {
            restResult.setObj(configServiceImpl.deleteConfig(id,errorList));
        }catch(Exception e){
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
