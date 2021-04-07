package com.sugon.iris.sugonweb;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.annotation.PropertySource;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;

@EnableRedisHttpSession
@ServletComponentScan
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class}, scanBasePackages = {"com.sugon.iris.sugonservice"})
@MapperScan(basePackages = {"com.sugon.iris.sugondata.mybaties.mapper"})
public class SugonwebApplication {

    public static void main(String[] args) {
        SpringApplication.run(SugonwebApplication.class, args);
    }


}
