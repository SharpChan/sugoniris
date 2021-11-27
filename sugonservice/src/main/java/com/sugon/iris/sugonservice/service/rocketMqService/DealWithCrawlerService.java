package com.sugon.iris.sugonservice.service.rocketMqService;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;

public interface DealWithCrawlerService {
    void  dealWithBankData( String message) throws UnsupportedEncodingException, NoSuchAlgorithmException;
}
