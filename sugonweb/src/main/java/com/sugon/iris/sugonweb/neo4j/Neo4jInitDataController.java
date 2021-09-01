package com.sugon.iris.sugonweb.neo4j;

import com.sugon.iris.sugonannotation.annotation.system.CurrentUser;
import com.sugon.iris.sugonannotation.annotation.system.LogInCheck;
import com.sugon.iris.sugondomain.beans.baseBeans.Error;
import com.sugon.iris.sugondomain.beans.baseBeans.RestResult;
import com.sugon.iris.sugondomain.beans.system.User;
import com.sugon.iris.sugondomain.dtos.fileDtos.FileTableDto;
import com.sugon.iris.sugondomain.dtos.neo4jDtos.Neo4jNodeAttributeDto;
import com.sugon.iris.sugonservice.service.neo4jService.Neo4jInitDatService;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/neo4jInitData")
public class Neo4jInitDataController {

    private static final String FAILED = "FAILED";

    @Resource
    private Neo4jInitDatService neo4jInitDatServiceImpl;




    @PostMapping("/getFileTables")
    @LogInCheck(doLock = true,doProcess = true)
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

    @PostMapping("/getAttribute")
    @LogInCheck(doLock = true,doProcess = true)
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
