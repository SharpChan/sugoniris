package com.sugon.iris.sugonweb.file;

import com.sugon.iris.sugonannotation.annotation.system.CurrentUser;
import com.sugon.iris.sugonannotation.annotation.system.LogInCheck;
import com.sugon.iris.sugondomain.beans.baseBeans.Error;
import com.sugon.iris.sugondomain.beans.baseBeans.RestResult;
import com.sugon.iris.sugondomain.beans.system.User;
import com.sugon.iris.sugondomain.dtos.fileDtos.FileRinseDetailDto;
import com.sugon.iris.sugondomain.dtos.fileDtos.FileRinseGroupDto;
import com.sugon.iris.sugondomain.dtos.fileDtos.FileRinseRegularDto;
import com.sugon.iris.sugonservice.service.fileService.FileRinseDetailService;
import com.sugon.iris.sugonservice.service.fileService.FileRinseRegularService;
import com.sugon.iris.sugonservice.service.fileService.FileRinseService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/fileRinse")
@Api(value = "数据清洗组", tags = "数据清洗组")
public class FileRinseController {

    private static final String FAILED = "FAILED";

    @Resource
    private FileRinseService FileRinseServiceImpl;

    @Resource
    private FileRinseDetailService fileRinseDetailServiceImpl;

    @Resource
    private FileRinseRegularService fileRinseRegularServiceImpl;

    @RequestMapping("/addFileRinse")
    @LogInCheck(doLock = true,doProcess = true)
    @ApiOperation(value = "保存清洗组数据")
    @ApiImplicitParam(name = "fileRinseDto", value = "清洗组信息")
    public RestResult<Integer> addFileRinse(@CurrentUser User user, @RequestBody FileRinseGroupDto fileRinseDto) {
        RestResult<Integer> restResult = new RestResult();
        List<Error> errorList = new ArrayList<>();
        fileRinseDto.setUserId(user.getId());
        try{
            restResult.setObj(FileRinseServiceImpl.add(fileRinseDto,errorList));
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

    @RequestMapping("/getFileRinses")
    @LogInCheck(doLock = true,doProcess = true)
    @ApiOperation(value = "获取清洗组数据")
    public RestResult<List<FileRinseGroupDto>> getFileRinses(@CurrentUser User user) {
        RestResult<List<FileRinseGroupDto>> restResult = new RestResult();
        List<Error> errorList = new ArrayList<>();
        try{
            restResult.setObj(FileRinseServiceImpl.findFileRinseByUserId(user.getId(),errorList));
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

    @RequestMapping("/modifyFileRinse")
    @LogInCheck(doLock = true,doProcess = true)
    @ApiOperation(value = "修改清洗组数据")
    @ApiImplicitParam(name = "fileRinseDto", value = "清洗组信息")
    public RestResult<Integer> modifyFileRinse(@CurrentUser User user, @RequestBody FileRinseGroupDto fileRinseDto) {
        RestResult<Integer> restResult = new RestResult();
        List<Error> errorList = new ArrayList<>();
        fileRinseDto.setUserId(user.getId());
        try{
            restResult.setObj(FileRinseServiceImpl.modifyFileRinse(fileRinseDto,errorList));
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

    @RequestMapping("/deleteFileRinseGroup")
    @LogInCheck(doLock = true,doProcess = true)
    @ApiOperation(value = "删除清洗组数据")
    @ApiImplicitParam(name = "id", value = "数据组id")
    public RestResult<Integer> deleteFileRinseGroup(@CurrentUser User user, long id) {
        RestResult<Integer> restResult = new RestResult();
        List<Error> errorList = new ArrayList<>();
        try{
            restResult.setObj(FileRinseServiceImpl.deleteFileRinse(id,errorList));
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


    @RequestMapping("/getFileRinseDetailsByGroupId")
    @LogInCheck(doLock = true,doProcess = true)
    @ApiOperation(value = "获取清洗组数据")
    @ApiImplicitParam(name = "groupId", value = "数据组id")
    public RestResult<List<FileRinseDetailDto>> getFileRinseDetailsByGroupId(@CurrentUser User user,Long groupId) {
        RestResult<List<FileRinseDetailDto>> restResult = new RestResult();
        List<Error> errorList = new ArrayList<>();
        try{
            restResult.setObj(fileRinseDetailServiceImpl.findFileRinseDetailByGroupId(groupId,errorList));
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
     * 保存清洗详细信息
     * @param user
     * @param fileRinseDetailDto
     * @return
     */
    @RequestMapping("/addFileRinseDetail")
    @LogInCheck(doLock = true,doProcess = true)
    @ApiOperation(value = "保存清洗详细信息")
    @ApiImplicitParam(name = "fileRinseDetailDto", value = "清洗详细信息")
    public RestResult<Long> addFileRinseDetail(@CurrentUser User user, @RequestBody FileRinseDetailDto fileRinseDetailDto) {
        RestResult<Long> restResult = new RestResult();
        List<Error> errorList = new ArrayList<>();
        fileRinseDetailDto.setUserId(user.getId());
        try{
            restResult.setObj(fileRinseDetailServiceImpl.add(fileRinseDetailDto,errorList));
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

    @RequestMapping("/removeFileRinseDetail")
    @LogInCheck(doLock = true,doProcess = true)
    @ApiOperation(value = "删除清洗详细信息")
    @ApiImplicitParam(name = "id", value = "清洗详细信息id")
    public RestResult<Integer> removeFileRinseDetail(@CurrentUser User user, Long  id) {
        RestResult<Integer> restResult = new RestResult();
        List<Error> errorList = new ArrayList<>();
        try{
            restResult.setObj(fileRinseDetailServiceImpl.deleteFileRinse(id,errorList));
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

    @RequestMapping("/updateFileRinseDetail")
    @LogInCheck(doLock = true,doProcess = true)
    @ApiOperation(value = "修改清洗详细信息")
    @ApiImplicitParam(name = "fileRinseDetailDto", value = "清洗详细信息")
    public RestResult<Integer> updateFileRinseDetail(@CurrentUser User user, @RequestBody FileRinseDetailDto fileRinseDetailDto) {
        RestResult<Integer> restResult = new RestResult();
        List<Error> errorList = new ArrayList<>();
        try{
            restResult.setObj(fileRinseDetailServiceImpl.modifyFileRinse(fileRinseDetailDto,errorList));
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

    @RequestMapping("/saveFileRinseRegular")
    @LogInCheck(doLock = true,doProcess = true)
    @ApiOperation(value = "保存清洗表达式")
    @ApiImplicitParam(name = "fileRinseDetailDto", value = "清洗详细信息")
    public RestResult<Integer> saveFileRinseRegular(@CurrentUser User user, @RequestBody FileRinseRegularDto fileRinseRegularDto) {
        RestResult<Integer> restResult = new RestResult();
        List<Error> errorList = new ArrayList<>();
        fileRinseRegularDto.setUserId(user.getId());
        try{
            restResult.setObj(fileRinseRegularServiceImpl.add(fileRinseRegularDto,errorList));
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

    @RequestMapping("/deleteFileRinse")
    @LogInCheck(doLock = true,doProcess = true)
    @ApiOperation(value = "删除清洗详细信息")
    @ApiImplicitParam(name = "id", value = "清洗详细信息id")
    public RestResult<Integer> deleteFileRinse(@CurrentUser User user,  Long  id) {
        RestResult<Integer> restResult = new RestResult();
        List<Error> errorList = new ArrayList<>();
        try{
            restResult.setObj(fileRinseDetailServiceImpl.deleteFileRinse(id,errorList));
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
