package com.sugon.iris.sugonrest.neo4j;

import com.sugon.iris.sugondomain.beans.baseBeans.Error;
import com.sugon.iris.sugondomain.beans.baseBeans.RestResult;
import com.sugon.iris.sugondomain.enums.ErrorCode_Enum;
import com.sugon.iris.sugonservice.service.neo4jBaseService.Neo4jBaseService;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import javax.annotation.Resource;
import javax.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/neo4jBase")
public class Neo4jBaseRest {

    private static final String FAILED = "FAILED";

    private static final String SUCCESS_MESSAGE = "[创建成功]";

    private static final String FAILED_MESSAGE = "[创建失败]";

    @Resource
    private Neo4jBaseService neo4jBaseServiceImpl;

    @PostMapping("/addRelationBatch")
    public RestResult<Integer> addRelationBatch(@RequestParam(name="sourceTableName") @NotBlank(message = "sourceTableName不为空") String sourceTableName,
                                                @RequestParam(name="targetTableName") @NotBlank(message = "targetTableName不为空") String targetTableName,
                                                @RequestParam(name="relationship") @NotBlank(message = "relationship不为空") String relationship,
                                                @RequestParam(name="relationshipAttribute") @NotBlank(message = "relationshipAttribute不为空") String relationshipAttribute,
                                                @RequestParam(name="sourceFiled") @NotBlank(message = "sourceFiled不为空") String sourceFiled,
                                                @RequestParam(name="targetFiled") @NotBlank(message = "targetFiled不为空") String targetFiled,
                                                @RequestParam(name="sourceValue") @NotBlank(message = "sourceValue不为空") String sourceValue,
                                                @RequestParam(name="targetValue") @NotBlank(message = "targetValue不为空") String targetValue,
                                                @RequestParam(name="relationId") @NotBlank(message = "relationId不为空") String relationId) throws IllegalAccessException{
        RestResult<Integer> restResult = new RestResult();
        List<Error> errorList = new ArrayList<>();
        try {
           restResult.setObj(neo4jBaseServiceImpl.addRelationBatch( sourceTableName,
                    targetTableName,
                    relationship,
                    relationshipAttribute,
                    sourceFiled,
                    targetFiled,
                    sourceValue,
                    targetValue,
                    relationId,
                    errorList));
        }catch (Exception e){
            e.printStackTrace();
            errorList.add(new Error(ErrorCode_Enum.SYS_SUGON_001.getCode(),ErrorCode_Enum.SYS_SUGON_001.getMessage()));
        }
        if(!CollectionUtils.isEmpty(errorList)){
            restResult.setFlag(FAILED);
            restResult.setMessage(FAILED_MESSAGE);
            restResult.setErrorList(errorList);
        }else{
            restResult.setMessage(SUCCESS_MESSAGE);
        }
        return restResult;
    }


    @PostMapping("/addRelationById")
    public RestResult<Integer> addRelationById(@RequestParam(name="sourceLable") @NotBlank(message = "sourceLable不为空") String sourceLable,
                                                @RequestParam(name="targetLable") @NotBlank(message = "targetLable不为空") String targetLable,
                                                @RequestParam(name="sourceId") @NotBlank(message = "sourceId不为空") String sourceId,
                                                @RequestParam(name="targetId") @NotBlank(message = "targetId不为空") String targetId,
                                                @RequestParam(name="relationship") @NotBlank(message = "relationship不为空") String relationship,
                                                @RequestParam(name="relationshipAttribute") @NotBlank(message = "relationshipAttribute不为空") String relationshipAttribute,
                                                @RequestParam(name="relationId") @NotBlank(message = "relationId不为空") String relationId) throws IllegalAccessException{
        RestResult<Integer> restResult = new RestResult();
        List<Error> errorList = new ArrayList<>();
        try {
            restResult.setObj(neo4jBaseServiceImpl.addRelationById( sourceLable,
                     targetLable,
                     sourceId,
                     targetId,
                     relationship,
                     relationshipAttribute,
                     relationId,
                     errorList));
        }catch (Exception e){
            e.printStackTrace();
            errorList.add(new Error(ErrorCode_Enum.SYS_SUGON_001.getCode(),ErrorCode_Enum.SYS_SUGON_001.getMessage()));
        }
        if(!CollectionUtils.isEmpty(errorList)){
            restResult.setFlag(FAILED);
            restResult.setMessage(FAILED_MESSAGE);
            restResult.setErrorList(errorList);
        }else{
            restResult.setMessage(SUCCESS_MESSAGE);
        }
        return restResult;
    }
}
