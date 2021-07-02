package com.sugon.iris.sugonweb.system;

import com.sugon.iris.sugonannotation.annotation.system.CurrentUser;
import com.sugon.iris.sugonannotation.annotation.system.LogInCheck;
import com.sugon.iris.sugondomain.beans.baseBeans.RestResult;
import com.sugon.iris.sugondomain.beans.configBeans.ConfigBean;
import com.sugon.iris.sugondomain.beans.userBeans.User;
import com.sugon.iris.sugondomain.dtos.configDtos.ConfigDto;
import com.sugon.iris.sugondomain.beans.baseBeans.Error;
import com.sugon.iris.sugonservice.service.configService.ConfigService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/config")
public class ConfigController {

    private static final Logger LOGGER = LogManager.getLogger(ConfigController.class);

    private static final String FAILED = "FAILED";

    @Autowired
    private ConfigService configServiceImpl;

    @PostMapping("/saveConfig")
    @LogInCheck(doLock = true,doProcess = true)
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
            restResult.setErrorList(errorList);
            return  restResult;
        }
        return restResult;
    }

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
            restResult.setErrorList(errorList);
            return  restResult;
        }
        return restResult;
    }



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
            restResult.setErrorList(errorList);
            return  restResult;
        }
        return restResult;
    }

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
            restResult.setMessage("删除失败！");
            restResult.setErrorList(errorList);
        }else{
            restResult.setMessage("删除成功");
        }
        return restResult;
    }
}
