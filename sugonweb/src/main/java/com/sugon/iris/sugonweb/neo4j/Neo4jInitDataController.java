package com.sugon.iris.sugonweb.neo4j;

import com.sugon.iris.sugonannotation.annotation.system.CurrentUser;
import com.sugon.iris.sugonannotation.annotation.system.LogInCheck;
import com.sugon.iris.sugondomain.beans.baseBeans.Error;
import com.sugon.iris.sugondomain.beans.baseBeans.RestResult;
import com.sugon.iris.sugondomain.beans.system.User;
import com.sugon.iris.sugondomain.dtos.fileDtos.FileTableDto;
import com.sugon.iris.sugondomain.dtos.neo4jDtos.Neo4jNodeAttributeDto;
import com.sugon.iris.sugondomain.dtos.neo4jDtos.Neo4jNodeInfoDto;
import com.sugon.iris.sugonservice.service.neo4jService.Neo4jInitDatService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/neo4jInitData")
@Api(value = "neo4j图谱", tags = "图谱数据初始化相关接口")
public class Neo4jInitDataController {

    private static final String FAILED = "FAILED";

    @Resource
    private Neo4jInitDatService neo4jInitDatServiceImpl;




    @PostMapping("/getFileTables")
    @LogInCheck(doLock = true,doProcess = true)
    @ApiOperation(value = "获取已经导入的数据表")
    public RestResult<List<FileTableDto>> getFileTables(@CurrentUser User user){

        RestResult<List<FileTableDto>> restResult = new RestResult();
        List<Error> errorList = new ArrayList<>();
        try{
            restResult.setObj(neo4jInitDatServiceImpl.getAllFileTableByUserid(user.getId(),errorList));
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

    @PostMapping("/initData")
    @LogInCheck(doLock = true,doProcess = true)
    @ApiOperation(value = "表数据初始化")
    @ApiImplicitParam(name = "fileTableDto", value = "已导入表的表信息")
    public RestResult<Integer> initData(@CurrentUser User user, @RequestBody FileTableDto fileTableDto){
        RestResult<Integer> restResult = new RestResult();
        List<Error> errorList = new ArrayList<>();
        try{
            restResult.setObj(neo4jInitDatServiceImpl.initData(user,fileTableDto,errorList));
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

    @PostMapping("/modifyNodeInfo")
    @LogInCheck(doLock = true,doProcess = true)
    @ApiOperation(value = "修改节点信息")
    @ApiImplicitParam(name = "neo4jNodeInfoDto", value = "图谱节点信息")
    public RestResult<Integer> modifyLabel(@CurrentUser User user, @RequestBody Neo4jNodeInfoDto neo4jNodeInfoDto){
        RestResult<Integer> restResult = new RestResult();
        List<Error> errorList = new ArrayList<>();
        try{
            restResult.setObj(neo4jInitDatServiceImpl.modifyNodeInfo(user,neo4jNodeInfoDto,errorList));
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

    @PostMapping("/getAttribute")
    @LogInCheck(doLock = true,doProcess = true)
    @ApiOperation(value = "获取节点属性")
    @ApiImplicitParam(name = "fileTableId", value = "数据表id")
    public RestResult<List<Neo4jNodeAttributeDto>> getAttribute(@RequestParam(value = "fileTableId") Long  fileTableId){
        RestResult<List<Neo4jNodeAttributeDto>> restResult = new RestResult();
        List<Error> errorList = new ArrayList<>();
        try{
            restResult.setObj(neo4jInitDatServiceImpl.getAttributes(fileTableId,errorList));
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

    @PostMapping("/attributeSave")
    @LogInCheck(doLock = true,doProcess = true)
    @ApiOperation(value = "属性保存")
    @ApiImplicitParam(name = "neo4jNodeAttributeDto", value = "节点属性信息")
    public RestResult<Integer> attributeSave(@RequestBody Neo4jNodeAttributeDto neo4jNodeAttributeDto){

        RestResult<Integer> restResult = new RestResult();
        List<Error> errorList = new ArrayList<>();
        try{
            restResult.setObj(neo4jInitDatServiceImpl.attributeSave(neo4jNodeAttributeDto,errorList));
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

    @PostMapping("/attributeUpdate")
    @LogInCheck(doLock = true,doProcess = true)
    @ApiOperation(value = "属性修改")
    @ApiImplicitParam(name = "neo4jNodeAttributeDto", value = "节点属性信息")
    public RestResult<Integer> attributeUpdate(@RequestBody Neo4jNodeAttributeDto neo4jNodeAttributeDto){

        RestResult<Integer> restResult = new RestResult();
        List<Error> errorList = new ArrayList<>();
        try{
            restResult.setObj(neo4jInitDatServiceImpl.attributeUpdate(neo4jNodeAttributeDto,errorList));
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

    @PostMapping("/attributeDelete")
    @LogInCheck(doLock = true,doProcess = true)
    @ApiOperation(value = "属性删除")
    @ApiImplicitParam(name = "id", value = "节点属性信息id")
    public RestResult<Integer> attributeDelete(@RequestParam(value = "id") Long  id){

        RestResult<Integer> restResult = new RestResult();
        List<Error> errorList = new ArrayList<>();
        try{
            restResult.setObj(neo4jInitDatServiceImpl.deleteAttribute(id,errorList));
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
