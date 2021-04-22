package com.sugon.iris.sugonservice.impl.modelServiceImpl;

import com.alibaba.fastjson.JSON;
import com.sugon.iris.sugondata.mybaties.mapper.db1.ModelDatasourceFieldMapper;
import com.sugon.iris.sugondata.mybaties.mapper.db1.ModelDatasourceMapper;
import com.sugon.iris.sugondata.mybaties.mapper.db1.ModelManagerShareMapper;
import com.sugon.iris.sugondata.mybaties.mapper.db1.ModelMapper;
import com.sugon.iris.sugondomain.beans.baseBeans.Error;
import com.sugon.iris.sugondomain.dtos.sdmModelInfosDtos.ModelBeanDTO;
import com.sugon.iris.sugondomain.entities.mybatiesEntity.db1.JmUserEntity;
import com.sugon.iris.sugondomain.entities.mybatiesEntity.db1.ModelDatasourceEntity;
import com.sugon.iris.sugondomain.entities.mybatiesEntity.db1.ModelEntity;
import com.sugon.iris.sugondomain.entities.mybatiesEntity.db1.ResultColumnEntity;
import com.sugon.iris.sugonservice.service.httpClientService.HttpClientService;
import com.sugon.iris.sugonservice.service.modelService.ModelInfoService;
import com.sugon.iris.sugondomain.dtos.sdmModelInfosDtos.*;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.cookie.Cookie;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import com.sugon.iris.sugondomain.dtos.moChuangGongchangDtos.*;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.sugon.iris.sugoncommon.publicUtils.PublicUtils.trans;

@Service
public class ModelInfoServiceImpl implements ModelInfoService {
    @Resource
    private ModelMapper modelMapper;

    @Resource
    private ModelDatasourceMapper modelDatasourceMapper;

    @Resource
    private ModelManagerShareMapper modelManagerShareMapper;

    @Resource
    private ModelDatasourceFieldMapper modelDatasourceFieldMapper;

    @Resource
    private HttpClientService httpClientServiceImpl;

    @Value("${sdm.jmhost}")
    private String jmhost;

    @Value("${sdm.bashUrl}")
    private String bashUrl;

    @Override
    public List<ModelBeanDTO> getAllModelInfos(List<Error> errorList) throws IllegalAccessException{
        List<ModelBeanDTO> ModelBeanDTOList = new ArrayList<>();
        List<ModelEntity> modelEntityList = null;
        try {
            modelEntityList = modelMapper.selectModelList();
        }catch (Exception e){
            Error error = new Error("sys-db-001","查询表t_model_manager出错",e.toString());
            errorList.add(error);
        }
        for(ModelEntity modelEntity : modelEntityList){
            if(StringUtils.isEmpty(modelEntity.getModelId())){
                continue;
            }
            ModelBeanDTO modelBeanDTO = new ModelBeanDTO();
            ModelBeanDTOList.add(modelBeanDTO);
            setModelDetailInfo(errorList, modelEntity, modelBeanDTO);
        }
        return ModelBeanDTOList;
    }

    @Override
    public ModelBeanDTO getModelInfosByUserNameAndModelId(String policeNo, String ModelId, List<Error> errorList) throws IllegalAccessException {
        ModelBeanDTO modelBeanDTO = new ModelBeanDTO();
        ModelEntity modelEntity = null;
        try {
            modelEntity = modelMapper.findOneByUserNameAndModelId(policeNo, ModelId);
        }catch (Exception e){
            e.printStackTrace();
            errorList.add(new Error("sys-db-001","通过警号和模型Id查询模型信息失败",e.toString()));
        }
        setModelDetailInfo(errorList, modelEntity, modelBeanDTO);
        return modelBeanDTO;
    }

    @Override
    public List<ModelBeanDTO> getPrivateModelInfosByUserName(String policeNo, List<Error> errorList) throws IllegalAccessException {
        List<ModelBeanDTO> modelBeanDTOList = new ArrayList<>();
        List<ModelEntity> modelEntityList = null;
        try {
            modelEntityList = modelMapper.findPrivateByUserNameAndModelId(policeNo);
        }catch (Exception e){
            e.printStackTrace();
            errorList.add(new Error("sys-db-001","通过警号查询模型信息失败",e.toString()));
        }

       for(ModelEntity modelEntity : modelEntityList){
           ModelBeanDTO modelBeanDTO = new ModelBeanDTO();
           modelBeanDTOList.add(modelBeanDTO);
           setModelDetailInfo(errorList, modelEntity, modelBeanDTO);
       }
        return modelBeanDTOList;
    }

    @Override
    public List<ModelBeanDTO> getPublicModelInfosByUserName(String policeNo, List<Error> errorList) throws IllegalAccessException {
        List<ModelBeanDTO> modelBeanDTOList = new ArrayList<>();
        List<ModelEntity> modelEntityList = null;
        try {
            modelEntityList = modelMapper.findBublicByUserNameAndModelId(policeNo);
        }catch (Exception e){
            e.printStackTrace();
            errorList.add(new Error("sys-db-001","通过警号查询模型信息失败",e.toString()));
        }

        for(ModelEntity modelEntity : modelEntityList){
            if(StringUtils.isEmpty(modelEntity.getModelId())){
               continue;
            }
            ModelBeanDTO modelBeanDTO = new ModelBeanDTO();
            modelBeanDTOList.add(modelBeanDTO);
            setModelDetailInfo(errorList, modelEntity, modelBeanDTO);
        }
        return modelBeanDTOList;
    }

    @Override
    public List<ResultColumnBeanDTO> getTableColumnByTableId(String tableId,List<Error> errorList) throws IllegalAccessException{
        List<ResultColumnEntity> resultColumnEntityList = null;
        try {
            resultColumnEntityList = modelDatasourceFieldMapper.getTableAliasByTableId(tableId);
        }catch (Exception e){
            errorList.add(new Error("sys-db-001","查询表字段出错",e.toString()));
        }
        List<ResultColumnBeanDTO> resultColumnBeanDTOList = new ArrayList<>();
        for(ResultColumnEntity resultColumnEntity : resultColumnEntityList) {
            resultColumnBeanDTOList.add(trans(resultColumnEntity,new ResultColumnBeanDTO()));
        }
        return resultColumnBeanDTOList;
    }

    @Override
    public Integer modelRun(String modelId,List<Error> errorList) throws IOException,IllegalAccessException {
        //进行模拟登陆
        List<Cookie>  cookies = httpClientServiceImpl.login();

        //查看当前模型的状态
        ModelBeanDTO modelBeanDTO = getModelInfosByUserNameAndModelId(null,modelId,errorList);
        if(!CollectionUtils.isEmpty(modelBeanDTO.getModelDatasourceBeanDTOList())) {
            for (ModelDatasourceBeanDTO modelDatasourceBeanDTO : modelBeanDTO.getModelDatasourceBeanDTOList()) {
                  if("1".equals(modelDatasourceBeanDTO.getStatus())){
                      errorList.add(new Error("sugon-00-001","模型正在执行，请稍后",String.valueOf(modelDatasourceBeanDTO.getId()) ));
                  }
            }
            if(!CollectionUtils.isEmpty(errorList)){
                return 0;
            }
        }

        String url = jmhost+bashUrl+"/vmp/model/executeByModelId.do";
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("modelId",modelId);

        String response =  httpClientServiceImpl.post(url,paramMap,cookies);
        Map maps = (Map) JSON.parse(response);
        if((boolean)maps.get("result")){
            return 1;
        }
        else{
            errorList.add(new Error("sugon-00-001","模型执行失败",""));
            return 0;
        }
    }

    private void setModelDetailInfo(List<Error> errorList, ModelEntity modelEntity, ModelBeanDTO modelBeanDTO) throws IllegalAccessException {
        trans(modelEntity,modelBeanDTO);
        List<ModelDatasourceBeanDTO> modelDatasourceBeanDTOList = new ArrayList<>();
        List<ModelDatasourceEntity> modelDatasourceEntityList = null;
        try {
            modelDatasourceEntityList = modelDatasourceMapper.getModelDatasourceByModelId(modelEntity.getModelId());
        }catch (Exception e){
            Error error = new Error("sys-db-001","查询表t_model_datasource出错",e.toString());
            errorList.add(error);
        }
        for(ModelDatasourceEntity modelDatasourceEntity : modelDatasourceEntityList){
            ModelDatasourceBeanDTO modelDatasourceBeanDTO =new ModelDatasourceBeanDTO();
            trans(modelDatasourceEntity,modelDatasourceBeanDTO);

            //获取表字段信息
            List<ResultColumnEntity> ResultColumnEntityList= null;
            try {
                ResultColumnEntityList = modelDatasourceFieldMapper.getTableAliasByTableId(String.valueOf(modelDatasourceBeanDTO.getId()) );
            }catch (Exception e){
                errorList.add(new Error("sys-db-001","查询表字段出错",e.toString()));
            }
            List<ResultColumnBeanDTO>  resultColumnBeanDTOList = new ArrayList<>();
            for(ResultColumnEntity resultColumnEntity : ResultColumnEntityList){
                resultColumnBeanDTOList.add(trans(resultColumnEntity,new ResultColumnBeanDTO()));
            }
            ResultTableBeanDTO resultTableBeanDTO = new ResultTableBeanDTO();
            resultTableBeanDTO.setTableID(modelDatasourceBeanDTO.getId());
            resultTableBeanDTO.setTableName(modelDatasourceBeanDTO.getTableName());
            resultTableBeanDTO.setNote(modelDatasourceBeanDTO.getName());
            resultTableBeanDTO.setResultColumns(resultColumnBeanDTOList);
            modelDatasourceBeanDTO.setResultTableBeanDTO(resultTableBeanDTO);

            modelDatasourceBeanDTOList.add(modelDatasourceBeanDTO);
        }
        modelBeanDTO.setModelDatasourceBeanDTOList(modelDatasourceBeanDTOList);

        UsersBeanDTO usersBeanDTO = new UsersBeanDTO();
        JmUserEntity createUser = null;
        try {
            createUser = modelManagerShareMapper.findCreateUser(modelBeanDTO.getModelId());
        }catch (Exception e){
            errorList.add(new Error("sys-db-001","查询创建者出错",e.toString()));
        }
        JmUserEntity privateUser = null;
        try {
             privateUser = modelManagerShareMapper.findPrivateUser(modelBeanDTO.getModelId());
        }catch (Exception e){
            errorList.add(new Error("sys-db-001","查询私有所有者出错",e.toString()));
        }
        List<JmUserEntity> publicUserEntityList = null;
        //如果
        try {
            publicUserEntityList = modelManagerShareMapper.findPublicUserList(modelBeanDTO.getModelId());
        }catch (Exception e){
            errorList.add(new Error("sys-db-001","查询共享者出错",e.toString()));
        }
        if(null != createUser) {
           usersBeanDTO.setCreateUser(trans(createUser, new JmUserBeanDTO()));
       }
        if(null != privateUser) {
            usersBeanDTO.setPrivateUser(trans(privateUser, new JmUserBeanDTO()));
        }
        JmUserBeanDTO publicUser = null;
        List<JmUserBeanDTO> publicUserList = new ArrayList<>();
        for(JmUserEntity jmUserEntity : publicUserEntityList){
            if(null != jmUserEntity) {
                publicUserList.add(trans(jmUserEntity, new JmUserBeanDTO()));
            }
        }
        usersBeanDTO.setPublicUserList(publicUserList);
        modelBeanDTO.setUsersBeanDTO(usersBeanDTO);
    }




}
