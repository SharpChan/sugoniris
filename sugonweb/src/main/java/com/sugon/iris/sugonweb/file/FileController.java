package com.sugon.iris.sugonweb.file;

import com.sugon.iris.sugonannotation.annotation.system.CurrentUser;
import com.sugon.iris.sugonannotation.annotation.system.LogInCheck;
import com.sugon.iris.sugondomain.beans.baseBeans.Error;
import com.sugon.iris.sugondomain.beans.baseBeans.RestResult;
import com.sugon.iris.sugondomain.beans.userBeans.User;
import com.sugon.iris.sugondomain.dtos.fileDtos.FileAttachmentDto;
import com.sugon.iris.sugonservice.service.FileService.FolderService;
import org.springframework.http.HttpStatus;
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
public class FileController {

    private static final String FAILED = "FAILED";

    @Resource
    private FolderService folderServiceImpl;


    @PostMapping("/setCaseId")
    @LogInCheck(doLock = true,doProcess = true)
    @ResponseStatus(HttpStatus.CREATED)
    public RestResult<Void> setCaseId(@RequestParam(value = "caseId") String caseId, HttpSession session){
        RestResult<Void> restResult = new RestResult<Void>();
        session.setAttribute("caseId",caseId);
        return restResult;
    }

    @RequestMapping("/uploadFile")
    @LogInCheck(doLock = true,doProcess = true)
    public RestResult<Integer> uploadFile(@CurrentUser User user, HttpServletRequest request, HttpServletResponse response, HttpSession session) throws Exception {

        RestResult<Integer> restResult = new RestResult();
        List<Error> errorList = new ArrayList<>();
        try {
            request.setCharacterEncoding("UTF-8");
            response.setCharacterEncoding("UTF-8");
            MultipartHttpServletRequest multipartHttpServletRequest = (MultipartHttpServletRequest) request;
            List<MultipartFile> files = multipartHttpServletRequest.getFiles("file");
            restResult.setObj(folderServiceImpl.uploadFile(user,files,(String) session.getAttribute("caseId"),errorList));
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

    @RequestMapping("/updateFileAttachmentTemplateGroup")
    @LogInCheck(doLock = true,doProcess = true)
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

    @RequestMapping("/findFileList")
    @LogInCheck(doLock = true,doProcess = true)
    public RestResult<List<FileAttachmentDto>>  findFileList(@CurrentUser User user,@RequestBody FileAttachmentDto fileAttachmentDto){
        RestResult<List<FileAttachmentDto>> restResult = new RestResult();
        List<Error> errorList = new ArrayList<>();
        try {
            restResult.setObj(folderServiceImpl.findFileAttachmentList(user,fileAttachmentDto,errorList));
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

    @RequestMapping("/deleteFile")
    @LogInCheck(doLock = true,doProcess = true)
    public RestResult<Integer> deleteFile(@CurrentUser User user,@RequestParam(value = "selected") String selected) throws Exception {

        RestResult<Integer> restResult = new RestResult();
        List<Error> errorList = new ArrayList<>();
        String[] selectedArr = selected.split(",");
        try {
            restResult.setObj(folderServiceImpl.deleteFile(user,selectedArr,errorList));
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

    @RequestMapping("/dataSync")
    @LogInCheck(doLock = true,doProcess = true)
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
}
