package com.sugon.iris.sugonservice.impl.neo4jServiceImpl;

import com.alibaba.fastjson.JSON;
import com.sugon.iris.sugoncommon.publicUtils.PublicUtils;
import com.sugon.iris.sugondata.mybaties.mapper.db2.FileTableMapper;
import com.sugon.iris.sugondata.mybaties.mapper.db2.FileTemplateDetailMapper;
import com.sugon.iris.sugondata.mybaties.mapper.db2.Neo4jNodeInfoMapper;
import com.sugon.iris.sugondata.mybaties.mapper.db4.MppMapper;
import com.sugon.iris.sugondata.neo4j.intf.Neo4jDao;
import com.sugon.iris.sugondomain.beans.baseBeans.Error;
import com.sugon.iris.sugondomain.beans.system.User;
import com.sugon.iris.sugondomain.dtos.fileDtos.FileTableDto;
import com.sugon.iris.sugondomain.entities.mybatiesEntity.db2.FileTableEntity;
import com.sugon.iris.sugondomain.entities.mybatiesEntity.db2.FileTemplateDetailEntity;
import com.sugon.iris.sugondomain.entities.mybatiesEntity.db2.Neo4jNodeInfoEntity;
import com.sugon.iris.sugondomain.enums.ErrorCode_Enum;
import com.sugon.iris.sugonservice.service.neo4jService.Neo4jInitDatService;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import javax.annotation.Resource;
import java.util.*;

@Service
public class Neo4jInitDatServiceImpl  implements Neo4jInitDatService {

    private static final String MESSAGE_01 = "[查询表file_table出错]";

    private static final String MESSAGE_02 = "[不存在需要初始的数据]";

    @Resource
    private FileTableMapper fileTableMapper;

    @Resource
    private FileTemplateDetailMapper fileTemplateDetailMapper;

    @Resource
    private Neo4jDao neo4jDaoImpl;

    @Resource
    private MppMapper mppMapper;

    @Resource
    private Neo4jNodeInfoMapper neo4jNodeInfoMapper;


    @Override
    public List<FileTableDto> getAllFileTableByUserid(Long userId,List<Error> errorList) throws IllegalAccessException {
        List<FileTableDto> fileTableDtoList = new ArrayList<>();
        List<FileTableEntity> fileTableEntityList = null;
        try {
            fileTableEntityList = fileTableMapper.findFileTablesWithCaseNameAndTemplateNameByUserId(userId);
        }catch (Exception e){
            e.printStackTrace();
            Error error = new Error(ErrorCode_Enum.SYS_DB_001.getCode(),ErrorCode_Enum.SYS_DB_001.getMessage()+MESSAGE_01,e.toString());
            errorList.add(error);
        }
        if(CollectionUtils.isEmpty(fileTableEntityList)){
           return  fileTableDtoList;
        }
        for(FileTableEntity fileTableEntity : fileTableEntityList){
            FileTableDto fileTableDto = new FileTableDto();
            PublicUtils.trans(fileTableEntity,fileTableDto);
            fileTableDtoList.add(fileTableDto);
        }
        return fileTableDtoList;
    }

    @Override
    public Integer initData(User user,FileTableDto fileTableDto, List<Error> errorList) throws IllegalAccessException {
        int result = 0;
        //获取数据初始状态
        FileTableEntity fileTableEntity = new FileTableEntity();
        PublicUtils.trans(fileTableDto,fileTableEntity);
        List<FileTableEntity> fileTableEntityList = fileTableMapper.findFileTableList(fileTableEntity);
        if(CollectionUtils.isEmpty(fileTableEntityList) || !"0".equals(fileTableEntityList.get(0).getNeo4jInitFlag())){
            errorList.add(new Error(ErrorCode_Enum.SUGON_01_007.getCode(),ErrorCode_Enum.SUGON_01_007.getMessage()+MESSAGE_02));
        }
        //通过模板id获取模板字段信息
        FileTemplateDetailEntity fileTemplateDetailEntity4Sql = new FileTemplateDetailEntity();
        fileTemplateDetailEntity4Sql.setTemplateId(fileTableDto.getFileTemplateId());
        List<FileTemplateDetailEntity> fileTemplateDetailEntityList = fileTemplateDetailMapper.selectFileTemplateDetailList(fileTemplateDetailEntity4Sql);
        //排序
        fileTemplateDetailEntityListSort(fileTemplateDetailEntityList);
        FileTemplateDetailEntity fileTemplateDetailEntity = new FileTemplateDetailEntity();
        fileTemplateDetailEntity.setFieldName("file_attachment_id");
        fileTemplateDetailEntityList.add(fileTemplateDetailEntity);
        //组装cql  label
        String label = "a:"+fileTableDto.getCaseName()+":"+fileTableDto.getTemplateName();
        //组装property
        String sql = "select result,id from (select id, concat('{',";
        for(int i = 0 ;i<fileTemplateDetailEntityList.size();i++){
            sql += "'\"','"+fileTemplateDetailEntityList.get(i).getFieldName()+"','\":','\"',a."+fileTemplateDetailEntityList.get(i).getFieldName()+",'\"'";
            if(i+1 != fileTemplateDetailEntityList.size()){
                sql += ",',',";
            }
        }
        sql += ",',','\"','file_attachment_id','\":','\"',a.file_attachment_id,'\"','}') as result from "+fileTableDto.getTableName()+" a ) b";
        //获取mpp数据库数据表总数量
        String sqlCount = "select max(id) from "+fileTableDto.getTableName();
        List<String> strListCount = mppMapper.mppSqlExecForSearch(sqlCount);
        Integer count = Integer.parseInt(strListCount.get(0));
        Integer batch = Integer.parseInt(PublicUtils.getConfigMap().get("neo4j_batch_quantity"));
        int divideCount = count /batch;
        if(divideCount == 0){
            result +=  neo4jSave(label,sql,fileTemplateDetailEntityList);
        }else{
            for(int i = 0 ;i<divideCount;i++){
                String sql4Neo1 =sql + " where b.id >="+(i*batch+1) +" and b.id <"+((i+1)*batch+1);
                result +=neo4jSave(label,sql4Neo1,fileTemplateDetailEntityList);
            }
            //其余部分数据
            String sql4Neo2 =sql + " where b.id >="+(divideCount*batch+1) +" and b.id <"+count+1;
            result +=neo4jSave(label,sql4Neo2,fileTemplateDetailEntityList);
        }
        fileTableEntity.setNeo4jInitFlag("2");
        fileTableMapper.updateFileTable(fileTableEntity);
        Neo4jNodeInfoEntity neo4jNodeInfoEntity4Sql = new Neo4jNodeInfoEntity();
        neo4jNodeInfoEntity4Sql.setRownum(count.longValue());
        neo4jNodeInfoEntity4Sql.setFileTableId(fileTableEntity.getId());
        neo4jNodeInfoEntity4Sql.setUserId(user.getId());
        neo4jNodeInfoMapper.saveNeo4jNodeInfo(neo4jNodeInfoEntity4Sql);
        return result;
    }

    private int neo4jSave(String label,String sql,List<FileTemplateDetailEntity> fileTemplateDetailEntityList){
        List<String> strList = mppMapper.mppSqlExecForSearch(sql);
        for(String str : strList){
            String property = "";
            Map map = (Map) JSON.parse(str);
            for(int i = 0 ;i<fileTemplateDetailEntityList.size();i++){
                property += fileTemplateDetailEntityList.get(i).getFieldName()+":'"+map.get(fileTemplateDetailEntityList.get(i).getFieldName())+"'";
                if(i+1 != fileTemplateDetailEntityList.size()){
                    property += ",";
                }
            }
            neo4jDaoImpl.addStar(label,property);
        }

        return  strList.size();
    }



    //给模板字段排序
    private void fileTemplateDetailEntityListSort(List<FileTemplateDetailEntity> fileTemplateDetailEntityList) {
        //用排序字段对字段列表进行排序
        Collections.sort(fileTemplateDetailEntityList, new Comparator<FileTemplateDetailEntity>() {
            @Override
            public int compare(FileTemplateDetailEntity bean1, FileTemplateDetailEntity bean2) {
                int diff = Integer.parseInt(bean1.getSortNo()) - Integer.parseInt(bean2.getSortNo());
                if (diff > 0) {
                    return 1;
                }else if (diff < 0) {
                    return -1;
                }
                return 0; //相等为0
            }
        });
    }
}
