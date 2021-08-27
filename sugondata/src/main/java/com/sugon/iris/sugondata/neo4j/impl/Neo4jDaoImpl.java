package com.sugon.iris.sugondata.neo4j.impl;

import com.sugon.iris.sugondata.neo4j.intf.Neo4jDao;
import org.neo4j.driver.Session;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class Neo4jDaoImpl implements Neo4jDao {

    @Resource
    private Session noe4jSession;

    @Resource
    private Session session;

    public void addStar( String label,String property) {
        try {
            session.run("CREATE ("+label+"{"+property+"})");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
