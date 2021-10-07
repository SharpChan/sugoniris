package com.sugon.iris.sugonfilerest;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.web.servlet.ServletComponentScan;

@ServletComponentScan
@SpringBootApplication(exclude = { DataSourceAutoConfiguration.class},
        scanBasePackages = {"com.sugon.iris.sugonservice",
                "com.sugon.iris.sugondata",
                "com.sugon.iris.sugonannotation",
                "com.sugon.iris.sugoncommon",
                "com.sugon.iris.sugonlistener",
                "com.sugon.iris.sugonfilerest"})
public class SugonfilerestApplication {

    public static void main(String[] args) {
        SpringApplication.run(SugonfilerestApplication.class, args);
    }

}
