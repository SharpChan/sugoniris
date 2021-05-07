/**
 * 作者：SharpChan
 * 创建日期2021.04.02
 * 修改日志：
 */
package com.sugon.iris.sugonservice.impl.modelServiceImpl;

import com.alibaba.fastjson.JSON;
import com.sugon.iris.sugondata.mybaties.mapper.db1.*;
import com.sugon.iris.sugondomain.beans.baseBeans.Error;
import com.sugon.iris.sugondomain.dtos.sdmModelInfosDtos.ModelBeanDTO;
import com.sugon.iris.sugondomain.entities.mybatiesEntity.db1.JmUserEntity;
import com.sugon.iris.sugondomain.entities.mybatiesEntity.db1.ModelDatasourceEntity;
import com.sugon.iris.sugondomain.entities.mybatiesEntity.db1.ModelEntity;
import com.sugon.iris.sugondomain.entities.mybatiesEntity.db1.ResultColumnEntity;
import com.sugon.iris.sugondomain.enums.ErrorCode_Enum;
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
    private SdmUserMapper sdmUserMapper;

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

    private static final String ZERO="0";

    private static final String ONE="1";

    private static final String  EXECUTEBYMODELID = "/vmp/model/executeByModelId.do";

    private static final String MESSAGE_01 = "[t_model_manager]";

    private static final String MESSAGE_02 = "[通过警号和模型Id查询模型信息失败]";

    private static final String MESSAGE_03 = "[通过警号查询私有模型信息失败]";

    private static final String MESSAGE_04 ="[通过警号查询公有模型信息失败]";

    private static final String MESSAGE_05 = "[查询表字段出错]";

    private static final String MESSAGE_06 = "[模型正在执行，请稍后]";

    private static final String MESSAGE_07 =  "[模型执行失败]";

    private static final String MESSAGE_08 = "[查询表t_model_datasource出错]";

    private static final String MESSAGE_09 = "[查询创建者出错]";

    private static final String MESSAGE_10 = "[查询私有所有者出错]";

    private static final String MESSAGE_11 = "[查询共享者出错]";

    /**
     * 描述：获取所有的模型信息
     * @param errorList：错误列表
     * @return
     * @throws IllegalAccessException
     */
    @Override
    public List<ModelBeanDTO> getAllModelInfos(List<Error> errorList) throws IllegalAccessException{
        List<ModelBeanDTO> ModelBeanDTOList = new ArrayList<>();
        List<ModelEntity> modelEntityList = null;
        try {
            modelEntityList = modelMapper.selectModelList();
        }catch (Exception e){
            Error error = new Error(ErrorCode_Enum.SYS_DB_001.getCode(),ErrorCode_Enum.SYS_DB_001.getMessage()+MESSAGE_01,e.toString());
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

    /**
     * 描述：通过警号和模型id获取模型信息
     * @param policeNo：警号
     * @param ModelId：模型id
     * @param errorList：错误列表
     * @return
     * @throws IllegalAccessException
     */
    @Override
    public ModelBeanDTO getModelInfosByUserNameAndModelId(String policeNo, String ModelId, List<Error> errorList) throws IllegalAccessException {
        ModelBeanDTO modelBeanDTO = new ModelBeanDTO();
        ModelEntity modelEntity = null;
        try {
            modelEntity = modelMapper.findOneByUserNameAndModelId(policeNo, ModelId);
        }catch (Exception e){
            e.printStackTrace();
            errorList.add(new Error(ErrorCode_Enum.SYS_DB_001.getCode(),ErrorCode_Enum.SYS_DB_001.getMessage()+MESSAGE_02,e.toString()));
        }
        setModelDetailInfo(errorList, modelEntity, modelBeanDTO);
        return modelBeanDTO;
    }

    /**
     * 描述：通过警号获取私有的模型信息
     * @param policeNo：警号
     * @param errorList：错误列表
     * @return
     * @throws IllegalAccessException
     */
    @Override
    public List<ModelBeanDTO> getPrivateModelInfosByUserName(String policeNo, List<Error> errorList) throws IllegalAccessException {
        List<ModelBeanDTO> modelBeanDTOList = new ArrayList<>();
        List<ModelEntity> modelEntityList = null;
        try {
            modelEntityList = modelMapper.findPrivateByUserNameAndModelId(policeNo);
        }catch (Exception e){
            e.printStackTrace();
            errorList.add(new Error(ErrorCode_Enum.SYS_DB_001.getCode(),ErrorCode_Enum.SYS_DB_001.getMessage()+MESSAGE_03,e.toString()));
        }

       for(ModelEntity modelEntity : modelEntityList){
           ModelBeanDTO modelBeanDTO = new ModelBeanDTO();
           modelBeanDTOList.add(modelBeanDTO);
           setModelDetailInfo(errorList, modelEntity, modelBeanDTO);
       }
        return modelBeanDTOList;
    }

    /**
     * 描述：通过警号获取公共的模型信息
     * @param policeNo：警号
     * @param errorList：错误列表
     * @return
     * @throws IllegalAccessException
     */
    @Override
    public List<ModelBeanDTO> getPublicModelInfosByUserName(String policeNo, List<Error> errorList) throws IllegalAccessException {
        List<ModelBeanDTO> modelBeanDTOList = new ArrayList<>();
        List<ModelEntity> modelEntityList = null;
        try {
            modelEntityList = modelMapper.findBublicByUserNameAndModelId(policeNo);
        }catch (Exception e){
            e.printStackTrace();
            errorList.add(new Error(ErrorCode_Enum.SYS_DB_001.getCode(),ErrorCode_Enum.SYS_DB_001.getMessage()+MESSAGE_04,e.toString()));
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

    /**
     * 描述：通过表编号获取表字段
     * @param tableId：表编号
     * @param errorList：错误列表
     * @return
     * @throws IllegalAccessException
     */
    @Override
    public List<ResultColumnBeanDTO> getTableColumnByTableId(String tableId,List<Error> errorList) throws IllegalAccessException{
        List<ResultColumnEntity> resultColumnEntityList = null;
        try {
            resultColumnEntityList = modelDatasourceFieldMapper.getTableAliasByTableId(tableId);
        }catch (Exception e){
            errorList.add(new Error(ErrorCode_Enum.SYS_DB_001.getCode(),ErrorCode_Enum.SYS_DB_001.getMessage()+MESSAGE_05,e.toString()));
        }
        List<ResultColumnBeanDTO> resultColumnBeanDTOList = new ArrayList<>();
        for(ResultColumnEntity resultColumnEntity : resultColumnEntityList) {
            resultColumnBeanDTOList.add(trans(resultColumnEntity,new ResultColumnBeanDTO()));
        }
        return resultColumnBeanDTOList;
    }

    /**
     * 描述：通过模型ID执行模型
     * @param modelId：模型编号
     * @param errorList：错误列表
     * @return
     * @throws IOException
     * @throws IllegalAccessException
     */
    @Override
    public String modelRun(String modelId,List<Error> errorList) throws IOException,IllegalAccessException {
        //进行模拟登陆
        List<Cookie>  cookies = httpClientServiceImpl.login();

        //查看当前模型的状态
        ModelBeanDTO modelBeanDTO = getModelInfosByUserNameAndModelId(null,modelId,errorList);
        if(!CollectionUtils.isEmpty(modelBeanDTO.getModelDatasourceBeanDTOList())) {
            for (ModelDatasourceBeanDTO modelDatasourceBeanDTO : modelBeanDTO.getModelDatasourceBeanDTOList()) {
                  if(ONE.equals(modelDatasourceBeanDTO.getStatus())){
                      errorList.add(new Error(ErrorCode_Enum.SYS_SUGON_001.getCode(),ErrorCode_Enum.SUGON_00_001.getMessage()+MESSAGE_06,String.valueOf(modelDatasourceBeanDTO.getId()) ));
                  }
            }
            if(!CollectionUtils.isEmpty(errorList)){
                return ZERO;
            }
        }

        String url = jmhost+bashUrl+EXECUTEBYMODELID;
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("modelId",modelId);

        String response =  httpClientServiceImpl.post(url,paramMap,cookies);
        Map maps = (Map) JSON.parse(response);
        if((boolean)maps.get("result")){
            return ONE;
        }
        else{
            errorList.add(new Error(ErrorCode_Enum.SYS_SUGON_001.getCode(),ErrorCode_Enum.SYS_DB_001.getMessage()+MESSAGE_07,""));
            return ZERO;
        }
    }

    /**
     * 描述：把modelEntity转换为ModelBeanDTO，并且查询出表字段信息、创建者信息、私有者信息、公共所有者信息
     * @param errorList：错误列表
     * @param modelEntity：实体对象
     * @param modelBeanDTO：DTO对象
     * @throws IllegalAccessException
     */
    private void setModelDetailInfo(List<Error> errorList, ModelEntity modelEntity, ModelBeanDTO modelBeanDTO) throws IllegalAccessException {
        trans(modelEntity,modelBeanDTO);
        List<ModelDatasourceBeanDTO> modelDatasourceBeanDTOList = new ArrayList<>();
        List<ModelDatasourceEntity> modelDatasourceEntityList = null;
        try {
            modelDatasourceEntityList = modelDatasourceMapper.getModelDatasourceByModelId(modelEntity.getModelId());
        }catch (Exception e){
            Error error = new Error(ErrorCode_Enum.SYS_DB_001.getCode(),ErrorCode_Enum.SYS_DB_001.getMessage()+MESSAGE_08,e.toString());
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
                errorList.add(new Error(ErrorCode_Enum.SYS_DB_001.getCode(),ErrorCode_Enum.SYS_DB_001.getMessage()+MESSAGE_05,e.toString()));
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
            errorList.add(new Error(ErrorCode_Enum.SYS_DB_001.getCode(),ErrorCode_Enum.SYS_DB_001.getMessage()+MESSAGE_09,e.toString()));
        }
        JmUserEntity privateUser = null;
        try {
             privateUser = modelManagerShareMapper.findPrivateUser(modelBeanDTO.getModelId());
        }catch (Exception e){
            errorList.add(new Error(ErrorCode_Enum.SYS_DB_001.getCode(),ErrorCode_Enum.SYS_DB_001.getMessage()+MESSAGE_10,e.toString()));
        }
        List<JmUserEntity> publicUserEntityList = null;
        //如果modelType是0则全部共享 是1指定共享
        try {
        if(ZERO.equals(modelBeanDTO.getModelType())){
            publicUserEntityList =sdmUserMapper.selectAllUserList();
        }else if(ONE.equals(modelBeanDTO.getModelType())) {

                publicUserEntityList = modelManagerShareMapper.findPublicUserList(modelBeanDTO.getModelId());
            }
        }catch (Exception e) {
            errorList.add(new Error(ErrorCode_Enum.SYS_DB_001.getCode(), ErrorCode_Enum.SYS_DB_001.getMessage()+MESSAGE_11, e.toString()));
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
