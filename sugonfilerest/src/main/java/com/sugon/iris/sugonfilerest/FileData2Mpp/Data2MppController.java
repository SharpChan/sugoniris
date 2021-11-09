package com.sugon.iris.sugonfilerest.FileData2Mpp;

import com.sugon.iris.sugondomain.beans.baseBeans.Error;
import com.sugon.iris.sugondomain.beans.baseBeans.RestResult;
import com.sugon.iris.sugonservice.service.fileService.FileParsingService;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/data2Mpp")
public class Data2MppController {

    @Resource
    private FileParsingService fileParsingServiceImpl;

    private static final String FAILED = "FAILED";

    @RequestMapping("/uploadFile")
    public RestResult<Void> uploadFile(@RequestParam(name="userId",required = true) Long userId,@RequestParam(name="fileAttachmentId",required = true) Long fileAttachmentId) throws Exception {

        RestResult<Void> restResult = new RestResult();
        List<Error> errorList = new ArrayList<>();
        try {
            fileParsingServiceImpl.fileParsing(userId,fileAttachmentId,errorList);
        }catch(Exception e){
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

    @RequestMapping("/test")
    public RestResult<Void> test(@RequestParam(name="userId",required = true) Long userId) throws Exception {

        RestResult<Void> restResult = new RestResult();
        List<Error> errorList = new ArrayList<>();
        try {
            fileParsingServiceImpl.test(userId);
        }catch(Exception e){
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
