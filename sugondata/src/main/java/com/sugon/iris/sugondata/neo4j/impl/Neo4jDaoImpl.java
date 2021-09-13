package com.sugon.iris.sugondata.neo4j.impl;

import com.sugon.iris.sugondata.neo4j.intf.Neo4jDao;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.neo4j.driver.Record;
import org.neo4j.driver.Result;
import org.neo4j.driver.Session;
import org.springframework.stereotype.Service;
import static org.neo4j.driver.Values.parameters;
import javax.annotation.Resource;
import java.util.Map;


@Slf4j
@Service
public class Neo4jDaoImpl implements Neo4jDao {

    @Resource
    private Session noe4jSession;

    @Resource
    private Session session;

    public void addNode( String label,String property) {
        try {
            session.run("CREATE ("+label+"{"+property+"})");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     *
     * @param sourceTableName         源标签名称
     * @param targetTableName         目标标签名称
     * @param relationship            关系名称
     * @param relationshipAttribute   关系属性
     * @param sourceFiled             源节点属性名称
     * @param targetFiled             目标点属性名称
     * @param sourceValue             源查找关系值
     * @param targetValue             目标查找关系值
     */
    public Map<String, Object> addRelationBatch(String sourceTableName,
                                 String targetTableName,
                                 String relationship,
                                 String relationshipAttribute,
                                 String sourceFiled,
                                 String targetFiled,
                                 String sourceValue,
                                 String targetValue,
                                 String relationId) {
        Result result= null;
        Map<String, Object> map = null;
        try {
            result = session.run("MATCH (a:"+sourceTableName+"), (b:"+targetTableName+") " +
                    "WHERE a."+sourceFiled+" = \""+sourceValue+"\" AND b."+targetFiled+" = \""+targetValue+"\" " +
                    "CREATE (a)-[r: "+relationship+"]->(b)  SET r={different:\""+relationshipAttribute+"\",relationId:\""+relationId+"\"} RETURN count(r) AS count");
        } catch (Exception e) {
            e.printStackTrace();
        }
        while (result.hasNext()){
            Record record = result.next();
            map = record.asMap();
        }
        return map;
    }


    public Map<String, Object> addRelationById( String sourceLable,
                                 String targetLable,
                                 String sourceId,
                                 String targetId,
                                 String relationship,
                                 String relationshipAttribute,
                                 String relationId) {
        Result result= null;
        Map<String, Object> map = null;
        try {
            result =  session.run("MATCH (a:"+sourceLable+"{id:$from}),(b:"+targetLable+"{id:$to}) " +
                            "CREATE (a)-[r:" + relationship + "]->(b) SET r={different:\""+relationshipAttribute+"\",relationId:\""+relationId+"\"} RETURN count(r) AS count",
                    parameters("from", sourceId, "to", targetId));
        } catch (Exception e) {
            e.printStackTrace();
        }
        while (result.hasNext()){
            Record record = result.next();
            map = record.asMap();
        }
        return map;
    }

    public Map<String, Object> getRelations(
                                   String relationship,
                                   String relationshipAttribute,
                                   String relationId) {
        Result result= null;
        Map<String, Object> map = null;
        String cql = "MATCH p=()-[r:"+relationship;
        if(StringUtils.isNotEmpty(relationId) && StringUtils.isEmpty(relationshipAttribute)){
            cql += "{relationId:\""+relationId+"\"}";
        }else if(StringUtils.isEmpty(relationId) && StringUtils.isNotEmpty(relationshipAttribute)){
            cql += "{different:\""+relationshipAttribute+"\"}";
        }else if(StringUtils.isNotEmpty(relationId) && StringUtils.isNotEmpty(relationshipAttribute)){
            cql += "{relationId:\""+relationId+"\",different:\""+relationshipAttribute+"\"}";
        }
        cql += "]->() RETURN p;";
        try {
            result =  session.run(cql);
        } catch (Exception e) {
            e.printStackTrace();
        }
        while (result.hasNext()){
            Record record = result.next();
            map = record.asMap();
        }
        return map;
    }
}
