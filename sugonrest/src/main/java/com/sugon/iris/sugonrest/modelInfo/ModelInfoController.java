package com.sugon.iris.sugonrest.modelInfo;

import com.sugon.iris.sugondomain.beans.baseBeans.RestResult;
import com.sugon.iris.sugondomain.enums.ErrorCode_Enum;
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

    private static final String SUCCESS = "FAILED";

    private static final String MESSAGE_01 = "[通过警号和模型id查询模型信息接口失败]";

    private static final String MESSAGE_02 = "[通过警号和模型Id查询模型信息失败]";

    private static final String MESSAGE_03 = "[通过警号和模型Id查询模型信息失败]";

    private static final String MESSAGE_04 = "[获取所有模型信息失败]";

    private static final String MESSAGE_05 = "[获取表字段信息失败]";

    private static final String MESSAGE_06 = "[模型执行失败]";

    private static final String SUCCESS_MESSAGE = "[查询成功]";

    private static final String FAILED_MESSAGE = "[查询失败]";

    /**
     * 描述：通过警号和模型id查询模型信息
     * @param policeNo：警号
     * @param modelId：模型id
     * @return
     * @throws IllegalAccessException
     */
    @PostMapping("/getModelInfosByUserNameAndModelId")
    public RestResult<ModelBeanDTO> modelInfo(@RequestParam(name="policeNo") String policeNo,
                                              @RequestParam(name="modelId") @NotBlank(message = "modelId不为空") String modelId) throws IllegalAccessException{
        RestResult<ModelBeanDTO> restResult = new RestResult();
        List<Error> errorList = new ArrayList<>();
        try {
            restResult.setObj(modelInfoServiceImpl.getModelInfosByUserNameAndModelId(policeNo, modelId, errorList));
        }catch (Exception e){
            e.printStackTrace();
            errorList.add(new Error(ErrorCode_Enum.SYS_SUGON_001.getCode(),ErrorCode_Enum.SYS_SUGON_001.getMessage()+MESSAGE_01,e.toString()));
        }
        if(!CollectionUtils.isEmpty(errorList)){
            restResult.setFlag(FAILED);
            restResult.setMessage(FAILED_MESSAGE);
            restResult.setErrorList(errorList);
        }else{
            restResult.setFlag(SUCCESS);
            restResult.setMessage(SUCCESS_MESSAGE);
        }
        return restResult;
    }

    /**
     * 描述：通过警号查询他的私有模型信息
     * @param policeNo：警号
     * @return
     * @throws IllegalAccessException
     */
    @PostMapping("/getPrivateModelInfosByUserName")
    public RestResult<List<ModelBeanDTO>> getPrivateModelInfosByUserName(@RequestParam(name="policeNo") @NotBlank(message = "policeNo不为空") String policeNo) throws IllegalAccessException{
        RestResult<List<ModelBeanDTO>> restResult = new RestResult();
        List<Error> errorList = new ArrayList<>();
        try {
            restResult.setObj(modelInfoServiceImpl.getPrivateModelInfosByUserName(policeNo, errorList));
        }catch (Exception e){
            errorList.add(new Error(ErrorCode_Enum.SYS_SUGON_001.getCode(),ErrorCode_Enum.SYS_SUGON_001.getMessage()+MESSAGE_02,e.toString()));
        }
        if(!CollectionUtils.isEmpty(errorList)){
            restResult.setFlag(FAILED);
            restResult.setMessage(FAILED_MESSAGE);
            restResult.setErrorList(errorList);
        }else{
            restResult.setFlag(SUCCESS);
            restResult.setMessage(SUCCESS_MESSAGE);
        }
        return restResult;
    }

    /**
     * 描述：通过警号查询他的公共模型信息
     * @param policeNo
     * @return
     * @throws IllegalAccessException
     */
    @PostMapping("/getPublicModelInfosByUserName")
    public RestResult<List<ModelBeanDTO>> getPublicModelInfosByUserName(@RequestParam(name="policeNo") @NotBlank(message = "policeNo不为空") String policeNo) throws IllegalAccessException{
        RestResult<List<ModelBeanDTO>> restResult = new RestResult();
        List<Error> errorList = new ArrayList<>();
        try {
            restResult.setObj(modelInfoServiceImpl.getPublicModelInfosByUserName(policeNo, errorList));
        }catch (Exception e){
            errorList.add(new Error(ErrorCode_Enum.SYS_SUGON_001.getCode(),ErrorCode_Enum.SYS_SUGON_001.getMessage()+MESSAGE_03,e.toString()));
        }
        if(!CollectionUtils.isEmpty(errorList)){
            restResult.setFlag(FAILED);
            restResult.setMessage(FAILED_MESSAGE);
            restResult.setErrorList(errorList);
        }else{
            restResult.setFlag(SUCCESS);
            restResult.setMessage(SUCCESS_MESSAGE);
        }
        return restResult;
    }

    /**
     * 描述：查询所有的模型信息
     * @return
     * @throws IllegalAccessException
     */
    @PostMapping("/allModelInfos")
    @ResponseBody
    public RestResult<List<ModelBeanDTO>> allModelInfos() throws IllegalAccessException{
        RestResult<List<ModelBeanDTO>> restResult = new RestResult();
        List<Error> errorList = new ArrayList<>();
        try {
            restResult.setObj(modelInfoServiceImpl.getAllModelInfos( errorList));
        }catch (Exception e){
            errorList.add(new Error(ErrorCode_Enum.SYS_SUGON_001.getCode(),ErrorCode_Enum.SYS_SUGON_001.getMessage()+MESSAGE_04,e.toString()));
        }
        if(!CollectionUtils.isEmpty(errorList)){
            restResult.setFlag(FAILED);
            restResult.setMessage(FAILED_MESSAGE);
            restResult.setErrorList(errorList);
        }else{
            restResult.setFlag(SUCCESS);
            restResult.setMessage(SUCCESS_MESSAGE);
        }
        return restResult;
    }

    /**
     * 描述：通过表编号查询表字段
     * @param tableId
     * @return
     * @throws IllegalAccessException
     */
    @PostMapping("/tableColumnsByTableId")
    @ResponseBody
    public RestResult<List<ResultColumnBeanDTO>> modelInfo(@RequestParam(name="tableId")@NotBlank(message = "tableId不为空") String tableId) throws IllegalAccessException{
        RestResult<List<ResultColumnBeanDTO>> restResult = new RestResult();
        List<Error> errorList = new ArrayList<>();
        try {
            restResult.setObj(modelInfoServiceImpl.getTableColumnByTableId(tableId,errorList));
        }catch (Exception e){
            errorList.add(new Error(ErrorCode_Enum.SYS_SUGON_001.getCode(),ErrorCode_Enum.SYS_SUGON_001.getMessage()+MESSAGE_05,e.toString()));
        }
        if(!CollectionUtils.isEmpty(errorList)){
            restResult.setFlag(FAILED);
            restResult.setMessage(FAILED_MESSAGE);
            restResult.setErrorList(errorList);
        }else{
            restResult.setFlag(SUCCESS);
            restResult.setMessage(SUCCESS_MESSAGE);
        }
        return restResult;
    }

    /**
     * 描述:通过模型ID调用模型执行接口来执行模型
     * @param modelId：模型id
     * @return
     * @throws IllegalAccessException
     */
    @PostMapping("/modelRun")
    @ResponseBody
    public RestResult<String> modelRun(@RequestParam(name="modelId")@NotBlank(message = "modelId不为空") String modelId) throws IllegalAccessException{
        RestResult<String> restResult = new RestResult();
        List<Error> errorList = new ArrayList<>();
        try {
            restResult.setObj(modelInfoServiceImpl.modelRun(modelId,errorList));
        }catch (Exception e){
            errorList.add(new Error(ErrorCode_Enum.SYS_SUGON_001.getCode(),ErrorCode_Enum.SYS_SUGON_001.getMessage()+MESSAGE_06,e.toString()));
        }
        if(!CollectionUtils.isEmpty(errorList)){
            restResult.setFlag(FAILED);
            restResult.setMessage(FAILED_MESSAGE);
            restResult.setErrorList(errorList);
        }else{
            restResult.setFlag(SUCCESS);
            restResult.setMessage(SUCCESS_MESSAGE);
        }
        return restResult;
    }
}
