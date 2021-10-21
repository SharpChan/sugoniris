package com.sugon.iris.sugonservice.impl.relationServiceImpl;

import com.sugon.iris.sugoncommon.publicUtils.PublicUtils;
import com.sugon.iris.sugondata.mybaties.mapper.db4.MppMapper;
import com.sugon.iris.sugondata.neo4j.intf.Neo4jDao;
import com.sugon.iris.sugondomain.beans.webSocket.WebSocketRequest;
import com.sugon.iris.sugondomain.dtos.fileDtos.FileTableDto;
import com.sugon.iris.sugondomain.dtos.fileDtos.FileTemplateDetailDto;
import com.sugon.iris.sugondomain.dtos.neo4jDtos.Neo4jRelationDto;
import com.sugon.iris.sugondomain.enums.RelationType_Enum;
import com.sugon.iris.sugonservice.impl.websocketServiceImpl.WebSocketRelationServer;
import com.sugon.iris.sugonservice.impl.websocketServiceImpl.WebSocketServer;
import com.sugon.iris.sugonservice.service.relationService.RelationCreateService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import java.io.IOException;
import java.util.List;
import java.util.Map;

@Service
@Qualifier("relationCreateServiceImpl")
public class RelationCreateServiceImpl implements RelationCreateService {

    @Resource
    private MppMapper mppMapper;

    @Resource
    private Neo4jDao neo4jDaoImpl;

    /**
     * @param
     * @return    map(relationid,百分比)
     */
    @Override
    public  void test1(WebSocketRequest webSocketRequest) throws IOException {
        //获取源数据的数目。进行分批数据关系操作。
        Neo4jRelationDto neo4jRelationDto = webSocketRequest.getNeo4jRelationDto();
        String differentiate = neo4jRelationDto.getDifferentiate();
        if(RelationType_Enum.RE_01.getCode().equals(differentiate)){
            test01(webSocketRequest);
        }

    }

    private void test01(WebSocketRequest webSocketRequest) throws IOException {
        //获取源数据总条数
        FileTableDto sourceFileTableDto =  webSocketRequest.getSourceFileTableDto();
        FileTableDto targetFileTableDto =  webSocketRequest.getTargetFileTableDto();
        List<FileTemplateDetailDto> fileTemplateDetailDtoList = sourceFileTableDto.getFileTemplateDto().getFileTemplateDetailDtoList();
        String sql = "Select count(*) from "+sourceFileTableDto.getTableName();
        List<String> strList = mppMapper.mppSqlExecForSearch(sql);
        int count = Integer.parseInt(strList.get(0));
        Integer batch = Integer.parseInt(PublicUtils.getConfigMap().get("neo4j_batch_quantity"));
        int divideCount = count /batch;
        if(divideCount == 0){
            this.batch(0,divideCount,sourceFileTableDto,targetFileTableDto,webSocketRequest.getNeo4jRelationDto().getRelationship(),webSocketRequest.getNeo4jRelationDto().getId()+"");

        }else{
            for(int i = 0 ;i<divideCount;i++){
                this.batch(batch*i,batch*(i+1),sourceFileTableDto,targetFileTableDto,webSocketRequest.getNeo4jRelationDto().getRelationship(),webSocketRequest.getNeo4jRelationDto().getId()+"");
                WebSocketRelationServer.sendInfo(webSocketRequest.getNeo4jRelationDto().getId()+";"+ Math.round(i/(divideCount+1)),String.valueOf(webSocketRequest.getUserId()));
            }
            //其余部分数据
            this.batch(batch*divideCount,count,sourceFileTableDto,targetFileTableDto,webSocketRequest.getNeo4jRelationDto().getRelationship(),webSocketRequest.getNeo4jRelationDto().getId()+"");
        }
        WebSocketRelationServer.sendInfo(webSocketRequest.getNeo4jRelationDto().getId()+";"+"100",String.valueOf(webSocketRequest.getUserId()));
    }

    private void batch(int start ,int end , FileTableDto sourceFileTableDto, FileTableDto targetFileTableDto ,String relationship,String relationId ){

        String sql = "SELECT a.* FROM " +
                     "(SELECT *,ROW_NUMBER() OVER() as rownm FROM "+sourceFileTableDto.getTableName()+") a  WHERE a.rownm>"+start+" and a.rownm<="+end;
        List<Map<String,Object>> sourceLm  = mppMapper.mppSqlExecForSearchRtMapList(sql);

        //获取对应模板字段
        List<FileTemplateDetailDto> sourceFileTemplateDetailDtoList = sourceFileTableDto.getFileTemplateDto().getFileTemplateDetailDtoList();
        for(FileTemplateDetailDto sourceFileTemplateDetailDto : sourceFileTemplateDetailDtoList){
             if(sourceFileTemplateDetailDto.getSortNo().equals("3")){
                  for(Map sourceMap : sourceLm){
                      String targetSql = "Select * from "+targetFileTableDto.getTableName() +" where zjhm = '"+sourceMap.get(sourceFileTemplateDetailDto.getFieldName())+"'";
                      List<Map<String,Object>> targetLm  = mppMapper.mppSqlExecForSearchRtMapList(sql);
                      for(Map targetMap : targetLm){
                             //创建关系
                          neo4jDaoImpl.addRelationBatch( sourceFileTableDto.getTableName(),
                                  targetFileTableDto.getTableName(),
                                  relationship,
                                  "zjhm",
                                  "zjhm",
                                  (String)sourceMap.get(sourceFileTemplateDetailDto.getFieldName()),
                                  (String)sourceMap.get(sourceFileTemplateDetailDto.getFieldName()),
                                  relationId
                                  );
                      }
                  }
             }
        }

    }
}
