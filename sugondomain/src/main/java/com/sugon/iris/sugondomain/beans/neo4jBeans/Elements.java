package com.sugon.iris.sugondomain.beans.neo4jBeans;

import lombok.Data;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Data
public class Elements {

   private List<Map<String, Map>> nodes;

    private List<Map<String, Map>> edges;


    public List<Map<String, Map>> getNodes(){
        if(CollectionUtils.isEmpty(nodes)){
            List<Map<String, Map>> list = new ArrayList<>();
            return list;
        }
        return this.nodes;
    }

    public List<Map<String, Map>> getEdges(){
        if(CollectionUtils.isEmpty(edges)){
            List<Map<String, Map>> list = new ArrayList<>();
            return list;
        }
        return this.edges;
    }
}
