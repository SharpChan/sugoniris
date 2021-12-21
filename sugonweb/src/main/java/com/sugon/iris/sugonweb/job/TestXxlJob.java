package com.sugon.iris.sugonweb.job;

import com.xxl.job.core.context.XxlJobHelper;
import com.xxl.job.core.handler.annotation.XxlJob;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class TestXxlJob {
    private static Logger logger = LoggerFactory.getLogger(TestXxlJob.class);

    //@XxlJob("testJobHandler")
    public void demoJobHandler() throws Exception {

        XxlJobHelper.log("XXL-JOB, Hello World.");
        logger.info("aaa");

    }
}
