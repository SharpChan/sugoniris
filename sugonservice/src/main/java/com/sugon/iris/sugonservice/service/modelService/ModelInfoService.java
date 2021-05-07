package com.sugon.iris.sugonservice.service.modelService;

import java.io.IOException;
import java.util.List;

import com.sugon.iris.sugondomain.beans.baseBeans.Error;
import com.sugon.iris.sugondomain.dtos.sdmModelInfosDtos.*;

public interface ModelInfoService {

    /**
     * 组装全部模型信息
     * @return
     * @throws IllegalAccessException
     */
    List<ModelBeanDTO> getAllModelInfos(List<Error> errorList) throws IllegalAccessException;

    ModelBeanDTO getModelInfosByUserNameAndModelId(String policeNo,String ModelId,List<Error> errorList) throws IllegalAccessException;

    List<ModelBeanDTO> getPrivateModelInfosByUserName(String policeNo, List<Error> errorList) throws IllegalAccessException;

    List<ModelBeanDTO> getPublicModelInfosByUserName(String policeNo, List<Error> errorList) throws IllegalAccessException;

    List<ResultColumnBeanDTO> getTableColumnByTableId(String tableId,List<Error> errorList) throws IllegalAccessException;

    String modelRun(String modelId,List<Error> errorList) throws IOException, IllegalAccessException;
}
