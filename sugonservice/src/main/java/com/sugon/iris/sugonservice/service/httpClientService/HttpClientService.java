package com.sugon.iris.sugonservice.service.httpClientService;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.cookie.Cookie;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public interface HttpClientService {

    List<Cookie> login() throws IOException;

    String post(String url, Map<String, Object> paramMap,List<Cookie> cookies) throws ClientProtocolException, IOException;

    String postForList(String url, Map<String, Object> paramMap,List<Cookie> cookies) throws ClientProtocolException, IOException;

    String postOtherSystem(String url, Map<String, Object> paramMap) throws ClientProtocolException, IOException;

    String postOtherSystemBody(String url,String paramJsonStr) throws ClientProtocolException, IOException;

    String doGet(String url,Map<String, String> paramMap) throws IOException;
}
