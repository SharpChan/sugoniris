package com.sugon.iris.sugonweb.file;

import com.sugon.iris.sugonannotation.annotation.system.CurrentUser;
import com.sugon.iris.sugonannotation.annotation.system.LogInCheck;
import com.sugon.iris.sugondomain.beans.baseBeans.Error;
import com.sugon.iris.sugondomain.beans.baseBeans.RestResult;
import com.sugon.iris.sugondomain.beans.system.User;
import com.sugon.iris.sugondomain.dtos.fileDtos.FileTemplateDetailDto;
import com.sugon.iris.sugondomain.dtos.fileDtos.FileTemplateDto;
import com.sugon.iris.sugonservice.service.FileService.FileTemplateDetailService;
import com.sugon.iris.sugonservice.service.FileService.FileTemplateService;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/fileTemplate")
public class FileTemplateController {

    private static final String FAILED = "FAILED";

    @Resource
    private FileTemplateService fileTemplateServiceImpl;

    @Resource
    private FileTemplateDetailService fileTemplateDetailServiceImpl;

    /**
     * 查询模板信息
     */

    @RequestMapping("/getFileTemplates")
    @LogInCheck(doLock = true,doProcess = true)
    public RestResult<List<FileTemplateDto>> getFileTemplates(@CurrentUser User user,@RequestBody FileTemplateDto fileTemplateDto){
        RestResult<List<FileTemplateDto>> restResult = new RestResult();
        List<Error> errorList = new ArrayList<>();
        try{
            restResult.setObj(fileTemplateServiceImpl.getFileTemplateDtoList(user,fileTemplateDto,errorList));
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

    @RequestMapping("/fileTemplateInsert")
    @LogInCheck(doLock = true,doProcess = true)
    public RestResult<Integer> fileTemplateInsert(@CurrentUser User user,@RequestBody FileTemplateDto fileTemplateDto){
        RestResult<Integer> restResult = new RestResult();
        List<Error> errorList = new ArrayList<>();
        try{
            restResult.setObj(fileTemplateServiceImpl.fileTemplateInsert(user,fileTemplateDto,errorList));
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

    @RequestMapping("/updateFileTemplate")
    @LogInCheck(doLock = true,doProcess = true)
    public RestResult<Integer> updateFileTemplate(@CurrentUser User user,@RequestBody FileTemplateDto fileTemplateDto){
        RestResult<Integer> restResult = new RestResult();
        List<Error> errorList = new ArrayList<>();
        try{
            restResult.setObj(fileTemplateServiceImpl.updateFileTemplate(user,fileTemplateDto,errorList));
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

    @RequestMapping("/deleteFileTemplate")
    @LogInCheck(doLock = true,doProcess = true)
    public RestResult<Integer> deleteFile(@RequestParam(value = "selected") String selected) throws Exception {
        RestResult<Integer> restResult = new RestResult();
        List<Error> errorList = new ArrayList<>();
        String[] selectedArr = selected.split(",");
        try {
            restResult.setObj(fileTemplateServiceImpl.deleteFileTemplate(selectedArr,errorList));
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

    /**
     * 查询模板信息
     */
    @RequestMapping("/getFileTemplateDetails")
    @LogInCheck(doLock = true,doProcess = true)
    public   RestResult<List<FileTemplateDetailDto>>   getFileTemplateDetails(@CurrentUser User user,@RequestBody FileTemplateDetailDto fileTemplateDetailDto){
        RestResult<List<FileTemplateDetailDto>> restResult = new RestResult();
        List<Error> errorList = new ArrayList<>();
        try {
            restResult.setObj(fileTemplateDetailServiceImpl.getFileTemplateDetailDtoList(user,fileTemplateDetailDto,errorList));
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

    /**
     * 保存模板详细信息
     */
    @RequestMapping("/saveFileTemplateDetails")
    @LogInCheck(doLock = true,doProcess = true)
    public   RestResult<Integer>   saveFileTemplateDetails(@CurrentUser User user,@RequestBody FileTemplateDetailDto fileTemplateDetailDto){
        RestResult<Integer> restResult = new RestResult();
        List<Error> errorList = new ArrayList<>();
        try {
            restResult.setObj(fileTemplateDetailServiceImpl.fileTemplateDetailInsert(user,fileTemplateDetailDto,errorList));
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

    /**
     * 修改模板详细信息
     */
    @RequestMapping("/updateFileTemplateDetails")
    @LogInCheck(doLock = true,doProcess = true)
    public   RestResult<Integer>   updateFileTemplateDetails(@CurrentUser User user,@RequestBody FileTemplateDetailDto fileTemplateDetailDto){
        RestResult<Integer> restResult = new RestResult();
        List<Error> errorList = new ArrayList<>();
        try {
            restResult.setObj(fileTemplateDetailServiceImpl.updateFileTemplateDetail(user,fileTemplateDetailDto,errorList));
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

    /**
     * 删除模板详细信息
     */
    @RequestMapping("/removeFileTemplateDetails")
    @LogInCheck(doLock = true,doProcess = true)
    public   RestResult<Integer>   removeFileTemplateDetails(@RequestParam(value = "selected") String selected){
        RestResult<Integer> restResult = new RestResult();
        List<Error> errorList = new ArrayList<>();
        String[] selectedArr = selected.split(",");
        try {
            restResult.setObj(fileTemplateDetailServiceImpl.deleteFileTemplateDetail(selectedArr,errorList));
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

    /**
     * 与清洗字段进行解绑
     */
    @RequestMapping("/removeBoundByTemplateId")
    @LogInCheck(doLock = true,doProcess = true)
    public   RestResult<Integer>   removeBoundByTemplateId(@RequestParam(value = "templateId") Long  templateId){
        RestResult<Integer> restResult = new RestResult();
        List<Error> errorList = new ArrayList<>();
        try {
            restResult.setObj(fileTemplateDetailServiceImpl.removeBoundByTemplateId(templateId,errorList));
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
