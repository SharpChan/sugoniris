package com.sugon.iris.sugonservice.impl.neo4jServiceImpl;

import com.sugon.iris.sugoncommon.publicUtils.PublicUtils;
import com.sugon.iris.sugondata.mybaties.mapper.db2.*;
import com.sugon.iris.sugondomain.beans.baseBeans.Error;
import com.sugon.iris.sugondomain.dtos.fileDtos.FileCaseDto;
import com.sugon.iris.sugondomain.dtos.fileDtos.FileTableDto;
import com.sugon.iris.sugondomain.dtos.neo4jDtos.Neo4jNodeAttributeDto;
import com.sugon.iris.sugondomain.dtos.neo4jDtos.Neo4jNodeInfoDto;
import com.sugon.iris.sugondomain.dtos.neo4jDtos.Neo4jRelationDto;
import com.sugon.iris.sugondomain.entities.mybatiesEntity.db2.*;
import com.sugon.iris.sugonservice.service.neo4jService.Neo4jModelOneService;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class Neo4jModelOneServiceImpl implements Neo4jModelOneService {

    @Resource
    private FileCaseMapper fileCaseMapper;

    @Resource
    private FileTableMapper fileTableMapper;

    @Resource
    private Neo4jNodeInfoMapper neo4jNodeInfoMapper;

    @Resource
    private Neo4jNodeAttributeMapper neo4jNodeAttributeMapper;

    @Resource
    private Neo4jRelationMapper neo4jRelationMapper;

    @Override
    public List<FileCaseDto> getAllNeo4jInfos(Long userId, List<Error> errorList) throws IllegalAccessException {

        //通过用户编号获取用户下的所有的案件
        FileCaseEntity fileCaseEntity4Sql = new FileCaseEntity();
        fileCaseEntity4Sql.setUserId(userId);
        List<FileCaseEntity> fileCaseEntityList =  fileCaseMapper.selectFileCaseEntityList(fileCaseEntity4Sql);
        List<FileCaseDto> fileCaseDtoList = new ArrayList<>();
        for(FileCaseEntity fileCaseEntity : fileCaseEntityList){
            FileCaseDto fileCaseDto = new FileCaseDto();
            PublicUtils.trans(fileCaseEntity,fileCaseDto);
            fileCaseDtoList.add(fileCaseDto);

            //通过案件编号获取表信息
            FileTableEntity fileTableEntity4Sql = new FileTableEntity();
            fileTableEntity4Sql.setCaseId(fileCaseDto.getId());
            List<FileTableDto> fileTableDtoList = new ArrayList<>();
            fileCaseDto.setFileTableDtoList(fileTableDtoList);
            List<FileTableEntity> fileTableEntityList = fileTableMapper.findFileTableList(fileTableEntity4Sql);
            for(FileTableEntity FileTableEntity : fileTableEntityList){
                FileTableDto fileTableDto = new FileTableDto();
                PublicUtils.trans(FileTableEntity,fileTableDto);
                fileTableDtoList.add(fileTableDto);
                Neo4jNodeInfoEntity neo4jNodeInfoEntity4Sql = new Neo4jNodeInfoEntity();
                neo4jNodeInfoEntity4Sql.setFileTableId(fileTableDto.getId());
                Neo4jNodeInfoEntity neo4jNodeInfoEntity = neo4jNodeInfoMapper.getNeo4jNodeInfo(neo4jNodeInfoEntity4Sql);
                Neo4jNodeInfoDto neo4jNodeInfoDto = new Neo4jNodeInfoDto();
                PublicUtils.trans(neo4jNodeInfoEntity,neo4jNodeInfoDto);
                if(StringUtils.isNotEmpty(neo4jNodeInfoDto.getLabel())) {
                    neo4jNodeInfoDto.setLabelList(Arrays.asList(neo4jNodeInfoDto.getLabel().split("\\.")));
                }
                fileTableDto.setNeo4jNodeInfoDto(neo4jNodeInfoDto);
                Neo4jNodeAttributeEntity neo4jNodeAttributeEntity4Sql = new Neo4jNodeAttributeEntity();
                neo4jNodeAttributeEntity4Sql.setFileTableId(fileTableDto.getId());
                List<Neo4jNodeAttributeDto> neo4jNodeAttributeDtoList = new ArrayList<>();
                neo4jNodeInfoDto.setNeo4jNodeAttributeDtoList(neo4jNodeAttributeDtoList);
                List<Neo4jNodeAttributeEntity> neo4jNodeAttributeEntityList = neo4jNodeAttributeMapper.getNeo4jNodeAttributeLis(neo4jNodeAttributeEntity4Sql);
                for(Neo4jNodeAttributeEntity neo4jNodeAttributeEntity : neo4jNodeAttributeEntityList){
                    Neo4jNodeAttributeDto neo4jNodeAttributeDto = new Neo4jNodeAttributeDto();
                    PublicUtils.trans(neo4jNodeAttributeEntity,neo4jNodeAttributeDto);
                    neo4jNodeAttributeDtoList.add(neo4jNodeAttributeDto);
                    //获取含有该节点的关系
                    Neo4jRelationEntity neo4jRelationEntity4Sql_01 = new Neo4jRelationEntity();
                    neo4jRelationEntity4Sql_01.setSourceAttributeId(neo4jNodeAttributeDto.getId());
                    List<Neo4jRelationDto> neo4jSourceRelationDtoList = new ArrayList<>();
                    neo4jNodeAttributeDto.setNeo4jSourceRelationDtoList(neo4jSourceRelationDtoList);
                    List<Neo4jRelationEntity> neo4jSourceRelationEntity = neo4jRelationMapper.findRelations(neo4jRelationEntity4Sql_01);
                    for(Neo4jRelationEntity neo4jRelationEntity_01 : neo4jSourceRelationEntity){
                        Neo4jRelationDto neo4jRelationDto = new Neo4jRelationDto();
                        PublicUtils.trans(neo4jRelationEntity_01,neo4jRelationDto);
                        neo4jSourceRelationDtoList.add(neo4jRelationDto);
                    }

                    List<Neo4jRelationDto> neo4jTargetRelationDtoList = new ArrayList<>();
                    neo4jNodeAttributeDto.setNeo4jTargetRelationDtoList(neo4jTargetRelationDtoList);
                    Neo4jRelationEntity neo4jRelationEntity4Sql_02 = new Neo4jRelationEntity();
                    neo4jRelationEntity4Sql_02.setTargetAttributeId(neo4jNodeAttributeDto.getId());
                    List<Neo4jRelationEntity> neo4jTargetRelationEntity = neo4jRelationMapper.findRelations(neo4jRelationEntity4Sql_02);
                    for(Neo4jRelationEntity neo4jRelationEntity_02 : neo4jTargetRelationEntity){
                        Neo4jRelationDto neo4jRelationDto = new Neo4jRelationDto();
                        PublicUtils.trans(neo4jRelationEntity_02,neo4jRelationDto);
                        neo4jTargetRelationDtoList.add(neo4jRelationDto);
                    }
                }
            }
        }
        return fileCaseDtoList;
    }


}
