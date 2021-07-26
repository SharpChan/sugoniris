package com.sugon.iris.sugonweb.file;

import com.sugon.iris.sugonannotation.annotation.system.CurrentUser;
import com.sugon.iris.sugonannotation.annotation.system.LogInCheck;
import com.sugon.iris.sugondomain.beans.baseBeans.Error;
import com.sugon.iris.sugondomain.beans.baseBeans.RestResult;
import com.sugon.iris.sugondomain.beans.system.User;
import com.sugon.iris.sugondomain.dtos.fileDtos.FileCaseDto;
import com.sugon.iris.sugondomain.dtos.fileDtos.FileParsingFailedDto;
import com.sugon.iris.sugonservice.service.FileService.FileImportCountService;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/fileImportCount")
public class FileImportCountController {

    private static final String FAILED = "FAILED";

    @Resource
    private FileImportCountService fileImportCountServiceImpl;


    @RequestMapping("/getImportCount")
    @LogInCheck(doLock = true,doProcess = true)
    public RestResult<List<FileCaseDto>> getImportCount(@CurrentUser User user, @RequestBody FileCaseDto fileCaseDto) {
        RestResult<List<FileCaseDto>> restResult = new RestResult();
        List<Error> errorList = new ArrayList<>();
        fileCaseDto.setUserId(user.getId());
        try{
            restResult.setObj(fileImportCountServiceImpl.getImportCount(fileCaseDto,errorList));
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

    @RequestMapping("/getFailedDetail")
    @LogInCheck(doLock = true,doProcess = true)
    public RestResult<List<FileParsingFailedDto>> getFailedDetail(@CurrentUser User user, @RequestBody  @RequestParam(value = "fileDetailId") Long  fileDetailId) {
        RestResult<List<FileParsingFailedDto>> restResult = new RestResult();
        List<Error> errorList = new ArrayList<>();
        try{
            restResult.setObj(fileImportCountServiceImpl.getFileParsingFailed(user.getId(),fileDetailId,errorList));
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
