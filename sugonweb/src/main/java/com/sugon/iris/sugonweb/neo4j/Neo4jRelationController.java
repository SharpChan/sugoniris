package com.sugon.iris.sugonweb.neo4j;

import com.sugon.iris.sugonannotation.annotation.system.CurrentUser;
import com.sugon.iris.sugonannotation.annotation.system.LogInCheck;
import com.sugon.iris.sugondomain.beans.baseBeans.Error;
import com.sugon.iris.sugondomain.beans.baseBeans.RestResult;
import com.sugon.iris.sugondomain.beans.system.User;
import com.sugon.iris.sugondomain.dtos.systemDtos.MenuDto;
import com.sugon.iris.sugonservice.service.neo4jService.Neo4jRelationService;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/neo4jRelation")
public class Neo4jRelationController {

    private static final String FAILED = "FAILED";

    @Resource
    private Neo4jRelationService neo4jRelationServiceImpl;

    /**
     * 查询模板信息
     */

    @RequestMapping("/getNeo4jAttribute")
    @LogInCheck(doLock = true,doProcess = true)
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
}
