package com.sugon.iris.sugonweb.file;

import com.sugon.iris.sugonannotation.annotation.system.BussLog;
import com.sugon.iris.sugonannotation.annotation.system.CurrentUser;
import com.sugon.iris.sugonannotation.annotation.system.LogInCheck;
import com.sugon.iris.sugoncommon.publicUtils.PublicUtils;
import com.sugon.iris.sugondomain.beans.baseBeans.Error;
import com.sugon.iris.sugondomain.beans.baseBeans.RestResult;
import com.sugon.iris.sugondomain.beans.system.User;
import com.sugon.iris.sugondomain.dtos.fileDtos.FileCaseDto;
import com.sugon.iris.sugondomain.dtos.fileDtos.MppSearchDto;
import com.sugon.iris.sugondomain.dtos.fileDtos.MppTableDto;
import com.sugon.iris.sugonservice.service.fileService.FileDataMergeService;
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
@RequestMapping("/dataMerge")
@Api(value = "数据合并清洗", tags = "数据合并清洗")
public class FileDataMergeController {

    private static final String FAILED = "FAILED";

    @Resource
    private FileDataMergeService fileDataMergeServiceImpl;


    @PostMapping("/getCases")
    @LogInCheck(doLock = true,doProcess = true)
    @ApiOperation(value = "通过用户信息获取案件信息")
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

    @PostMapping("/getTableRecord")
    @LogInCheck(doLock = true,doProcess = true)
    @ApiOperation(value = "通过mpp表信息获取mpp表信息")
    @ApiImplicitParam(name = "mppSearchDto", value = "mpp表信息")
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


    @PostMapping("/getTableRecordQuantity")
    @LogInCheck(doLock = true,doProcess = true)
    @ApiOperation(value = "通过mpp表信息获取mpp表数据量")
    @ApiImplicitParam(name = "mppSearchDto", value = "mpp表信息")
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

    @PostMapping("/getCsv")
    @BussLog
    @LogInCheck(doLock = true,doProcess = true)
    @ApiOperation(value = "通过mpp表信息导出csv")
    @ApiImplicitParam(name = "mppSearchDto", value = "mpp表信息")
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
    @GetMapping("/mergeExport")
    @BussLog
    @LogInCheck(doLock = true,doProcess = true)
    @ApiOperation(value = "通过mpp表信息导出excel压缩包")
    @ApiImplicitParam(name = "caseId", value = "案件编号")
    public void mergeExport(@CurrentUser User user, HttpServletResponse response,@RequestParam(value = "caseId") Long caseId){
        try{

            if("1".equals(PublicUtils.getConfigMap().get("sheet"))){
                fileDataMergeServiceImpl.mergeExportAsyncForSheet(caseId,response);
            }else{
                fileDataMergeServiceImpl.mergeExportAsync(caseId,response);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @PostMapping("/doUserDefinedRinse")
    @LogInCheck(doLock = true,doProcess = true)
    @ApiOperation(value = "数据清洗")
    @ApiImplicitParam(name = "caseId", value = "案件编号")
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
