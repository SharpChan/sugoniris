package com.sugon.iris.sugonweb.system;


import com.sugon.iris.sugonannotation.annotation.system.BussLog;
import com.sugon.iris.sugonannotation.annotation.system.CurrentUser;
import com.sugon.iris.sugonannotation.annotation.system.LogInCheck;
import com.sugon.iris.sugondomain.beans.baseBeans.Error;
import com.sugon.iris.sugondomain.beans.baseBeans.RestResult;
import com.sugon.iris.sugondomain.beans.system.User;
import com.sugon.iris.sugondomain.dtos.configDtos.ConfigDto;
import com.sugon.iris.sugondomain.dtos.systemDtos.BusinessLogDto;
import com.sugon.iris.sugonservice.service.systemService.BussLogService;
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
@RequestMapping("/bussLog")
public class BussLogController {

    private static final String FAILED = "FAILED";

    @Resource
    private BussLogService bussLogServiceImpl;

    @PostMapping("/queryLogs")
    @LogInCheck(doLock = true,doProcess = true)
    @ApiOperation(value = "查看系统日志")
    public RestResult<List<BusinessLogDto>> saveConfig(@CurrentUser User user){
        RestResult<List<BusinessLogDto>> restResult = new RestResult();
        List<Error> errorList = new ArrayList<>();
        try{
            restResult.setObj(bussLogServiceImpl.queryLogs(errorList));
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
