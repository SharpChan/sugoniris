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
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import javax.sql.DataSource;
import java.sql.SQLException;

@Configuration
@MapperScan(basePackages = "com.sugon.iris.sugondata.mybaties.mapper.db1",sqlSessionTemplateRef = "db1SqlSessionTemplate")
public class DataSource1Config {

    @Value("${datasource.druid.db1.username}")
    private String user;

    @Value("${datasource.druid.db1.jdbc-url}")
    private String url;

    @Value("${datasource.druid.db1.password}")
    private String password;

    @Value("${datasource.druid.db1.driverClassName}")
    private String driverClass;

    @Value("${datasource.druid.db1.filters}")
    private String filters;

    @Value("${datasource.druid.db1.initialSize}")
    private int initialSize;

    @Value("${datasource.druid.db1.minIdle}")
    private int minIdle;

    @Value("${datasource.druid.db1.maxActive}")
    private int maxActive;

    @Value("${datasource.druid.db1.validationQuery}")
    private String validationQuery;

    @Value("${datasource.druid.db1.testWhileIdle}")
    private boolean testWhileIdle;

    @Value("${datasource.druid.db1.testOnBorrow}")
    private boolean testOnBorrow;

    @Value("${datasource.druid.db1.testOnReturn}")
    private boolean testOnReturn;

    @Value("${datasource.druid.db1.maxWait}")
    private long maxWait;

    @Bean
    @Primary
    public DataSource db1DataSource() {
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
    @Primary
    public SqlSessionFactory db1SqlSessionFactory(@Qualifier("db1DataSource") DataSource dataSource) throws Exception {
        SqlSessionFactoryBean bean = new SqlSessionFactoryBean();
        bean.setDataSource(dataSource);
        bean.setMapperLocations(new PathMatchingResourcePatternResolver().getResources("classpath*:com/sugon/iris/sugondata/mybaties/mapper/db1xml/*.xml"));
        return bean.getObject();
    }

    @Bean
    @Primary
    public DataSourceTransactionManager db1TransactionManager(@Qualifier("db1DataSource") DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }

    @Bean
    @Primary
    public SqlSessionTemplate db1SqlSessionTemplate(@Qualifier("db1SqlSessionFactory") SqlSessionFactory sqlSessionFactory) throws Exception {
        return new SqlSessionTemplate(sqlSessionFactory);
    }

}
