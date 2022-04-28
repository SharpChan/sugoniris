package com.sugon.iris.sugonweb.search;

import com.sugon.iris.sugonannotation.annotation.system.BussLog;
import com.sugon.iris.sugonannotation.annotation.system.CurrentUser;
import com.sugon.iris.sugonannotation.annotation.system.LogInCheck;
import com.sugon.iris.sugondomain.beans.baseBeans.Error;
import com.sugon.iris.sugondomain.beans.baseBeans.RestResult;
import com.sugon.iris.sugondomain.beans.system.User;
import com.sugon.iris.sugondomain.dtos.searchDtos.TableRecordSearchDto;
import com.sugon.iris.sugonservice.service.searchService.TableRecordSearchService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/search")
@Api(value = "数据搜索", tags = "数据搜索相关接口")
public class TableRecordSearchController {

    private static final String FAILED = "FAILED";

    @Resource
    private TableRecordSearchService tableRecordSearchServiceImpl;

    @RequestMapping("/searchAllTables")
    @BussLog
    @LogInCheck(doLock = true,doProcess = true)
    @ApiOperation(value = "通过条件获取查询结果")
    @ApiImplicitParam(name = "condition", value = "查询条件")
    public RestResult<List<TableRecordSearchDto>> getFileTemplates(@CurrentUser User user, @RequestParam(value = "condition") String  condition,
                                                                   @RequestParam(value = "offset") String  offset,
                                                                   @RequestParam(value = "perSize") String  perSize){
        RestResult<List<TableRecordSearchDto>> restResult = new RestResult();
        List<Error> errorList = new ArrayList<>();
        try{
            restResult.setObj(tableRecordSearchServiceImpl.getRecordsByUserId(user.getId(),condition,offset,perSize,errorList));
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

    @RequestMapping("/searchAllTablesTotalCount")
    @BussLog
    @LogInCheck(doLock = true,doProcess = true)
    @ApiOperation(value = "通过条件获取查询结果")
    @ApiImplicitParam(name = "condition", value = "查询条件")
    public RestResult<Integer> getFileTemplatesTotalCount(@CurrentUser User user, @RequestParam(value = "condition") String  condition){
        RestResult<Integer> restResult = new RestResult();
        List<Error> errorList = new ArrayList<>();
        try{
            restResult.setObj(tableRecordSearchServiceImpl.getRecordCountByUserId(user.getId(),condition,errorList));
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
