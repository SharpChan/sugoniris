package com.sugon.iris.sugonweb.system;

import com.sugon.iris.sugonannotation.annotation.system.LogInCheck;
import com.sugon.iris.sugondomain.beans.baseBeans.Error;
import com.sugon.iris.sugondomain.beans.baseBeans.RestResult;
import com.sugon.iris.sugondomain.dtos.systemDtos.TranslateDto;
import com.sugon.iris.sugonservice.service.systemService.TranslateService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/translate")
@Api(value = "后台管理", tags = "系统翻译")
public class TranslateController {

    private static final String FAILED = "FAILED";

    @Resource
    private TranslateService translateServiceImpl;

    @PostMapping("/getTranslate")
    @ApiOperation(value = "获取翻译信息")
    @ApiImplicitParam(name = "tsType", value = "翻译类型")
    public RestResult<Map<String,?>> getTranslate(@RequestParam(value = "tsType") String tsType){
        RestResult<Map<String,?>> restResult = new RestResult();
        List<Error> errorList = new ArrayList<>();
        try{
            restResult.setObj(translateServiceImpl.getTranslateList(tsType,errorList));
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
