package com.sugon.iris.sugonweb;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;

@EnableRedisHttpSession
@ServletComponentScan
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class},
                       scanBasePackages = {"com.sugon.iris.sugonservice",
                                           "com.sugon.iris.sugondata",
                                           "com.sugon.iris.sugonannotation",
                                           "com.sugon.iris.sugonweb",
                                           "com.sugon.iris.sugoncommon",
                                           "com.sugon.iris.sugonrest"})
public class SugonwebApplication {
    public static void main(String[] args) {
        SpringApplication.run(SugonwebApplication.class, args);
    }
}
