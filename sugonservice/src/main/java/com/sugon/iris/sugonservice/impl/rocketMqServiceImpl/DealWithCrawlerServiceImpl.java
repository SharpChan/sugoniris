package com.sugon.iris.sugonservice.impl.rocketMqServiceImpl;

import com.sugon.iris.sugonservice.service.rocketMqService.DealWithCrawlerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class DealWithCrawlerServiceImpl implements DealWithCrawlerService {


    @Override
    public void dealWithBankData(String message) {
        log.info(message);
    }
}
