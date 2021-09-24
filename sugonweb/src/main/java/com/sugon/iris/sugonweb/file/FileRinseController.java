package com.sugon.iris.sugonweb.file;

import com.sugon.iris.sugonannotation.annotation.system.CurrentUser;
import com.sugon.iris.sugonannotation.annotation.system.LogInCheck;
import com.sugon.iris.sugondomain.beans.baseBeans.Error;
import com.sugon.iris.sugondomain.beans.baseBeans.RestResult;
import com.sugon.iris.sugondomain.beans.system.User;
import com.sugon.iris.sugondomain.dtos.fileDtos.FileRinseDto;
import com.sugon.iris.sugonservice.service.FileService.FileRinseService;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/fileRinse")
public class FileRinseController {

    private static final String FAILED = "FAILED";

    @Resource
    private FileRinseService FileRinseServiceImpl;

    @RequestMapping("/addFileRinse")
    @LogInCheck(doLock = true,doProcess = true)
    public RestResult<Integer> addFileRinse(@CurrentUser User user, @RequestBody FileRinseDto fileRinseDto) {
        RestResult<Integer> restResult = new RestResult();
        List<Error> errorList = new ArrayList<>();
        fileRinseDto.setUserId(user.getId());
        try{
            restResult.setObj(FileRinseServiceImpl.add(fileRinseDto,errorList));
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

    @RequestMapping("/getFileRinses")
    @LogInCheck(doLock = true,doProcess = true)
    public RestResult<List<FileRinseDto>> getFileRinses(@CurrentUser User user) {
        RestResult<List<FileRinseDto>> restResult = new RestResult();
        List<Error> errorList = new ArrayList<>();
        try{
            restResult.setObj(FileRinseServiceImpl.findFileRinseByUserId(user.getId(),errorList));
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

    @RequestMapping("/modifyFileRinse")
    @LogInCheck(doLock = true,doProcess = true)
    public RestResult<Integer> modifyFileRinse(@CurrentUser User user, @RequestBody FileRinseDto fileRinseDto) {
        RestResult<Integer> restResult = new RestResult();
        List<Error> errorList = new ArrayList<>();
        fileRinseDto.setUserId(user.getId());
        try{
            restResult.setObj(FileRinseServiceImpl.modifyFileRinse(fileRinseDto,errorList));
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

    @RequestMapping("/deleteFileRinse")
    @LogInCheck(doLock = true,doProcess = true)
    public RestResult<Integer> deleteFileRinse(@CurrentUser User user, long id) {
        RestResult<Integer> restResult = new RestResult();
        List<Error> errorList = new ArrayList<>();
        try{
            restResult.setObj(FileRinseServiceImpl.deleteFileRinse(id,errorList));
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
