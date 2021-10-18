package com.sugon.iris.sugondata.neo4j.impl;

import com.sugon.iris.sugondomain.beans.neo4jBeans.Elements;
import com.sugon.iris.sugondomain.beans.neo4jBeans.Neo4jRelationshipBean;
import org.neo4j.driver.types.Node;
import org.neo4j.driver.types.Relationship;
import com.sugon.iris.sugondata.neo4j.intf.Neo4jDao;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.neo4j.driver.Record;
import org.neo4j.driver.Result;
import org.neo4j.driver.Session;
import org.neo4j.driver.internal.InternalPath;
import org.springframework.stereotype.Service;
import static org.neo4j.driver.Values.parameters;
import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;


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
     * @param sourceFiled             源节点属性名称
     * @param targetFiled             目标点属性名称
     * @param sourceValue             源查找关系值
     * @param targetValue             目标查找关系值
     */
    @Override
    public Map<String, Object> addRelationBatch(String sourceTableName,
                                 String targetTableName,
                                 String relationship,
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
                    "CREATE (a)-[r: "+relationship+"]->(b)  SET r={relationId:\""+relationId+"\"} RETURN count(r) AS count");
        } catch (Exception e) {
            e.printStackTrace();
        }
        while (result.hasNext()){
            Record record = result.next();
            map = record.asMap();
        }
        return map;
    }

    @Override
    public Map<String, Object> addRelationById( String sourceLable,
                                 String targetLable,
                                 String sourceId,
                                 String targetId,
                                 String relationship,
                                 String relationId) {
        Result result= null;
        Map<String, Object> map = null;
        try {
            result =  session.run("MATCH (a:"+sourceLable+"{id:$from}),(b:"+targetLable+"{id:$to}) " +
                            "CREATE (a)-[r:" + relationship + "]->(b) SET r={relationId:\""+relationId+"\"} RETURN count(r) AS count",
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

    @Override
    public Elements getRelations(
                                   String relationship,
                                   String relationId,
                                   Map<String,String> map) {
        Result result= null;
        Elements elements =new Elements();
        Map<String,Map> mapNode = new HashMap<>();
        String cql = "MATCH p=()-[r:"+relationship;
        if(StringUtils.isNotEmpty(relationId)){
            cql += "{relationId:'"+relationId+"'}";
        }
        cql += "]->() RETURN p;";
        try {
            result =  session.run(cql);
        } catch (Exception e) {
            e.printStackTrace();
        }
        Set<Neo4jRelationshipBean> set = new HashSet<>();
        List<Map<String ,Map>> nodes = new ArrayList<>();
        List<Map<String ,Map>> edges = new ArrayList<>();
        elements.setNodes(nodes);
        elements.setEdges(edges);
        while (result.hasNext()){
            Record record = result.next();
            List<InternalPath> paths = record.asMap().values().stream().map(d -> (InternalPath) d)
                    .collect(Collectors.toList());
            for (InternalPath path : paths) {
                int i =0;
                for (Node nodeBean : path.nodes()) {
                    Long id = nodeBean.id();
                    Map<String,Object> map1 = nodeBean.asMap();
                    Map<String,Map> map2 = new HashMap<>();
                    Map<String,String> map3 = new HashMap<>();
                    for(String key : map1.keySet()){
                        map3.put(key,(String) map1.get(key));
                        if(key.equals(map.get("sourceNodeContent"))){
                            map3.put("sourceNodeContent",(String) map1.get(key));
                            map3.put("targetNodeContent",(String) map1.get(key));
                        }
                    }
                    if(i == 0){
                        map3.put("type","source");
                        map3.put("sourceNodeWidth",map.get("sourceNodeWidth"));
                        map3.put("sourceNodeColor",map.get("sourceNodeColor"));
                        map3.put("sourceNodeHeight",map.get("sourceNodeHeight"));
                        map3.put("sourceNodeShape",map.get("sourceNodeShape"));
                    }else if(i == 1){
                        map3.put("type","target");
                        map3.put("targetNodeWidth",map.get("targetNodeWidth"));
                        map3.put("targetNodeHeight",map.get("targetNodeHeight"));
                        map3.put("targetNodeColor",map.get("targetNodeColor"));
                        map3.put("targetNodeShape",map.get("targetNodeShape"));
                    }
                    map3.put("id",String.valueOf(id));
                    map2.put("data",map3);
                    nodes.add(map2);
                    i++;
                }
                for (Relationship relationshipBean : path.relationships()) {
                    Neo4jRelationshipBean neo4jRelationshipBean = new Neo4jRelationshipBean();
                    neo4jRelationshipBean.setId(String.valueOf(relationshipBean.id()));
                    neo4jRelationshipBean.setEndNodeId(relationshipBean.endNodeId()+"");
                    neo4jRelationshipBean.setStartNodeId(relationshipBean.startNodeId()+"");
                    neo4jRelationshipBean.setRelationId(Integer.parseInt(relationId)+"");
                    neo4jRelationshipBean.setRelationship(relationship);
                    neo4jRelationshipBean.setColor(map.get("relationShipColor"));
                    neo4jRelationshipBean.setShape(map.get("relationShipShape"));
                    set.add(neo4jRelationshipBean);
                }
            }
        }

        for(Neo4jRelationshipBean neo4jRelationshipBean : set){
            Map<String,Map> map4 = new HashMap<>();
            edges.add(map4);
            Map<String,String> map5 = new HashMap<>();
            map4.put("data",map5);
            map5.put("id",neo4jRelationshipBean.getId());
            map5.put("source",String.valueOf(neo4jRelationshipBean.getStartNodeId()));
            map5.put("target",String.valueOf(neo4jRelationshipBean.getEndNodeId()));
            map5.put("relationship",neo4jRelationshipBean.getRelationship());

        }

        return elements;
    }
}
