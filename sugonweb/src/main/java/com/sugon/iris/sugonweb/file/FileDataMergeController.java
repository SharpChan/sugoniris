package com.sugon.iris.sugonweb.file;

import com.sugon.iris.sugonannotation.annotation.system.CurrentUser;
import com.sugon.iris.sugonannotation.annotation.system.LogInCheck;
import com.sugon.iris.sugondomain.beans.baseBeans.Error;
import com.sugon.iris.sugondomain.beans.baseBeans.RestResult;
import com.sugon.iris.sugondomain.beans.system.User;
import com.sugon.iris.sugondomain.dtos.fileDtos.FileCaseDto;
import com.sugon.iris.sugondomain.dtos.fileDtos.MppSearchDto;
import com.sugon.iris.sugondomain.dtos.fileDtos.MppTableDto;
import com.sugon.iris.sugonservice.service.fileService.FileDataMergeService;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/dataMerge")
public class FileDataMergeController {

    private static final String FAILED = "FAILED";

    @Resource
    private FileDataMergeService fileDataMergeServiceImpl;


    @RequestMapping("/getCases")
    @LogInCheck(doLock = true,doProcess = true)
    public RestResult<List<FileCaseDto>> getCases(@CurrentUser User user){
        RestResult<List<FileCaseDto>> restResult = new RestResult();
        List<Error> errorList = new ArrayList<>();
        try{
            restResult.setObj(fileDataMergeServiceImpl.getCases(user.getId(),errorList));
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

    @RequestMapping("/getTableRecord")
    @LogInCheck(doLock = true,doProcess = true)
    public RestResult<MppTableDto> getCases(@CurrentUser User user,@RequestBody MppSearchDto mppSearchDto){
        RestResult<MppTableDto> restResult = new RestResult();
        List<Error> errorList = new ArrayList<>();
        try{
            restResult.setObj(fileDataMergeServiceImpl.getTableRecord( mppSearchDto, errorList));
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


    @RequestMapping("/getTableRecordQuantity")
    @LogInCheck(doLock = true,doProcess = true)
    public RestResult<Integer> getTableRecordQuantity(@CurrentUser User user,@RequestBody MppSearchDto mppSearchDto){
        RestResult<Integer> restResult = new RestResult();
        List<Error> errorList = new ArrayList<>();
        try{
            restResult.setObj(fileDataMergeServiceImpl.getTableRecordQuantity( mppSearchDto,errorList));
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

    @RequestMapping("/getCsv")
    @LogInCheck(doLock = true,doProcess = true)
    public void getCsv(@CurrentUser User user, HttpServletResponse response, @RequestBody MppSearchDto mppSearchDto){
        try{
           fileDataMergeServiceImpl.getCsv(mppSearchDto,response);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * 把excel压缩后导出
     * @param user
     * @param response
     * @param caseId
     */
    @RequestMapping("/mergeExport")
    @LogInCheck(doLock = true,doProcess = true)
    public void mergeExport(@CurrentUser User user, HttpServletResponse response,@RequestParam(value = "caseId") Long caseId){
        try{
            fileDataMergeServiceImpl.mergeExport(caseId,response);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @RequestMapping("/doUserDefinedRinse")
    @LogInCheck(doLock = true,doProcess = true)
    public RestResult doUserDefinedRinse(@CurrentUser User user,@RequestParam(value = "caseId") Long caseId){
        RestResult restResult = new RestResult();
        List<Error> errorList = new ArrayList<>();
        try{
            fileDataMergeServiceImpl.doUserDefinedRinse( caseId,user.getId(),errorList);
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
