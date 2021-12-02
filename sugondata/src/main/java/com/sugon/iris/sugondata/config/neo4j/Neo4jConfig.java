package com.sugon.iris.sugondata.config.neo4j;


import com.sugon.iris.sugoncommon.publicUtils.PublicUtils;
import org.neo4j.ogm.session.SessionFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.neo4j.repository.config.EnableNeo4jRepositories;
import org.springframework.data.neo4j.transaction.Neo4jTransactionManager;

@Configuration
@EnableNeo4jRepositories("com.sugon.iris.sugondata.neo4j") // 声明neo4j repository存放地址
public class Neo4jConfig {

    private String uri;
    private String userName;
    private String password;

    public Neo4jConfig(){
        this.uri = PublicUtils.getConfigMap().get("neo4j_url");
        this.userName = PublicUtils.getConfigMap().get("neo4j_username");
        this.password = PublicUtils.getConfigMap().get("password");
    }

    @Bean
    public org.neo4j.ogm.config.Configuration getConfiguration() {
        org.neo4j.ogm.config.Configuration configuration = new org.neo4j.ogm.config.Configuration.Builder().uri(uri).connectionPoolSize(100).credentials(userName, password).withBasePackages("com.xm").build();
        return configuration;
    }

    @Bean
    public SessionFactory sessionFactory() {
        return new SessionFactory(getConfiguration());
    }

    @Bean("transactionManager")
    public Neo4jTransactionManager neo4jTransactionManager(SessionFactory sessionFactory) {
        return new Neo4jTransactionManager(sessionFactory);
    }
}
