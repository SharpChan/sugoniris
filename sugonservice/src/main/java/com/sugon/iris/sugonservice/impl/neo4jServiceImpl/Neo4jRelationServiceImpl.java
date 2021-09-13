package com.sugon.iris.sugonservice.impl.neo4jServiceImpl;

import com.alibaba.fastjson.JSONArray;
import com.sugon.iris.sugoncommon.publicUtils.PublicUtils;
import com.sugon.iris.sugondata.mybaties.mapper.db2.*;
import com.sugon.iris.sugondata.neo4j.intf.Neo4jDao;
import com.sugon.iris.sugondomain.beans.baseBeans.Error;
import com.sugon.iris.sugondomain.beans.system.User;
import com.sugon.iris.sugondomain.beans.webSocket.WebSocketRequest;
import com.sugon.iris.sugondomain.dtos.configDtos.SysDictionaryDto;
import com.sugon.iris.sugondomain.dtos.fileDtos.FileTableDto;
import com.sugon.iris.sugondomain.dtos.fileDtos.FileTemplateDetailDto;
import com.sugon.iris.sugondomain.dtos.fileDtos.FileTemplateDto;
import com.sugon.iris.sugondomain.dtos.neo4jDtos.Neo4jRelationDto;
import com.sugon.iris.sugondomain.dtos.systemDtos.MenuDto;
import com.sugon.iris.sugondomain.entities.mybatiesEntity.db2.*;
import com.sugon.iris.sugondomain.enums.ErrorCode_Enum;
import com.sugon.iris.sugonservice.impl.websocketServiceImpl.WebSocketClient;
import com.sugon.iris.sugonservice.service.configService.SysDictionaryService;
import com.sugon.iris.sugonservice.service.neo4jService.Neo4jRelationService;
import org.springframework.stereotype.Service;
import javax.websocket.ContainerProvider;
import javax.websocket.WebSocketContainer;
import java.net.URI;
import javax.annotation.Resource;
import java.util.*;

@Service
public class Neo4jRelationServiceImpl implements Neo4jRelationService {

    @Resource
    private FileTableMapper fileTableMapper;

    @Resource
    private FileCaseMapper fileCaseMapper;

    @Resource
    private Neo4jNodeAttributeMapper neo4jNodeAttributeMapper;

    @Resource
    private Neo4jRelationMapper neo4jRelationMapper;

    @Resource
    private SysDictionaryService sysDictionaryServiceImpl;

    @Resource
    private FileTemplateMapper fileTemplateMapper;

    @Resource
    private FileTemplateDetailMapper fileTemplateDetailMapper;

    @Resource
    private Neo4jDao neo4jDaoImpl;


    @Override
    public List<MenuDto> findNeo4jNodeAttributeByUserId(User user, List<Error> errorList) throws IllegalAccessException {
        //获取该用户的所有的数据表
        FileTableEntity fileTableEntity = new FileTableEntity();
        fileTableEntity.setUserId(user.getId());
        List<FileTableEntity> fileTableEntityList = fileTableMapper.findFileTableList(fileTableEntity);

        //3.获取该用户下所有的案件
        FileCaseEntity fileCaseEntity = new FileCaseEntity();
        fileCaseEntity.setUserId(user.getId());
        List<FileCaseEntity> fileCaseEntityList = fileCaseMapper.selectFileCaseEntityList(fileCaseEntity);
        List<MenuDto> menuDtoList = new ArrayList<>();
        for(FileCaseEntity fileCaseEntityBean : fileCaseEntityList){
            MenuDto menuDtoCase = new MenuDto();
            menuDtoList.add(menuDtoCase);
            menuDtoCase.setName(fileCaseEntityBean.getCaseName());
            List<MenuDto> submenu = new ArrayList<>();
            menuDtoCase.setSubmenu(submenu);
            for(FileTableEntity fileTableEntityBean : fileTableEntityList){
                if(fileTableEntityBean.getCaseId().equals(fileCaseEntityBean.getId())){
                    MenuDto menuDtoTable = new MenuDto();
                    submenu.add(menuDtoTable);
                    menuDtoTable.setName(fileTableEntityBean.getTableName()+"["+fileTableEntityBean.getTitle().substring(fileTableEntityBean.getTitle().indexOf("_")+1)+"]");
                    menuDtoTable.setId(fileTableEntityBean.getId());
                    List<MenuDto> submenuAttributeList = new ArrayList<>();
                    menuDtoTable.setSubmenu(submenuAttributeList);
                    Neo4jNodeAttributeEntity neo4jNodeAttributeEntity4Sql = new Neo4jNodeAttributeEntity();
                    neo4jNodeAttributeEntity4Sql.setFileTableId(fileTableEntityBean.getId());
                    List<Neo4jNodeAttributeEntity> neo4jNodeAttributeEntityList = neo4jNodeAttributeMapper.getNeo4jNodeAttributeLis(neo4jNodeAttributeEntity4Sql);
                    for(Neo4jNodeAttributeEntity neo4jNodeAttributeEntity : neo4jNodeAttributeEntityList){
                        MenuDto menuAttributeDto = new MenuDto();
                        menuAttributeDto.setName(neo4jNodeAttributeEntity.getAttributeName());
                        menuAttributeDto.setId(neo4jNodeAttributeEntity.getId());
                        submenuAttributeList.add(menuAttributeDto);
                    }
;                }
            }
        }
        return menuDtoList;
    }

    @Override
    public Integer saveRelation(User user, Neo4jRelationDto neo4jRelationDto, List<Error> errorList) throws Exception {
        Neo4jRelationEntity record = new Neo4jRelationEntity();
        PublicUtils.trans(neo4jRelationDto,record);
        record.setUserId(user.getId());
        return neo4jRelationMapper.insert(record);
    }

    @Override
    public List<Neo4jRelationDto> getRelations(User user, List<Error> errorList) throws IllegalAccessException {
        List<Neo4jRelationDto> neo4jRelationDtoList = new ArrayList<>();
        List<Neo4jRelationEntity> neo4jRelationEntityList = null;
        try {
             neo4jRelationEntityList = neo4jRelationMapper.selectByUserId(user.getId());
        }catch (Exception e){

        }

        String dicGroup = "neo4j_relation_color";
        Map<String, String> map = new HashMap<>();
        List<SysDictionaryDto> sysDictionaryDtoList =  sysDictionaryServiceImpl.getSysDictionariesByDicGroup(dicGroup,errorList);
        for(SysDictionaryDto sysDictionaryDto : sysDictionaryDtoList) {
            map.put(sysDictionaryDto.getValue(),sysDictionaryDto.getDicShow());
        }

        dicGroup = "neo4j_relation_shape";
        sysDictionaryDtoList =  sysDictionaryServiceImpl.getSysDictionariesByDicGroup(dicGroup,errorList);
        for(SysDictionaryDto sysDictionaryDto : sysDictionaryDtoList) {
            map.put(sysDictionaryDto.getValue(),sysDictionaryDto.getDicShow());
        }

        for(Neo4jRelationEntity neo4jRelationEntity : neo4jRelationEntityList){
            //通过节点样式id获取样式名称
            Neo4jNodeAttributeEntity neo4jdNoeSourceAttributeEntity = neo4jNodeAttributeMapper.selectByPrimaryKey(neo4jRelationEntity.getSourceAttributeId());
            Neo4jNodeAttributeEntity neo4jdNoeTargetAttributeEntity = neo4jNodeAttributeMapper.selectByPrimaryKey(neo4jRelationEntity.getTargetAttributeId());
            Neo4jRelationDto neo4jRelationDto = new Neo4jRelationDto();
            PublicUtils.trans(neo4jRelationEntity,neo4jRelationDto);
            neo4jRelationDto.setSourceAttributeName(neo4jdNoeSourceAttributeEntity.getAttributeName());
            neo4jRelationDto.setTargetAttributeName(neo4jdNoeSourceAttributeEntity.getAttributeName());
            neo4jRelationDto.setColor(map.get(neo4jRelationDto.getColor()));
            neo4jRelationDto.setShape(map.get(neo4jRelationDto.getShape()));
            neo4jRelationDtoList.add(neo4jRelationDto);
        }
        return neo4jRelationDtoList;
    }

    @Override
    public Integer initRelation(User user, Neo4jRelationDto neo4jRelationDto, List<Error> errorList) throws IllegalAccessException {
        Integer result = 1;
        WebSocketContainer container = ContainerProvider.getWebSocketContainer();
        WebSocketClient client = new WebSocketClient();
        String URI = neo4jRelationDto.getProgram()+"/"+user.getId();
        try {
            container.connectToServer(client, new URI(URI));
        }catch (Exception e){
            e.printStackTrace();
            errorList.add(new Error(ErrorCode_Enum.SUGON_02_009.getCode(),ErrorCode_Enum.SUGON_02_009.getMessage(),e.toString()));
        }

        WebSocketRequest webSocketRequest = new WebSocketRequest();
        webSocketRequest.setNeo4jRelationDto(neo4jRelationDto);
        //获取源数据的表信息
        FileTableEntity sourceFileTableEntity = fileTableMapper.findFileTableByRelationAttributeId(neo4jRelationDto.getSourceAttributeId());
        //获取目标数据的表信息
        FileTableEntity targetFileTableEntity = fileTableMapper.findFileTableByRelationAttributeId(neo4jRelationDto.getTargetAttributeId());

        FileTableDto sourceFileTableDto = new FileTableDto();
        PublicUtils.trans(sourceFileTableEntity,sourceFileTableDto);

        FileTableDto targetFileTableDto = new FileTableDto();
        PublicUtils.trans(targetFileTableEntity,targetFileTableDto);

        FileTemplateDto sourceFileTemplateDto = getFileTemplateDto(sourceFileTableEntity);
        FileTemplateDto targetFileTemplateDto = getFileTemplateDto(targetFileTableEntity);
        sourceFileTableDto.setFileTemplateDto(sourceFileTemplateDto);
        targetFileTableDto.setFileTemplateDto(targetFileTemplateDto);

        webSocketRequest.setSourceFileTableDto(sourceFileTableDto);
        webSocketRequest.setTargetFileTableDto(targetFileTableDto);

        webSocketRequest.setUserId(neo4jRelationDto.getUserId());
        Object obj = JSONArray.toJSON(webSocketRequest);
        String json = obj.toString();
        client.send(json);
        return result;
    }

    @Override
    public Map<?, ?> getNeo4jRelations(String relationship, String relationshipAttribute, String relationId, List<Error> errorList) {

        relationship ="www";
        relationshipAttribute = "hhh";
        relationId = "1";
        neo4jDaoImpl.getRelations(relationship,relationshipAttribute,relationId);
        return null;
    }

    private FileTemplateDto getFileTemplateDto(FileTableEntity sourceFileTableEntity) throws IllegalAccessException {
        //获取源模板
        FileTemplateEntity fileTemplateEntity4Sql = new FileTemplateEntity();
        fileTemplateEntity4Sql.setId(sourceFileTableEntity.getFileTemplateId());
        FileTemplateEntity fileTemplateEntity =  fileTemplateMapper.selectFileTemplateList(fileTemplateEntity4Sql).get(0);
        FileTemplateDto fileTemplateDto = new FileTemplateDto();
        PublicUtils.trans(fileTemplateEntity,fileTemplateDto);
        //获取源模板字段
        FileTemplateDetailEntity fileTemplateDetailEntity4Sql = new FileTemplateDetailEntity();
        fileTemplateDetailEntity4Sql.setTemplateId(sourceFileTableEntity.getFileTemplateId());
        List<FileTemplateDetailEntity> fileTemplateDetailEntityList = fileTemplateDetailMapper.selectFileTemplateDetailList(fileTemplateDetailEntity4Sql);
        List<FileTemplateDetailDto> fileTemplateDetailDtoList = new ArrayList<>();
        for(FileTemplateDetailEntity fileTemplateDetailEntity : fileTemplateDetailEntityList){
            FileTemplateDetailDto fileTemplateDetailDto = new FileTemplateDetailDto();
            PublicUtils.trans(fileTemplateDetailEntity,fileTemplateDetailDto);
            fileTemplateDetailDtoList.add(fileTemplateDetailDto);
        }
        fileTemplateDto.setFileTemplateDetailDtoList(fileTemplateDetailDtoList);
        return fileTemplateDto;
    }


}
