package com.sugon.iris.sugonweb.file;

import com.sugon.iris.sugonannotation.annotation.system.CurrentUser;
import com.sugon.iris.sugonannotation.annotation.system.LogInCheck;
import com.sugon.iris.sugondomain.beans.baseBeans.Error;
import com.sugon.iris.sugondomain.beans.baseBeans.RestResult;
import com.sugon.iris.sugondomain.beans.system.User;
import com.sugon.iris.sugondomain.dtos.fileDtos.FileAttachmentDto;
import com.sugon.iris.sugonservice.service.fileService.FolderService;
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
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/file")
@Api(value = "文件管理", tags = "文件相关接口")
public class FileController {

    private static final String FAILED = "FAILED";

    @Resource
    private FolderService folderServiceImpl;

    /*
    @PostMapping("/setCaseId")
    @LogInCheck(doLock = true,doProcess = true)
    @ResponseStatus(HttpStatus.CREATED)
    public RestResult<Void> setCaseId(@RequestParam(value = "caseId") String caseId, HttpSession session){
        RestResult<Void> restResult = new RestResult<Void>();
        session.setAttribute("caseId",caseId);
        return restResult;
    }*/

    @PostMapping("/test2")
    public RestResult<Integer> test() throws Exception {
        RestResult<Integer> restResult = new RestResult();
        List<Error> errorList = new ArrayList<>();
        try {
            restResult.setObj(folderServiceImpl.test());
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

    @PostMapping("/uploadFile")
    @LogInCheck(doLock = true,doProcess = true)
    @ApiOperation(value = "文件上传")
    @ApiImplicitParam(name = "caseId", value = "案件id")
    public RestResult<Integer> uploadFile(@CurrentUser User user,@RequestParam(value = "caseId") Long caseId ,HttpServletRequest request, HttpServletResponse response, HttpSession session) throws Exception {
        RestResult<Integer> restResult = new RestResult();
        List<Error> errorList = new ArrayList<>();
        try {
            request.setCharacterEncoding("UTF-8");
            response.setCharacterEncoding("UTF-8");
            MultipartHttpServletRequest multipartHttpServletRequest = (MultipartHttpServletRequest) request;
            List<MultipartFile> files = multipartHttpServletRequest.getFiles("file");
            restResult.setObj(folderServiceImpl.uploadFile(user,files, caseId,errorList));
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

    @PostMapping("/updateFileAttachmentTemplateGroup")
    @LogInCheck(doLock = true,doProcess = true)
    @ApiOperation(value = "文件绑定模板组")
    @ApiImplicitParam(name = "fileAttachmentDto", value = "原始文件信息")
    public RestResult<Integer> updateFileAttachmentTemplateGroup(@CurrentUser User user, @RequestBody FileAttachmentDto fileAttachmentDto) throws Exception {
        RestResult<Integer> restResult = new RestResult();
        List<Error> errorList = new ArrayList<>();
        try {
            restResult.setObj(folderServiceImpl.updateFileAttachmentTemplateGroup(user,fileAttachmentDto,errorList));
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

    @PostMapping("/findFileList")
    @LogInCheck(doLock = true,doProcess = true)
    @ApiOperation(value = "获取原始文件信息")
    @ApiImplicitParam(name = "fileAttachmentDto", value = "原始文件信息")
    public RestResult<List<FileAttachmentDto>>  findFileList(@CurrentUser User user,@RequestBody FileAttachmentDto fileAttachmentDto){
        RestResult<List<FileAttachmentDto>> restResult = new RestResult();
        List<Error> errorList = new ArrayList<>();
        fileAttachmentDto.setUserId(user.getId());
        try {
            restResult.setObj(folderServiceImpl.findFileAttachmentList(fileAttachmentDto,errorList));
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

    @PostMapping("/deleteFile")
    @LogInCheck(doLock = true,doProcess = true)
    @ApiOperation(value = "删除原始文件")
    @ApiImplicitParam(name = "selected", value = "选中的原始文件")
    public RestResult<Integer> deleteFile(@CurrentUser User user,@RequestParam(value = "selected") String selected) throws Exception {

        RestResult<Integer> restResult = new RestResult();
        List<Error> errorList = new ArrayList<>();
        String[] selectedArr = selected.split(",");
        try {
            restResult.setObj(folderServiceImpl.deleteFile(user,selectedArr,true,errorList));
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

    @PostMapping("/dataSync")
    @LogInCheck(doLock = true,doProcess = true)
    @ApiOperation(value = "文件解析")
    @ApiImplicitParam(name = "selected", value = "选中的原始文件")
    public RestResult<Void> dataSync(@CurrentUser User user,@RequestParam(value = "selected") String selected) throws Exception {

        RestResult<Void> restResult = new RestResult();
        List<Error> errorList = new ArrayList<>();
        String[] selectedArr = selected.split(",");
        try {
            folderServiceImpl.decompress(user,selectedArr,errorList);
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

    @PostMapping("/test")
    @LogInCheck(doLock = true,doProcess = true)
    public RestResult<Void> test(@CurrentUser User user)  throws Exception {

        RestResult<Void> restResult = new RestResult();
        List<Error> errorList = new ArrayList<>();
        try {
            folderServiceImpl.test(user);
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

    @PostMapping("/getFileServerIp")
    @LogInCheck(doLock = true,doProcess = true)
    @ApiOperation(value = "获取文件解析服务器的ip")
    public RestResult<String> getFileServerIp() throws Exception {

        RestResult<String> restResult = new RestResult();
        List<Error> errorList = new ArrayList<>();
        try {
            restResult.setObj(folderServiceImpl.getFileServerIp());
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
