package com.sugon.iris.sugonweb.file;

import com.sugon.iris.sugonannotation.annotation.system.CurrentUser;
import com.sugon.iris.sugonannotation.annotation.system.LogInCheck;
import com.sugon.iris.sugondomain.beans.baseBeans.Error;
import com.sugon.iris.sugondomain.beans.baseBeans.RestResult;
import com.sugon.iris.sugondomain.beans.system.User;
import com.sugon.iris.sugondomain.dtos.fileDtos.FileCaseDto;
import com.sugon.iris.sugondomain.dtos.fileDtos.FileParsingFailedDto;
import com.sugon.iris.sugondomain.dtos.fileDtos.FileTemplateDetailDto;
import com.sugon.iris.sugonservice.service.ExcelService.ExcelService;
import com.sugon.iris.sugonservice.service.FileService.FileImportCountService;
import org.springframework.http.MediaType;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/fileImportCount")
public class FileImportCountController {

    private static final String FAILED = "FAILED";

    @Resource
    private FileImportCountService fileImportCountServiceImpl;

    @Resource
    private ExcelService ExcelServiceImpl;


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


    /**
     * 下载服务器上的文件
     * @param res
     * @param request
     * @throws IOException
     */
    @RequestMapping("/getExcelqxxxTest")
    @LogInCheck(doLock = true,doProcess = true)
    public void getExcelzzz(HttpServletResponse res, HttpServletRequest request) throws IOException {
        String fileName = "华夏银行股份有限公司人员信息.xls";

        res.setHeader("content-type", "application/octet-stream");
        res.setContentType("application/octet-stream");
        res.setHeader("Content-Disposition", "attachment; filename=" + fileName);
        byte[] buff = new byte[1024];
        BufferedInputStream bis = null;
        OutputStream os = null;

        try {
            os = res.getOutputStream();
            bis = new BufferedInputStream(new FileInputStream(
                    new File("C:\\uplaodFile\\b0345aa8440e195a04f3b88e28d2d187\\银行信息\\" + fileName )));
            int i = bis.read(buff);

            while (i != -1) {
                os.write(buff, 0, buff.length);
                os.flush();
                i = bis.read(buff);
            }
        } catch ( IOException e ) {
            e.printStackTrace();
        } finally {
            if (bis != null) {
                try {
                    bis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        System.out.println("export file finish");
    }

    @RequestMapping("/getErrorsExcelZip")
    @LogInCheck(doLock = true,doProcess = true)
    public void getErrorsExcel(HttpServletResponse response, HttpServletRequest request,@RequestBody  @RequestParam(value = "fileDetailId") Long  fileDetailId) {
        try{
           fileImportCountServiceImpl.downloadErrorsExcelZip(response, request, fileDetailId);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @RequestMapping("/getExcel")
    @LogInCheck(doLock = true,doProcess = true)
    public void getExcel(HttpServletResponse response, HttpServletRequest request ,@RequestParam(value = "fileDetailId") Long  fileDetailId) throws IOException {

        try{
            fileImportCountServiceImpl.downloadErrorsExcel(response, request, fileDetailId);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @RequestMapping(value = "/dataAmendment",produces = MediaType.APPLICATION_JSON_VALUE)
    @LogInCheck(doLock = true,doProcess = true)
    public RestResult<Integer> dataAmendment(HttpServletRequest request, HttpServletResponse response,@RequestParam(value = "fileDetailId") Long  fileDetailId) throws Exception {
        RestResult<Integer> restResult = new RestResult();
        List<Error> errorList = new ArrayList<>();
        try {
            request.setCharacterEncoding("UTF-8");
            MultipartHttpServletRequest multipartHttpServletRequest = (MultipartHttpServletRequest) request;
            List<MultipartFile> files = multipartHttpServletRequest.getFiles("file");
            restResult.setObj(fileImportCountServiceImpl.dataAmendment(files,fileDetailId,errorList));
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
