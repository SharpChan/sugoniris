package com.sugon.iris.sugondata.config.mybatisConfig;
import com.alibaba.druid.pool.DruidDataSource;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import javax.sql.DataSource;
import java.sql.SQLException;

@Configuration
@MapperScan(basePackages = "com.sugon.iris.sugondata.mybaties.mapper.db2", sqlSessionTemplateRef = "db2SqlSessionTemplate")
public class DataSource2Config {

    @Value("${datasource.druid.db2.jdbc-url}")
    private String url;

    @Value("${datasource.druid.db2.username}")
    private String user;

    @Value("${datasource.druid.db2.password}")
    private String password;

    @Value("${datasource.druid.db2.driverClassName}")
    private String driverClass;

    @Value("${datasource.druid.db2.filters}")
    private String filters;

    @Value("${datasource.druid.db2.initialSize}")
    private int initialSize;

    @Value("${datasource.druid.db2.minIdle}")
    private int minIdle;

    @Value("${datasource.druid.db2.maxActive}")
    private int maxActive;

    @Value("${datasource.druid.db2.validationQuery}")
    private String validationQuery;

    @Value("${datasource.druid.db2.testWhileIdle}")
    private boolean testWhileIdle;

    @Value("${datasource.druid.db2.testOnBorrow}")
    private boolean testOnBorrow;

    @Value("${datasource.druid.db2.testOnReturn}")
    private boolean testOnReturn;

    @Value("${datasource.druid.db2.maxWait}")
    private long maxWait;

    @Bean
    public DataSource db2DataSource() {

        DruidDataSource dataSource = new DruidDataSource();
        dataSource.setDriverClassName(driverClass);
        dataSource.setUrl(url);
        dataSource.setUsername(user);
        dataSource.setPassword(password);
        dataSource.setInitialSize(initialSize);
        dataSource.setMinIdle(minIdle);
        dataSource.setMaxActive(maxActive);
        dataSource.setValidationQuery(validationQuery);
        dataSource.setTestWhileIdle(testWhileIdle);
        dataSource.setTestOnBorrow(testOnBorrow);
        dataSource.setTestOnReturn(testOnReturn);
        dataSource.setMaxWait(maxWait);
        try {
            dataSource.setFilters(filters);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return dataSource;
    }

    @Bean
    public SqlSessionFactory db2SqlSessionFactory(@Qualifier("db2DataSource") DataSource dataSource) throws Exception {
        SqlSessionFactoryBean bean = new SqlSessionFactoryBean();
        bean.setDataSource(dataSource);
        bean.setMapperLocations(new PathMatchingResourcePatternResolver().getResources("classpath*:com/sugon/iris/sugondata/mybaties/mapper/db2xml/*.xml"));
        return bean.getObject();
    }

    @Bean
    public DataSourceTransactionManager db2TransactionManager(@Qualifier("db2DataSource") DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }

    @Bean
    public SqlSessionTemplate db2SqlSessionTemplate(@Qualifier("db2SqlSessionFactory") SqlSessionFactory sqlSessionFactory) throws Exception {
        return new SqlSessionTemplate(sqlSessionFactory);
    }


}