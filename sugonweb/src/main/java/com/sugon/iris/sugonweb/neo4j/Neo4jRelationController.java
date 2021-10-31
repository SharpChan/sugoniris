package com.sugon.iris.sugonweb.neo4j;

import com.sugon.iris.sugonannotation.annotation.system.CurrentUser;
import com.sugon.iris.sugonannotation.annotation.system.LogInCheck;
import com.sugon.iris.sugondomain.beans.baseBeans.Error;
import com.sugon.iris.sugondomain.beans.baseBeans.RestResult;
import com.sugon.iris.sugondomain.beans.neo4jBeans.Elements;
import com.sugon.iris.sugondomain.beans.system.User;
import com.sugon.iris.sugondomain.dtos.neo4jDtos.Neo4jRelationDto;
import com.sugon.iris.sugondomain.dtos.systemDtos.MenuDto;
import com.sugon.iris.sugonservice.service.neo4jService.Neo4jRelationService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/neo4jRelation")
@Api(value = "neo4j图谱", tags = "图谱数据关系信息相关接口")
public class Neo4jRelationController {

    private static final String FAILED = "FAILED";

    @Resource
    private Neo4jRelationService neo4jRelationServiceImpl;

    /**
     * 查询模板信息
     */

    @RequestMapping("/getNeo4jAttribute")
    @LogInCheck(doLock = true,doProcess = true)
    @ApiOperation(value = "获取节点属性")
    public RestResult<List<MenuDto>> getFileDataGroup(@CurrentUser User user){
        RestResult<List<MenuDto>> restResult = new RestResult();
        List<Error> errorList = new ArrayList<>();
        try{
            restResult.setObj(neo4jRelationServiceImpl.findNeo4jNodeAttributeByUserId(user,errorList));
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

    @RequestMapping("/saveRelation")
    @LogInCheck(doLock = true,doProcess = true)
    @ApiOperation(value = "保存关系")
    @ApiImplicitParam(name = "neo4jRelationDto", value = "关系信息")
    public RestResult<Integer> saveRelation(@CurrentUser User user, @RequestBody Neo4jRelationDto neo4jRelationDto){
        RestResult<Integer> restResult = new RestResult();
        List<Error> errorList = new ArrayList<>();
        try{
            restResult.setObj(neo4jRelationServiceImpl.saveRelation(user,neo4jRelationDto,errorList));
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

    @RequestMapping("/getRelations")
    @LogInCheck(doLock = true,doProcess = true)
    @ApiOperation(value = "获取关系列表")
    public RestResult<List<Neo4jRelationDto>> getRelations(@CurrentUser User user){
        RestResult<List<Neo4jRelationDto>> restResult = new RestResult();
        List<Error> errorList = new ArrayList<>();
        try{
            restResult.setObj(neo4jRelationServiceImpl.getRelations(user,errorList));
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
     * 关系数据初始化
     * @param user
     * @param neo4jRelationDto
     * @return
     */
    @RequestMapping("/initRelation")
    @LogInCheck(doLock = true,doProcess = true)
    @ApiOperation(value = "关系数据初始化")
    @ApiImplicitParam(name = "neo4jRelationDto", value = "关系信息")
    public RestResult<Integer> initRelation(@CurrentUser User user, @RequestBody Neo4jRelationDto neo4jRelationDto){
        RestResult<Integer> restResult = new RestResult();
        List<Error> errorList = new ArrayList<>();
        try{
            restResult.setObj(neo4jRelationServiceImpl.initRelation(user,neo4jRelationDto,errorList));
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

    @RequestMapping("/getNeo4jRelations")
    @LogInCheck(doLock = true,doProcess = true)
    @ApiOperation(value = "关系数据初始化")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "relationship", value = "关系名称"),
            @ApiImplicitParam(name = "relationId", value = "关系id")
    })
    public RestResult<Elements> getNeo4jRelations(@RequestParam(value = "relationship") String  relationship,
                                                  @RequestParam(value = "relationId") String  relationId){
        RestResult<Elements> restResult = new RestResult();
        List<Error> errorList = new ArrayList<>();
        try{
            restResult.setObj(neo4jRelationServiceImpl.getNeo4jRelations( relationship, relationId, errorList));
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
