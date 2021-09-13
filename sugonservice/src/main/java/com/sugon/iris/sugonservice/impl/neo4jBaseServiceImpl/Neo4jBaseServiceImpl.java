package com.sugon.iris.sugonservice.impl.neo4jBaseServiceImpl;

import com.sugon.iris.sugondata.neo4j.intf.Neo4jDao;
import com.sugon.iris.sugondomain.beans.baseBeans.Error;
import com.sugon.iris.sugondomain.enums.ErrorCode_Enum;
import com.sugon.iris.sugonservice.service.neo4jBaseService.Neo4jBaseService;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import java.util.List;

@Service
public class Neo4jBaseServiceImpl implements Neo4jBaseService {

    @Resource
    private Neo4jDao neo4jDaoImpl;

    @Override
    public int addRelationBatch(String sourceTableName,
                                String targetTableName,
                                String relationship,
                                String relationshipAttribute,
                                String sourceFiled,
                                String targetFiled,
                                String sourceValue,
                                String targetValue,
                                String relationId,
                                List<Error> errorList) {
        int i = 0;
        try {
            i = (int) neo4jDaoImpl.addRelationBatch(sourceTableName, targetTableName, relationship, relationshipAttribute, sourceFiled, targetFiled, sourceValue, targetValue, relationId).get("count");
        }catch (Exception e){
            e.printStackTrace();
            errorList.add(new Error(ErrorCode_Enum.SYS_NEO4J_001.getCode(), ErrorCode_Enum.SYS_NEO4J_001.getMessage()));
        }
        return  i;
    }

    @Override
    public int addRelationById(String sourceLable,
                               String targetLable,
                               String sourceId,
                               String targetId,
                               String relationship,
                               String relationshipAttribute,
                               String relationId,
                               List<Error> errorList) {

        int i = 0;
        try {
            i = (int)neo4jDaoImpl.addRelationById(sourceLable,targetLable,sourceId,targetId,relationship,relationshipAttribute,relationId).get("count");
        }catch (Exception e){
            e.printStackTrace();
            errorList.add(new Error(ErrorCode_Enum.SYS_NEO4J_001.getCode(), ErrorCode_Enum.SYS_NEO4J_001.getMessage()));
        }
        return  i;
    }
}
