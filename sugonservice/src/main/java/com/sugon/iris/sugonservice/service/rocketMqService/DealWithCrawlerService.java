package com.sugon.iris.sugonservice.service.rocketMqService;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.util.concurrent.ExecutionException;

public interface DealWithCrawlerService {
    void  dealWithBankData( String message) throws IOException, NoSuchAlgorithmException, InterruptedException, ExecutionException, IllegalAccessException, ParseException;
}
