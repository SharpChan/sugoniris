package com.sugon.iris.sugoniris.controller.shengting;

import com.sugon.iris.sugondomain.beans.shengTing.ShanghaiJsrBean;
import com.sugon.iris.sugondomain.beans.shengTing.ShanghaiMinhangBean;
import com.sugon.iris.sugondomain.beans.shengTing.ShanghaijdcxxBean;
import com.sugon.iris.sugondomain.beans.shengTing.ShanghaikeyunBean;
import com.sugon.iris.sugondomain.beans.shengTing.base.StResponse;
import com.sugon.iris.sugonksservice.intf.shengTingIntf.ShengTingServiceIntf;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@RestController
@RequestMapping("/templateInfo")
public class ShengTingController {

    private static final String FAILED = "FAILED";

    @Resource
    private ShengTingServiceIntf shengTingServiceImpl;

    @CrossOrigin
    @PostMapping("/getKyxx")
    @ApiOperation(value = "客运信息")
    public List<StResponse> getKyxx(@RequestParam(value = "cardId") String cardId, @RequestParam(value = "minRownum") String minRownum, @RequestParam(value = "maxRownum") String maxRownum) throws IllegalAccessException{
        List<StResponse> stResponseList = null;
        try {
            stResponseList = shengTingServiceImpl.getKyxx(cardId,minRownum, maxRownum);
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
            stResponseList =  shengTingServiceImpl.getMinHanxx(cert_no,minRownum,maxRownum);
        }catch (Exception e){
            e.printStackTrace();

        }
        return stResponseList;
    }

    @CrossOrigin
    @PostMapping("/getJdcxx")
    @ApiOperation(value = "机动车信息")
    public List<StResponse> getJdcxx(@RequestParam(value = "hphm" ,required = true) String hphm,@RequestParam(value = "minRownum", required = true) String minRownum,@RequestParam(value = "maxRownum",required = true) String maxRownum) throws IllegalAccessException{
        List<StResponse> stResponseList = null;
        try {
            stResponseList =  shengTingServiceImpl.getJdcxx(hphm,minRownum,maxRownum);
        }catch (Exception e){
            e.printStackTrace();

        }
        return stResponseList;
    }

    @CrossOrigin
    @PostMapping("/getJdcxxByExcel")
    @ApiOperation(value = "民航信息")
    public List<StResponse<List<ShanghaijdcxxBean>>> getJdcxxByExcel(HttpServletRequest request, HttpServletResponse response) throws IllegalAccessException{
        List<StResponse<List<ShanghaijdcxxBean>>> stResponseList = null;
        try {
            request.setCharacterEncoding("UTF-8");
            response.setCharacterEncoding("UTF-8");
            MultipartHttpServletRequest multipartHttpServletRequest = (MultipartHttpServletRequest) request;
            List<MultipartFile> files = multipartHttpServletRequest.getFiles("file");
            stResponseList = shengTingServiceImpl.getJdcxxByExcel(files);
        }catch (Exception e){
            e.printStackTrace();
        }
        return stResponseList;
    }


    @CrossOrigin
    @PostMapping("/getJsrxx")
    @ApiOperation(value = "机动车信息")
    public List<StResponse> getJsrxx(@RequestParam(value = "sfzmhm") String sfzmhm,@RequestParam(value = "minRownum") String minRownum,@RequestParam(value = "maxRownum") String maxRownum) throws IllegalAccessException{
        List<StResponse> stResponseList = null;
        try {
            stResponseList =  shengTingServiceImpl.getJsrxx(sfzmhm,minRownum,maxRownum);
        }catch (Exception e){
            e.printStackTrace();

        }
        return stResponseList;
    }

    @CrossOrigin
    @PostMapping("/getJsrxxByExcel")
    @ApiOperation(value = "民航信息")
    public List<StResponse<List<ShanghaiJsrBean>>> getJsrxxByExcel(HttpServletRequest request, HttpServletResponse response) throws IllegalAccessException{
        List<StResponse<List<ShanghaiJsrBean>>> stResponseList = null;
        try {
            request.setCharacterEncoding("UTF-8");
            response.setCharacterEncoding("UTF-8");
            MultipartHttpServletRequest multipartHttpServletRequest = (MultipartHttpServletRequest) request;
            List<MultipartFile> files = multipartHttpServletRequest.getFiles("file");
            stResponseList = shengTingServiceImpl.getJsrxxByExcel(files);
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
            stResponseList = shengTingServiceImpl.getMinHanxxByExcel(files);
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
            stResponseList = shengTingServiceImpl.getKeyunByExcel(files);
        }catch (Exception e){
            e.printStackTrace();
        }
        return stResponseList;
    }
}
