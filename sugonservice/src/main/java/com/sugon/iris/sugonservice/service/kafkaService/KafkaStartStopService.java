package com.sugon.iris.sugonservice.service.kafkaService;

public interface KafkaStartStopService {

    void startListener(String listenerId,String status);

    void stopListener(String listenerId);
}
