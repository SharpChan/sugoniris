package com.sugon.iris.sugondata.config.neo4j;

import com.sugon.iris.sugoncommon.publicUtils.PublicUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.neo4j.driver.*;

@Configuration

public class neo4jSession {

    private String url;

    private String userName;

    private String password;

    public neo4jSession(){
        this.url = PublicUtils.getConfigMap().get("neo4j_url");
        this.userName = PublicUtils.getConfigMap().get("neo4j_username");
        this.password = PublicUtils.getConfigMap().get("password");
    }

    @Bean(name = "noe4jSession")
    public Session getNoe4jSession(){
        Driver  driver = GraphDatabase.driver(url, AuthTokens.basic(userName, password));
        return driver.session();
    }

}
