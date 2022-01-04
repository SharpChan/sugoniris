package com.sugon.iris.sugonrest.Template;

import com.sugon.iris.sugondomain.beans.baseBeans.Error;
import com.sugon.iris.sugondomain.beans.baseBeans.RestResult;
import com.sugon.iris.sugondomain.dtos.fileDtos.FileTemplateDto;
import com.sugon.iris.sugondomain.enums.ErrorCode_Enum;
import com.sugon.iris.sugonservice.service.fileService.FileTemplateService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/templateInfo")
@Api(value = "对外接口-模板", tags = "模板信息")
public class TemplateInfosController {

    private static final String FAILED = "FAILED";

    @Resource
    private FileTemplateService fileTemplateServiceImpl;

    @GetMapping("/getTemplateAndDetails")
    @ApiOperation(value = "模板信息")
    public RestResult<List<FileTemplateDto>> getAllTemplateInfos() throws IllegalAccessException{
        RestResult<List<FileTemplateDto>> restResult = new RestResult();
        List<Error> errorList = new ArrayList<>();
        try {
            restResult.setObj(fileTemplateServiceImpl.getAllFileTemplateDtoAndDetailsList(errorList));
        }catch (Exception e){
            e.printStackTrace();
            errorList.add(new Error(ErrorCode_Enum.SYS_SUGON_001.getCode(),ErrorCode_Enum.SYS_SUGON_001.getMessage()));
        }
        if(!CollectionUtils.isEmpty(errorList)){
            restResult.setFlag(FAILED);
            restResult.setErrorList(errorList);
        }
        return restResult;
    }

}
