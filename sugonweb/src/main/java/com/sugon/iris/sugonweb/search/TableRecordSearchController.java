package com.sugon.iris.sugonweb.search;

import com.sugon.iris.sugonannotation.annotation.system.CurrentUser;
import com.sugon.iris.sugonannotation.annotation.system.LogInCheck;
import com.sugon.iris.sugondomain.beans.baseBeans.Error;
import com.sugon.iris.sugondomain.beans.baseBeans.RestResult;
import com.sugon.iris.sugondomain.beans.system.User;
import com.sugon.iris.sugondomain.dtos.searchDtos.TableRecordSearchDto;
import com.sugon.iris.sugonservice.service.searchService.TableRecordSearchService;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/search")
public class TableRecordSearchController {

    private static final String FAILED = "FAILED";

    @Resource
    private TableRecordSearchService tableRecordSearchServiceImpl;

    @RequestMapping("/searchAllTables")
    @LogInCheck(doLock = true,doProcess = true)
    public RestResult<List<TableRecordSearchDto>> getFileTemplates(@CurrentUser User user, @RequestParam(value = "condition") String  condition){
        RestResult<List<TableRecordSearchDto>> restResult = new RestResult();
        List<Error> errorList = new ArrayList<>();
        try{
            restResult.setObj(tableRecordSearchServiceImpl.getRecordsByUserId(user.getId(),condition,errorList));
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
