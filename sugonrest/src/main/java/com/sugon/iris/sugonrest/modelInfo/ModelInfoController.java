package com.sugon.iris.sugonrest.modelInfo;

import com.sugon.iris.sugondomain.beans.baseBeans.RestResult;
import com.sugon.iris.sugonservice.service.modelService.ModelInfoService;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;
import com.sugon.iris.sugondomain.beans.baseBeans.Error;
import java.util.ArrayList;
import java.util.List;
import com.sugon.iris.sugondomain.dtos.sdmModelInfosDtos.*;

import javax.annotation.Resource;
import javax.validation.constraints.NotBlank;

@RestController
@RequestMapping("/model")
public class ModelInfoController {

    @Resource
    private ModelInfoService  modelInfoServiceImpl;

    private static final String FAILED = "FAILED";

    @PostMapping("/getModelInfosByUserNameAndModelId")
    public RestResult<ModelBeanDTO> modelInfo(@RequestParam(name="policeNo") String policeNo,
                                              @RequestParam(name="modelId") @NotBlank(message = "modelId不为空") String modelId) throws IllegalAccessException{
        RestResult<ModelBeanDTO> restResult = new RestResult();
        List<Error> errorList = new ArrayList<>();
        try {
            restResult.setObj(modelInfoServiceImpl.getModelInfosByUserNameAndModelId(policeNo, modelId, errorList));
        }catch (Exception e){
            errorList.add(new Error("sys-sugon-001","通过警号和模型Id查询模型信息失败",e.toString()));
        }
        if(!CollectionUtils.isEmpty(errorList)){
            restResult.setFlag(FAILED);
            restResult.setMessage("查询失败");
            restResult.setErrorList(errorList);
        }else{
            restResult.setMessage("查询成功");
        }
        return restResult;
    }

    @PostMapping("/getPrivateModelInfosByUserName")
    public RestResult<List<ModelBeanDTO>> getPrivateModelInfosByUserName(@RequestParam(name="policeNo") @NotBlank(message = "policeNo不为空") String policeNo) throws IllegalAccessException{
        RestResult<List<ModelBeanDTO>> restResult = new RestResult();
        List<Error> errorList = new ArrayList<>();
        try {
            restResult.setObj(modelInfoServiceImpl.getPrivateModelInfosByUserName(policeNo, errorList));
        }catch (Exception e){
            errorList.add(new Error("sys-sugon-001","通过警号和模型Id查询模型信息失败",e.toString()));
        }
        if(!CollectionUtils.isEmpty(errorList)){
            restResult.setFlag(FAILED);
            restResult.setMessage("查询失败");
            restResult.setErrorList(errorList);
        }else{
            restResult.setMessage("查询成功");
        }
        return restResult;
    }

    @PostMapping("/getPublicModelInfosByUserName")
    public RestResult<List<ModelBeanDTO>> getPublicModelInfosByUserName(@RequestParam(name="policeNo") @NotBlank(message = "policeNo不为空") String policeNo) throws IllegalAccessException{
        RestResult<List<ModelBeanDTO>> restResult = new RestResult();
        List<Error> errorList = new ArrayList<>();
        try {
            restResult.setObj(modelInfoServiceImpl.getPublicModelInfosByUserName(policeNo, errorList));
        }catch (Exception e){
            errorList.add(new Error("sys-sugon-001","通过警号和模型Id查询模型信息失败",e.toString()));
        }
        if(!CollectionUtils.isEmpty(errorList)){
            restResult.setFlag(FAILED);
            restResult.setMessage("查询失败");
            restResult.setErrorList(errorList);
        }else{
            restResult.setMessage("查询成功");
        }
        return restResult;
    }

    @PostMapping("/allModelInfos")
    @ResponseBody
    public RestResult<List<ModelBeanDTO>> allModelInfos() throws IllegalAccessException{
        RestResult<List<ModelBeanDTO>> restResult = new RestResult();
        List<Error> errorList = new ArrayList<>();
        try {
            restResult.setObj(modelInfoServiceImpl.getAllModelInfos( errorList));
        }catch (Exception e){
            errorList.add(new Error("sys-sugon-001","获取所有模型信息失败",e.toString()));
        }
        if(!CollectionUtils.isEmpty(errorList)){
            restResult.setFlag(FAILED);
            restResult.setMessage("查询失败");
            restResult.setErrorList(errorList);
        }else{
            restResult.setMessage("查询成功");
        }
        return restResult;
    }

    @PostMapping("/tableColumnsByTableId")
    @ResponseBody
    public RestResult<List<ResultColumnBeanDTO>> modelInfo(@RequestParam(name="tableId")@NotBlank(message = "tableId不为空") String tableId) throws IllegalAccessException{
        RestResult<List<ResultColumnBeanDTO>> restResult = new RestResult();
        List<Error> errorList = new ArrayList<>();
        try {
            restResult.setObj(modelInfoServiceImpl.getTableColumnByTableId(tableId,errorList));
        }catch (Exception e){
            errorList.add(new Error("sys-sugon-001","获取表字段信息失败",e.toString()));
        }
        if(!CollectionUtils.isEmpty(errorList)){
            restResult.setFlag(FAILED);
            restResult.setMessage("查询失败");
            restResult.setErrorList(errorList);
        }else{
            restResult.setMessage("查询成功");
        }
        return restResult;
    }

    @PostMapping("/modelRun")
    @ResponseBody
    public RestResult<Integer> modelRun(@RequestParam(name="modelId")@NotBlank(message = "modelId不为空") String modelId) throws IllegalAccessException{
        RestResult<Integer> restResult = new RestResult();
        List<Error> errorList = new ArrayList<>();
        try {
            restResult.setObj(modelInfoServiceImpl.modelRun(modelId,errorList));
        }catch (Exception e){
            errorList.add(new Error("sys-sugon-001","模型执行失败",e.toString()));
        }
        if(!CollectionUtils.isEmpty(errorList)){
            restResult.setFlag(FAILED);
            restResult.setMessage("查询失败");
            restResult.setErrorList(errorList);
        }else{
            restResult.setMessage("查询成功");
        }
        return restResult;
    }
}
