package com.sugon.iris.sugonweb.file;

import com.sugon.iris.sugonannotation.annotation.system.BussLog;
import com.sugon.iris.sugonannotation.annotation.system.CurrentUser;
import com.sugon.iris.sugonannotation.annotation.system.LogInCheck;
import com.sugon.iris.sugondomain.beans.baseBeans.Error;
import com.sugon.iris.sugondomain.beans.baseBeans.RestResult;
import com.sugon.iris.sugondomain.beans.system.User;
import com.sugon.iris.sugondomain.dtos.fileDtos.FileTemplateDetailDto;
import com.sugon.iris.sugondomain.dtos.fileDtos.FileTemplateDto;
import com.sugon.iris.sugondomain.dtos.rinseBusinessDto.*;
import com.sugon.iris.sugondomain.enums.ErrorCode_Enum;
import com.sugon.iris.sugonservice.service.fileService.FileTemplateDetailService;
import com.sugon.iris.sugonservice.service.fileService.FileTemplateService;
import com.sugon.iris.sugonservice.service.rinseBusinessService.RinseBusinessService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/fileTemplate")
@Api(value = "数据模板", tags = "数据模板相关接口")
public class FileTemplateController {

    private static final String FAILED = "FAILED";

    @Resource
    private FileTemplateService fileTemplateServiceImpl;

    @Resource
    private FileTemplateDetailService fileTemplateDetailServiceImpl;

    @Resource
    private RinseBusinessService rinseBusinessService;


    /**
     * 查询模板信息
     */

    @PostMapping("/getFileTemplates")
    @LogInCheck(doLock = true,doProcess = true)
    @ApiOperation(value = "获取数据导入模板")
    @ApiImplicitParam(name = "fileTemplateDto", value = "数据模板")
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

    @PostMapping("/fileTemplateInsert")
    @LogInCheck(doLock = true,doProcess = true)
    @ApiOperation(value = "保存数据导入模板")
    @ApiImplicitParam(name = "fileTemplateDto", value = "数据模板")
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

    @PostMapping("/updateFileTemplate")
    @LogInCheck(doLock = true,doProcess = true)
    @ApiOperation(value = "修改数据导入模板")
    @ApiImplicitParam(name = "fileTemplateDto", value = "数据模板")
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

    @PostMapping("/deleteFileTemplate")
    @LogInCheck(doLock = true,doProcess = true)
    @ApiOperation(value = "修改数据导入模板")
    @ApiImplicitParam(name = "selected", value = "勾选的数据模板")
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
    @PostMapping("/getFileTemplateDetails")
    @LogInCheck(doLock = true,doProcess = true)
    @ApiOperation(value = "获取模板字段信息")
    @ApiImplicitParam(name = "fileTemplateDetailDto", value = "模板字段信息")
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
    @PostMapping("/saveFileTemplateDetails")
    @LogInCheck(doLock = true,doProcess = true)
    @ApiOperation(value = "获取模板字段信息")
    @ApiImplicitParam(name = "fileTemplateDetailDto", value = "模板字段信息")
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
    @PostMapping("/updateFileTemplateDetails")
    @LogInCheck(doLock = true,doProcess = true)
    @ApiOperation(value = "修改获取模板字段信息")
    @ApiImplicitParam(name = "fileTemplateDetailDto", value = "模板字段信息")
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
    @PostMapping("/removeFileTemplateDetails")
    @LogInCheck(doLock = true,doProcess = true)
    @ApiOperation(value = "删除模板字段信息")
    @ApiImplicitParam(name = "selected", value = "已经勾选的")
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
    @PostMapping("/removeBoundByTemplateId")
    @LogInCheck(doLock = true,doProcess = true)
    @ApiOperation(value = "通过id删除模板")
    @ApiImplicitParam(name = "templateId", value = "模板id")
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

    /**
     * 获取拼音
     */
    @PostMapping("/getPinyin")
    @LogInCheck(doLock = true,doProcess = true)
    @ApiOperation(value = "中文转拼音")
    @ApiImplicitParam(name = "chinese", value = "中文字符")
    public   RestResult<String>   getPinyin(@RequestBody String  chinese){
        RestResult<String> restResult = new RestResult();
        List<Error> errorList = new ArrayList<>();
        try {
            restResult.setObj(fileTemplateDetailServiceImpl.getPinyin(chinese,errorList));
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
     * 保存重复校验字段
     */
    @PostMapping("/saveRepetBuss")
    @LogInCheck(doLock = true,doProcess = true)
    @ApiOperation(value = "去重配置信息保存")
    @ApiImplicitParam(name = "rinseBusinessRepeatDto", value = "去重配置信息")
    public   RestResult<Integer>   saveRepetBuss( @RequestBody RinseBusinessRepeatDto rinseBusinessRepeatDto){
        RestResult<Integer> restResult = new RestResult();
        List<Error> errorList = new ArrayList<>();
        try {
           restResult.setObj(rinseBusinessService.saveRinseBusinessRepeat(rinseBusinessRepeatDto,errorList));
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
     * 保存null替换
     */
    @PostMapping("/saveNullBuss")
    @LogInCheck(doLock = true,doProcess = true)
    @ApiOperation(value = "空值替换信息保存")
    @ApiImplicitParam(name = "rinseBusinessNullDto", value = "空值信息")
    public   RestResult<Integer>   saveNullBuss( @RequestBody RinseBusinessNullDto rinseBusinessNullDto){
        RestResult<Integer> restResult = new RestResult();
        List<Error> errorList = new ArrayList<>();
        try {
            restResult.setObj(rinseBusinessService.saveRinseBusinessNull(rinseBusinessNullDto,errorList));
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
     * 保存字段关键字进行替换
     */
    @PostMapping("/saveReplaceBuss")
    @LogInCheck(doLock = true,doProcess = true)
    @ApiOperation(value = "指定值替换信息保存")
    @ApiImplicitParam(name = "rinseBusinessReplaceDto", value = "指定值信息")
    public   RestResult<Integer>   saveReplaceBuss( @RequestBody RinseBusinessReplaceDto rinseBusinessReplaceDto){
        RestResult<Integer> restResult = new RestResult();
        List<Error> errorList = new ArrayList<>();
        try {
            restResult.setObj(rinseBusinessService.saveRinseBusinessReplace(rinseBusinessReplaceDto,errorList));
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
     * 查询重复校验字段
     */
    @PostMapping("/getRepetBussList")
    @LogInCheck(doLock = true,doProcess = true)
    @ApiOperation(value = "获取去重信息")
    @ApiImplicitParam(name = "fileTemplateId", value = "模板id")
    public   RestResult<List<RinseBusinessRepeatDto>>   getRepetBussList(@RequestParam(value = "fileTemplateId") Long  fileTemplateId){
        RestResult<List<RinseBusinessRepeatDto>> restResult = new RestResult();
        List<Error> errorList = new ArrayList<>();
        try {
            restResult.setObj(rinseBusinessService.getRepetBussList(fileTemplateId,errorList));
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
     * 查询null值替换
     */
    @PostMapping("/getNullBussList")
    @LogInCheck(doLock = true,doProcess = true)
    @ApiOperation(value = "获取空值替换信息")
    @ApiImplicitParam(name = "fileTemplateId", value = "模板id")
    public   RestResult<List<RinseBusinessNullDto>>   getNullBussList(@RequestParam(value = "fileTemplateId") Long  fileTemplateId){
        RestResult<List<RinseBusinessNullDto>> restResult = new RestResult();
        List<Error> errorList = new ArrayList<>();
        try {
            restResult.setObj(rinseBusinessService.getNullBussList(fileTemplateId,errorList));
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
     * 查询关键字替换
     */
    @PostMapping("/getReplaceBussList")
    @LogInCheck(doLock = true,doProcess = true)
    @ApiOperation(value = "获取指定值替换信息")
    @ApiImplicitParam(name = "fileTemplateId", value = "模板id")
    public   RestResult<List<RinseBusinessReplaceDto>>   getReplaceBussList(@RequestParam(value = "fileTemplateId") Long  fileTemplateId){
        RestResult<List<RinseBusinessReplaceDto>> restResult = new RestResult();
        List<Error> errorList = new ArrayList<>();
        try {
            restResult.setObj(rinseBusinessService.getReplaceBussList(fileTemplateId,errorList));
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
     * 查询去除后缀
     */
    @PostMapping("/getSuffixBussList")
    @LogInCheck(doLock = true,doProcess = true)
    @ApiOperation(value = "后缀替换信息")
    @ApiImplicitParam(name = "fileTemplateId", value = "模板id")
    public   RestResult<List<RinseBusinessSuffixDto>>   getSuffixBussList(@RequestParam(value = "fileTemplateId") Long  fileTemplateId){
        RestResult<List<RinseBusinessSuffixDto>> restResult = new RestResult();
        List<Error> errorList = new ArrayList<>();
        try {
            restResult.setObj(rinseBusinessService.getSuffixBussList(fileTemplateId,errorList));
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
     * 查询前缀替换
     */
    @PostMapping("/getPrefixBussList")
    @LogInCheck(doLock = true,doProcess = true)
    @ApiOperation(value = "前缀替换信息")
    @ApiImplicitParam(name = "fileTemplateId", value = "模板id")
    public   RestResult<List<RinseBusinessPrefixDto>>   getPrefixBussList(@RequestParam(value = "fileTemplateId") Long  fileTemplateId){
        RestResult<List<RinseBusinessPrefixDto>> restResult = new RestResult();
        List<Error> errorList = new ArrayList<>();
        try {
            restResult.setObj(rinseBusinessService.getPrefixBussList(fileTemplateId,errorList));
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
     * 查询ip解析
     */
    @PostMapping("/getIpBussList")
    @LogInCheck(doLock = true,doProcess = true)
    @ApiOperation(value = "ip解析信息")
    @ApiImplicitParam(name = "fileTemplateId", value = "模板id")
    public   RestResult<List<RinseBusinessIpDto>>   getIpBussList(@RequestParam(value = "fileTemplateId") Long  fileTemplateId){
        RestResult<List<RinseBusinessIpDto>> restResult = new RestResult();
        List<Error> errorList = new ArrayList<>();
        try {
            restResult.setObj(rinseBusinessService.getIpBussList(fileTemplateId,errorList));
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
     * 查询Phone解析
     */
    @PostMapping("/getPhoneBussList")
    @LogInCheck(doLock = true,doProcess = true)
    @ApiOperation(value = "Phone解析信息")
    @ApiImplicitParam(name = "fileTemplateId", value = "模板id")
    public   RestResult<List<RinseBusinessPhoneDto>>   getPhoneBussList(@RequestParam(value = "fileTemplateId") Long  fileTemplateId){
        RestResult<List<RinseBusinessPhoneDto>> restResult = new RestResult();
        List<Error> errorList = new ArrayList<>();
        try {
            restResult.setObj(rinseBusinessService.getPhoneBussList(fileTemplateId,errorList));
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
     * 查询身份证解析
     */
    @PostMapping("/getIdCardNoBussList")
    @LogInCheck(doLock = true,doProcess = true)
    @ApiOperation(value = "IdCardNo解析信息")
    @ApiImplicitParam(name = "fileTemplateId", value = "模板id")
    public   RestResult<List<RinseBusinessIdCardNoDto>>   getIdCardNoBussList(@RequestParam(value = "fileTemplateId") Long  fileTemplateId){
        RestResult<List<RinseBusinessIdCardNoDto>> restResult = new RestResult();
        List<Error> errorList = new ArrayList<>();
        try {
            restResult.setObj(rinseBusinessService.getIdCardNoBussList(fileTemplateId,errorList));
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
     * 保存后缀替换
     */
    @PostMapping("/saveSuffixBuss")
    @LogInCheck(doLock = true,doProcess = true)
    @ApiOperation(value = "保存后缀替换信息")
    @ApiImplicitParam(name = "rinseBusinessSuffixDto", value = "后缀替换信息")
    public   RestResult<Integer>   saveSuffixBuss( @RequestBody RinseBusinessSuffixDto rinseBusinessSuffixDto){
        RestResult<Integer> restResult = new RestResult();
        List<Error> errorList = new ArrayList<>();
        try {
            restResult.setObj(rinseBusinessService.saveRinseBusinessSuffix(rinseBusinessSuffixDto,errorList));
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
     * 保存后缀替换
     */
    @PostMapping("/savePrefixBuss")
    @LogInCheck(doLock = true,doProcess = true)
    @ApiOperation(value = "保存后缀替换信息")
    @ApiImplicitParam(name = "rinseBusinessSuffixDto", value = "后缀替换信息")
    public   RestResult<Integer>   savePrefixBuss( @RequestBody RinseBusinessPrefixDto rinseBusinessPrefixDto){
        RestResult<Integer> restResult = new RestResult();
        List<Error> errorList = new ArrayList<>();
        try {
            restResult.setObj(rinseBusinessService.saveRinseBusinessPrefix(rinseBusinessPrefixDto,errorList));
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
     * 保存ip解析
     */
    @PostMapping("/saveIpBuss")
    @LogInCheck(doLock = true,doProcess = true)
    @ApiOperation(value = "保存解析ip地址信息")
    @ApiImplicitParam(name = "rinseBusinessIpDto", value = "ip地址信息")
    public   RestResult<Integer>   saveIpBuss( @RequestBody RinseBusinessIpDto rinseBusinessIpDto){
        RestResult<Integer> restResult = new RestResult();
        List<Error> errorList = new ArrayList<>();
        try {
            restResult.setObj(rinseBusinessService.saveRinseBusinessIp(rinseBusinessIpDto,errorList));
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
     * 保存电话解析
     */
    @PostMapping("/savePhoneBuss")
    @LogInCheck(doLock = true,doProcess = true)
    @ApiOperation(value = "保存后缀替换信息")
    @ApiImplicitParam(name = "rinseBusinessPhoneDto", value = "电话信息")
    public   RestResult<Integer>   savePhoneBuss( @RequestBody RinseBusinessPhoneDto rinseBusinessPhoneDto){
        RestResult<Integer> restResult = new RestResult();
        List<Error> errorList = new ArrayList<>();
        try {
            restResult.setObj(rinseBusinessService.saveRinseBusinessPhone(rinseBusinessPhoneDto,errorList));
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
     * 保存身份证解析
     */
    @PostMapping("/saveIdCardNoBuss")
    @LogInCheck(doLock = true,doProcess = true)
    @ApiOperation(value = "保存后缀替换信息")
    @ApiImplicitParam(name = "rinseBusinessIdCardNoDto", value = "电话信息")
    public   RestResult<Integer>   saveIdCardNoBuss( @RequestBody RinseBusinessIdCardNoDto rinseBusinessIdCardNoDto){
        RestResult<Integer> restResult = new RestResult();
        List<Error> errorList = new ArrayList<>();
        try {
            restResult.setObj(rinseBusinessService.saveRinseBusinessIdCardNo(rinseBusinessIdCardNoDto,errorList));
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
     * 删除去重
     */
    @PostMapping("/deleteRepetById")
    @LogInCheck(doLock = true,doProcess = true)
    @ApiOperation(value = "通过id删除去重信息")
    @ApiImplicitParam(name = "id", value = "去重id")
    public   RestResult<Integer>   deleteRepetById( @RequestParam(value = "id") Long  id ){
        RestResult<Integer> restResult = new RestResult();
        List<Error> errorList = new ArrayList<>();
        try {
            restResult.setObj(rinseBusinessService.deleteRinseBusinessRepeatById(id,errorList));
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
     * 删除关键字替换
     */
    @PostMapping("/deleteReplaceById")
    @LogInCheck(doLock = true,doProcess = true)
    @ApiOperation(value = "通过id删除指定值替换信息")
    @ApiImplicitParam(name = "id", value = "指定值id")
    public   RestResult<Integer>   deleteReplaceById( @RequestParam(value = "id") Long  id ){
        RestResult<Integer> restResult = new RestResult();
        List<Error> errorList = new ArrayList<>();
        try {
            restResult.setObj(rinseBusinessService.deleteRinseBusinessReplaceById(id,errorList));
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
     * 删除null替换
     */
    @PostMapping("/deleteNullById")
    @LogInCheck(doLock = true,doProcess = true)
    @ApiOperation(value = "通过id删除指定值替换信息")
    @ApiImplicitParam(name = "id", value = "指定值id")
    public   RestResult<Integer>   deleteNullById( @RequestParam(value = "id") Long  id ){
        RestResult<Integer> restResult = new RestResult();
        List<Error> errorList = new ArrayList<>();
        try {
            restResult.setObj(rinseBusinessService.deleteRinseBusinessNullById(id,errorList));
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
     * 删除去除后缀
     */
    @PostMapping("/deleteSuffixById")
    @LogInCheck(doLock = true,doProcess = true)
    @ApiOperation(value = "后缀替换信息删除")
    @ApiImplicitParam(name = "id", value = "id")
    public   RestResult<Integer>   deleteSuffixById( @RequestParam(value = "id") Long  id ){
        RestResult<Integer> restResult = new RestResult();
        List<Error> errorList = new ArrayList<>();
        try {
            restResult.setObj(rinseBusinessService.deleteRinseBusinessSuffixById(id,errorList));
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
     * 删除去除前缀
     */
    @PostMapping("/deletePrefixById")
    @LogInCheck(doLock = true,doProcess = true)
    @ApiOperation(value = "后缀替换信息删除")
    @ApiImplicitParam(name = "id", value = "id")
    public   RestResult<Integer>   deletePrefixById( @RequestParam(value = "id") Long  id ){
        RestResult<Integer> restResult = new RestResult();
        List<Error> errorList = new ArrayList<>();
        try {
            restResult.setObj(rinseBusinessService.deleteRinseBusinessPrefixById(id,errorList));
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
     * 删除ip解析
     */
    @PostMapping("/deleteIpById")
    @LogInCheck(doLock = true,doProcess = true)
    @ApiOperation(value = "ip解析信息删除")
    @ApiImplicitParam(name = "id", value = "id")
    public   RestResult<Integer>   deleteIpById( @RequestParam(value = "id") Long  id ){
        RestResult<Integer> restResult = new RestResult();
        List<Error> errorList = new ArrayList<>();
        try {
            restResult.setObj(rinseBusinessService.deleteRinseBusinessIpById(id,errorList));
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
     * 删除电话解析
     */
    @PostMapping("/deletePhoneById")
    @LogInCheck(doLock = true,doProcess = true)
    @ApiOperation(value = "Phone解析信息删除")
    @ApiImplicitParam(name = "id", value = "id")
    public   RestResult<Integer>   deletePhoneById( @RequestParam(value = "id") Long  id ){
        RestResult<Integer> restResult = new RestResult();
        List<Error> errorList = new ArrayList<>();
        try {
            restResult.setObj(rinseBusinessService.deleteRinseBusinessPhoneById(id,errorList));
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
     * 删除身份证解析
     */
    @PostMapping("/deleteIdCardNoById")
    @LogInCheck(doLock = true,doProcess = true)
    @ApiOperation(value = "Phone解析信息删除")
    @ApiImplicitParam(name = "id", value = "id")
    public   RestResult<Integer>   deleteIdCardNoById( @RequestParam(value = "id") Long  id ){
        RestResult<Integer> restResult = new RestResult();
        List<Error> errorList = new ArrayList<>();
        try {
            restResult.setObj(rinseBusinessService.deleteRinseBusinessIdCardNoById(id,errorList));
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

    @GetMapping("/downloadTemplateTXT")
    @ApiOperation(value = "模板下载")
    @ApiImplicitParam(name = "fileTemplateId", value = "模板id")
    public RestResult<Void> downloadTemplateTXT(HttpServletResponse response,@RequestParam(value = "fileTemplateId") Long  fileTemplateId) throws IllegalAccessException{
        RestResult<Void> restResult = new RestResult();
        List<Error> errorList = new ArrayList<>();
        try {
            fileTemplateServiceImpl.getTemplateTxt(response,fileTemplateId,errorList);
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

    @PostMapping("/uploadFileTemplateTxt")
    @BussLog
    @LogInCheck(doLock = true,doProcess = true)
    @ApiOperation(value = "模板文件上传")
    public RestResult<Boolean> uploadFile(@CurrentUser User user,  HttpServletRequest request, HttpServletResponse response) throws Exception {
        RestResult<Boolean> restResult = new RestResult();
        List<Error> errorList = new ArrayList<>();
        try {
            request.setCharacterEncoding("UTF-8");
            MultipartHttpServletRequest multipartHttpServletRequest = (MultipartHttpServletRequest) request;
            List<MultipartFile> files = multipartHttpServletRequest.getFiles("fileTxt");
            restResult.setObj(fileTemplateServiceImpl.importTemplateTxt(files.get(0),user,errorList));
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
