package com.sugon.iris.sugonweb.file;

import com.sugon.iris.sugonannotation.annotation.system.CurrentUser;
import com.sugon.iris.sugonannotation.annotation.system.LogInCheck;
import com.sugon.iris.sugondomain.beans.baseBeans.Error;
import com.sugon.iris.sugondomain.beans.baseBeans.RestResult;
import com.sugon.iris.sugondomain.beans.fileBeans.ExcelRow;
import com.sugon.iris.sugondomain.beans.system.User;
import com.sugon.iris.sugondomain.dtos.fileDtos.FileCaseDto;
import com.sugon.iris.sugondomain.dtos.fileDtos.FileParsingFailedDto;
import com.sugon.iris.sugonservice.service.ExcelService.ExcelService;
import com.sugon.iris.sugonservice.service.FileService.FileImportCountService;
import com.sun.xml.internal.messaging.saaj.util.ByteOutputStream;
import org.apache.poi.hssf.usermodel.*;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

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
    @RequestMapping("/getExcelqxxx")
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


    @RequestMapping("/getExcelqqq")
    @LogInCheck(doLock = true,doProcess = true)
    public void getExcel(HttpServletResponse response, HttpServletRequest request) throws IOException {

        HSSFWorkbook workbook = new HSSFWorkbook();
        HSSFSheet sheet = workbook.createSheet("信息表");
        List<User> classmateList = new ArrayList<>();
        User user = new User();
        user.setUserName("asdasd");
        user.setPoliceNo("213423");
        user.setImageUrl("adfwedf");
        user.setPassword("asdasd");
        User user2 = new User();
        user2.setUserName("asdasSad");
        user2.setPoliceNo("2134Sas23");
        user2.setImageUrl("adfwASasedf");
        user2.setPassword("asdaASassd");
        classmateList.add(user);
        classmateList.add(user2);

        // 设置要导出的文件的名字
        String fileName = "users"  + new Date() + ".xls";

        // 新增数据行，并且设置单元格数据
        int rowNum = 1;

        // headers表示excel表中第一行的表头 在excel表中添加表头
        String[] headers = { "id", "uid", "地址", "城市"};
        HSSFRow row = sheet.createRow(0);
        for(int i=0;i<headers.length;i++){
            HSSFCell cell = row.createCell(i);
            HSSFRichTextString text = new HSSFRichTextString(headers[i]);
            cell.setCellValue(text);
        }

        //在表中存放查询到的数据放入对应的列
        for (User item : classmateList) {
            HSSFRow row1 = sheet.createRow(rowNum);
            row1.createCell(0).setCellValue(item.getImageUrl());
            row1.createCell(1).setCellValue(item.getUserName());
            row1.createCell(2).setCellValue(item.getPoliceNo());
            row1.createCell(3).setCellValue(item.getPassword());
            rowNum++;
        }

        response.setContentType("application/vnd.ms-excel");
        response.setHeader("Content-disposition", "attachment;filename=" + fileName);
        response.flushBuffer();
        workbook.write(response.getOutputStream());
    }

    @RequestMapping("/getExcel")
    @LogInCheck(doLock = true,doProcess = true)
    public void findUserByIdCardToDown(HttpServletResponse response, HttpServletRequest request,@RequestBody  @RequestParam(value = "fileDetailId") Long  fileDetailId) {

        List<ExcelRow> excelRowList = new ArrayList<>();

        List<String> fields = new ArrayList<>();
        fields.add("字段1");
        fields.add("字段2");
        fields.add("字段3");
        HSSFWorkbook workbook = ExcelServiceImpl.getNewExcel(11L, excelRowList);
        try {
            response.setContentType("application/zip; charset=UTF-8");
            //返回客户端浏览器的版本号、类型
            String agent = request.getHeader("USER-AGENT");
            String downloadName = "压缩文件测试.zip";
            //针对IE或者以IE为内核的浏览器：
            if (agent.contains("MSIE") || agent.contains("Trident")) {
                downloadName = java.net.URLEncoder.encode(downloadName, "UTF-8");
            } else {
                downloadName = new String(downloadName.getBytes("UTF-8"), "ISO-8859-1");
            }
            response.setHeader("Content-disposition", "attachment;filename=" + downloadName);

            ZipOutputStream zipOutputStream = new ZipOutputStream(response.getOutputStream());
            //多个从这里就可遍历了
            // --start
            ZipEntry entry = new ZipEntry("第一个文件名.xls");
            zipOutputStream.putNextEntry(entry);

            ByteOutputStream byteOutputStream = new ByteOutputStream();
            workbook.write(byteOutputStream);
            byteOutputStream.writeTo(zipOutputStream);
//            zipOutputStream.write(workbook.getBytes());
            byteOutputStream.close();
            zipOutputStream.closeEntry();
            // --end
            zipOutputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @RequestMapping("/getErrorsExcel")
    @LogInCheck(doLock = true,doProcess = true)
    public RestResult<Boolean> getErrorsExcel(HttpServletResponse response, HttpServletRequest request,@RequestBody  @RequestParam(value = "fileDetailId") Long  fileDetailId) {
        RestResult<Boolean> restResult = new RestResult();
        List<Error> errorList = new ArrayList<>();
        try{
            restResult.setObj(fileImportCountServiceImpl.downloadErrorsExcel(response, request, fileDetailId, errorList));
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
