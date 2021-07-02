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
@MapperScan(basePackages = "com.sugon.iris.sugondata.mybaties.mapper.db4", sqlSessionTemplateRef = "db4SqlSessionTemplate")
public class DataSource4Config {

    @Value("${datasource.druid.db4.jdbc-url}")
    private String url;

    @Value("${datasource.druid.db4.username}")
    private String user;

    @Value("${datasource.druid.db4.password}")
    private String password;

    @Value("${datasource.druid.db4.driverClassName}")
    private String driverClass;

    @Value("${datasource.druid.db4.filters}")
    private String filters;

    @Value("${datasource.druid.db4.initialSize}")
    private int initialSize;

    @Value("${datasource.druid.db4.minIdle}")
    private int minIdle;

    @Value("${datasource.druid.db4.maxActive}")
    private int maxActive;

    @Value("${datasource.druid.db4.validationQuery}")
    private String validationQuery;

    @Value("${datasource.druid.db4.testWhileIdle}")
    private boolean testWhileIdle;

    @Value("${datasource.druid.db4.testOnBorrow}")
    private boolean testOnBorrow;

    @Value("${datasource.druid.db4.testOnReturn}")
    private boolean testOnReturn;

    @Value("${datasource.druid.db4.maxWait}")
    private long maxWait;

    @Bean
    public DataSource db4DataSource() {

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
    public SqlSessionFactory db4SqlSessionFactory(@Qualifier("db4DataSource") DataSource dataSource) throws Exception {
        SqlSessionFactoryBean bean = new SqlSessionFactoryBean();
        bean.setDataSource(dataSource);
        bean.setMapperLocations(new PathMatchingResourcePatternResolver().getResources("classpath*:com/sugon/iris/sugondata/mybaties/mapper/db4xml/*.xml"));
        return bean.getObject();
    }

    @Bean
    public DataSourceTransactionManager db4TransactionManager(@Qualifier("db4DataSource") DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }

    @Bean
    public SqlSessionTemplate db4SqlSessionTemplate(@Qualifier("db4SqlSessionFactory") SqlSessionFactory sqlSessionFactory) throws Exception {
        return new SqlSessionTemplate(sqlSessionFactory);
    }
}