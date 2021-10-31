package com.sugon.iris.sugoncommon.ExecuteConfig;

import com.sugon.iris.sugoncommon.publicUtils.PublicUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;

@Slf4j
@Configuration
public class ExecutorConfig {

    private int corePoolSize;

    private int maxPoolSize;

    private int keepAliveSeconds;

    private int queueCapacity;

    private String threadNamePrefix;


    public   ExecutorConfig(){
        this.corePoolSize = PublicUtils.getConfigMap().get("corePoolSize") == null ? 5 : Integer.parseInt(PublicUtils.getConfigMap().get("corePoolSize"));

        this.maxPoolSize = PublicUtils.getConfigMap().get("maxPoolSize") == null ? 50 : Integer.parseInt(PublicUtils.getConfigMap().get("maxPoolSize"));

        this.keepAliveSeconds =PublicUtils.getConfigMap().get("keepAliveSeconds") == null ? 300: Integer.parseInt(PublicUtils.getConfigMap().get("keepAliveSeconds"));

        this.queueCapacity =PublicUtils.getConfigMap().get("queueCapacity") == null ? 999 : Integer.parseInt(PublicUtils.getConfigMap().get("queueCapacity"));

        this.threadNamePrefix = PublicUtils.getConfigMap().get("threadNamePrefix") == null ? "Grape-": PublicUtils.getConfigMap().get("threadNamePrefix");
}

    @Bean
    public Executor asyncExcelServiceExecutor() {
        log.info("...ExecutorConfig...asyncServiceExecutor()...启动[zip任务]线程池...");
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(corePoolSize);
        executor.setMaxPoolSize(maxPoolSize);
        executor.setThreadNamePrefix(threadNamePrefix);
        executor.setKeepAliveSeconds(keepAliveSeconds);
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        executor.initialize();
        return executor;
    }
}
