package com.sugon.iris.sugonservice.impl.kafkaServiceImpl;

import com.sugon.iris.sugonservice.service.kafkaService.KafkaStartStopService;
import org.springframework.kafka.config.KafkaListenerEndpointRegistry;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class KafkaStartStopServiceImpl implements KafkaStartStopService {

    @Resource
    private KafkaListenerEndpointRegistry registry;

    public void startListener(String listenerId,String status) {

        //判断监听容器是否启动，未启动则将其启动
        if (!registry.getListenerContainer(listenerId).isRunning()) {

            registry.getListenerContainer(listenerId).start();

        }

        //项目启动的时候监听容器是未启动状态，而resume是恢复的意思不是启动的意思
        if(!"1".equals(status)) {
            registry.getListenerContainer(listenerId).resume();
        }
    }


    public void stopListener(String listenerId) {
        //如果是关闭的直接退出
        if(!registry.getListenerContainer(listenerId).isRunning()){
            return;
        }
        //停止接收下发
        registry.getListenerContainer(listenerId).stop();
    }
}
