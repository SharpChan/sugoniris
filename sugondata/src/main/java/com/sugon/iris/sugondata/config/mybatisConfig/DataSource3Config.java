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
@MapperScan(basePackages = "com.sugon.iris.sugondata.mybaties.mapper.db3", sqlSessionTemplateRef = "db3SqlSessionTemplate")
public class DataSource3Config {

    @Value("${datasource.druid.db3.jdbc-url}")
    private String url;

    @Value("${datasource.druid.db3.username}")
    private String user;

    @Value("${datasource.druid.db3.password}")
    private String password;

    @Value("${datasource.druid.db3.driverClassName}")
    private String driverClass;

    @Value("${datasource.druid.db3.filters}")
    private String filters;

    @Value("${datasource.druid.db3.initialSize}")
    private int initialSize;

    @Value("${datasource.druid.db3.minIdle}")
    private int minIdle;

    @Value("${datasource.druid.db3.maxActive}")
    private int maxActive;

    @Value("${datasource.druid.db3.validationQuery}")
    private String validationQuery;

    @Value("${datasource.druid.db3.testWhileIdle}")
    private boolean testWhileIdle;

    @Value("${datasource.druid.db3.testOnBorrow}")
    private boolean testOnBorrow;

    @Value("${datasource.druid.db3.testOnReturn}")
    private boolean testOnReturn;

    @Value("${datasource.druid.db3.maxWait}")
    private long maxWait;

    @Bean
    public DataSource db3DataSource() {

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
    public SqlSessionFactory db3SqlSessionFactory(@Qualifier("db3DataSource") DataSource dataSource) throws Exception {
        SqlSessionFactoryBean bean = new SqlSessionFactoryBean();
        bean.setDataSource(dataSource);
        bean.setMapperLocations(new PathMatchingResourcePatternResolver().getResources("classpath*:com/sugon/iris/sugondata/mybaties/mapper/db3xml/*.xml"));
        return bean.getObject();
    }

    @Bean
    public DataSourceTransactionManager db3TransactionManager(@Qualifier("db3DataSource") DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }

    @Bean
    public SqlSessionTemplate db3SqlSessionTemplate(@Qualifier("db3SqlSessionFactory") SqlSessionFactory sqlSessionFactory) throws Exception {
        return new SqlSessionTemplate(sqlSessionFactory);
    }
}