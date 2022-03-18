package com.sugon.iris.sugonrest.Template;

import com.sugon.iris.sugondomain.beans.baseBeans.Error;
import com.sugon.iris.sugondomain.beans.baseBeans.RestResult;
import com.sugon.iris.sugondomain.beans.shengTing.ShanghaiMinhangBean;
import com.sugon.iris.sugondomain.beans.shengTing.ShanghaikeyunBean;
import com.sugon.iris.sugondomain.beans.shengTing.base.StResponse;
import com.sugon.iris.sugondomain.dtos.fileDtos.FileTemplateDto;
import com.sugon.iris.sugondomain.enums.ErrorCode_Enum;
import com.sugon.iris.sugonservice.service.fileService.FileTemplateService;
import io.swagger.annotations.Api;
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
@RequestMapping("/templateInfo")
@Api(value = "对外接口-模板", tags = "模板信息")
public class TemplateInfosController {

    private static final String FAILED = "FAILED";

    @Resource
    private FileTemplateService fileTemplateServiceImpl;

    @GetMapping("/getTemplateAndDetails")
    @ApiOperation(value = "模板信息")
    public RestResult<List<FileTemplateDto>> getAllTemplateInfos() throws IllegalAccessException{
        RestResult<List<FileTemplateDto>> restResult = new RestResult();
        List<Error> errorList = new ArrayList<>();
        try {
            restResult.setObj(fileTemplateServiceImpl.getAllFileTemplateDtoAndDetailsList(errorList));
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


    @CrossOrigin
    @PostMapping("/getKyxx")
    @ApiOperation(value = "客运信息")
    public List<StResponse> getKyxx(@RequestParam(value = "cardId") String cardId,@RequestParam(value = "minRownum") String minRownum,@RequestParam(value = "maxRownum") String maxRownum) throws IllegalAccessException{
        List<StResponse> stResponseList = null;
        try {
            stResponseList = fileTemplateServiceImpl.getKyxx(cardId,minRownum, maxRownum);
        }catch (Exception e){
            e.printStackTrace();
        }

        return stResponseList;
    }

    @CrossOrigin
    @PostMapping("/getMhxx")
    @ApiOperation(value = "民航信息")
    public List<StResponse> getMhxx(@RequestParam(value = "cert_no") String cert_no,@RequestParam(value = "minRownum") String minRownum,@RequestParam(value = "maxRownum") String maxRownum) throws IllegalAccessException{
        List<StResponse> stResponseList = null;
        try {
            stResponseList =  fileTemplateServiceImpl.getMinHanxx(cert_no,minRownum,maxRownum);
        }catch (Exception e){
            e.printStackTrace();

        }
        return stResponseList;
    }

    @CrossOrigin
    @PostMapping("/getMhxxByExcel")
    @ApiOperation(value = "民航信息")
    public List<StResponse<List<ShanghaiMinhangBean>>> getMhxxByExcel(HttpServletRequest request, HttpServletResponse response) throws IllegalAccessException{
        List<StResponse<List<ShanghaiMinhangBean>>> stResponseList = null;
        try {
            request.setCharacterEncoding("UTF-8");
            response.setCharacterEncoding("UTF-8");
            MultipartHttpServletRequest multipartHttpServletRequest = (MultipartHttpServletRequest) request;
            List<MultipartFile> files = multipartHttpServletRequest.getFiles("file");
            stResponseList = fileTemplateServiceImpl.getMinHanxxByExcel(files);
        }catch (Exception e){
            e.printStackTrace();
        }
        return stResponseList;
    }

    @CrossOrigin
    @PostMapping("/getKeYunByExcel")
    @ApiOperation(value = "客运")
    public List<StResponse<List<ShanghaikeyunBean>>> getKeYunByExcel(HttpServletRequest request, HttpServletResponse response) throws IllegalAccessException{
        List<StResponse<List<ShanghaikeyunBean>>> stResponseList = null;
        try {
            request.setCharacterEncoding("UTF-8");
            response.setCharacterEncoding("UTF-8");
            MultipartHttpServletRequest multipartHttpServletRequest = (MultipartHttpServletRequest) request;
            List<MultipartFile> files = multipartHttpServletRequest.getFiles("file");
            stResponseList = fileTemplateServiceImpl.getKeyunByExcel(files);
        }catch (Exception e){
            e.printStackTrace();

        }
        return stResponseList;
    }
}
