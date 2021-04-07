package com.sugon.iris.sugonservice.impl.httpServiceImpl;

import com.sugon.iris.sugonservice.service.httpClientService.HttpClientService;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.cookie.Cookie;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import java.io.*;
import java.util.*;



@Service
public class HttpClientServiceImpl implements HttpClientService {

    @Value("${sdm.jmhost}")
    private String jmhost;

    @Value("${sdm.bashUrl}")
    private String bashUrl;

    @Value("${sdm.username}")
    private String userName;

    @Value("${sdm.password}")
    private String password;

    private final static String LOGINBEFORE_URL = "/loginbefore.do";

    private final static String LOGINFORJM_URL = "/model/loginForJM.do";


    /**
     * 作者:SharpChan
     * 日期：2020.06.03
     * 描述：进行模拟登录，并获取登录后的cookie
     */
    public List<Cookie> login(String jmHost) throws IOException {
        String url = jmHost+bashUrl+LOGINBEFORE_URL;
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("accountno",userName);
        paramMap.put("identity","1");
        paramMap.put("password",password);
        List<Cookie> cookie01 =new ArrayList<Cookie>();
        String  str = post(url,paramMap,cookie01);

        String url2 = jmHost+bashUrl+LOGINFORJM_URL;
        Map<String, Object> paramMap2 = new HashMap<>();
        paramMap2.put("accountno",userName);
        paramMap2.put("password",password);
        String bb = post(url2,paramMap2,cookie01);
        return cookie01;
    }

    /**
     * 作者:SharpChan
     * 日期：2020.06.03
     * 描述：发送post请求
     */
    public  String post(String url, Map<String, Object> paramMap,List<Cookie> cookies) throws ClientProtocolException, IOException {
        BasicCookieStore cookieStore = new BasicCookieStore();
        CloseableHttpClient httpClient = HttpClients.custom().setDefaultCookieStore(cookieStore).build();
        HttpPost httpPost = new HttpPost(url);
        if(!CollectionUtils.isEmpty(paramMap)) {
            List<NameValuePair> formparams = setHttpParams(paramMap);
            UrlEncodedFormEntity param = new UrlEncodedFormEntity(formparams, "UTF-8");
            httpPost.setEntity(param);
        }
        if(!CollectionUtils.isEmpty(cookies)){
            httpPost.setHeader("Cookie","JSESSIONID="+cookies.get(0).getValue());
        }
        HttpResponse response = httpClient.execute(httpPost);
        String httpEntityContent = getHttpEntityContent(response);
        httpPost.abort();
        if(!CollectionUtils.isEmpty(cookieStore.getCookies())) {
            cookies.addAll(cookieStore.getCookies());
        }
        return httpEntityContent;
    }

    public  String postForList(String url, Map<String, Object> paramMap,List<Cookie> cookies) throws ClientProtocolException, IOException {
        BasicCookieStore cookieStore = new BasicCookieStore();
        CloseableHttpClient httpClient = HttpClients.custom().setDefaultCookieStore(cookieStore).build();
        HttpPost httpPost = new HttpPost(url);
        if(!CollectionUtils.isEmpty(paramMap)) {
            List<NameValuePair> formparams = setHttpParams(paramMap);
            UrlEncodedFormEntity param = new UrlEncodedFormEntity(formparams, "UTF-8");
            httpPost.setEntity(param);
        }
        if(!CollectionUtils.isEmpty(cookies)){
            httpPost.setHeader("Cookie","JSESSIONID="+cookies.get(0).getValue());
        }
        HttpResponse response = httpClient.execute(httpPost);
        String httpEntityContent = getHttpEntityContent(response);
        httpPost.abort();
        if(!CollectionUtils.isEmpty(cookieStore.getCookies())) {
            cookies.addAll(cookieStore.getCookies());
        }
        return httpEntityContent;
    }

    /**
     * 作者:SharpChan
     * 日期：2020.06.17
     * 描述：发送post请求,到其他系统
     */
    public  String postOtherSystem(String url, Map<String, Object> paramMap) throws ClientProtocolException, IOException {
        BasicCookieStore cookieStore = new BasicCookieStore();
        CloseableHttpClient httpClient = HttpClients.custom().setDefaultCookieStore(cookieStore).build();

        HttpPost httpPost = new HttpPost(url);
        List<NameValuePair> formparams = setHttpParams(paramMap);
        UrlEncodedFormEntity param = new UrlEncodedFormEntity(formparams, "UTF-8");
        httpPost.setEntity(param);
        HttpResponse response = httpClient.execute(httpPost);

        String httpEntityContent = getHttpEntityContent(response);
        httpPost.abort();
        return httpEntityContent;
    }

    public  String postOtherSystemBody(String url,String paramJsonStr) throws ClientProtocolException, IOException {
        BasicCookieStore cookieStore = new BasicCookieStore();
        CloseableHttpClient httpClient = HttpClients.custom().setDefaultCookieStore(cookieStore).build();

        HttpPost httpPost = new HttpPost(url);
        httpPost.setEntity(new StringEntity(paramJsonStr));
        HttpResponse response = httpClient.execute(httpPost);

        String httpEntityContent = getHttpEntityContent(response);
        httpPost.abort();
        return httpEntityContent;
    }

    /**
     * 作者:SharpChan
     * 日期：2020.06.03
     * 描述：组装请求参数
     */
    private  List<NameValuePair>  setHttpParams(Map<String, Object> paramMap) {
        List<NameValuePair> formparams = new ArrayList<NameValuePair>();
        Set<Map.Entry<String, Object>> set = paramMap.entrySet();
        for (Map.Entry<String, Object> entry : set) {
            if(entry.getValue() instanceof String) {
                formparams.add(new BasicNameValuePair(entry.getKey(), (String) entry.getValue()));
            }else if(entry.getValue() instanceof  List){
                for(Object str : (List)entry.getValue()){
                    formparams.add(new BasicNameValuePair(entry.getKey(), (String) str));
                }
            }
        }
        return formparams;
    }

    /**
     * 作者:SharpChan
     * 日期：2020.06.03
     * 描述：获取请求的返回值
     */
    private  String getHttpEntityContent(HttpResponse response) throws IOException, UnsupportedEncodingException {
        HttpEntity entity = response.getEntity();
        if (entity != null) {
            InputStream is = entity.getContent();
            BufferedReader br = new BufferedReader(new InputStreamReader(is, "UTF-8"));
            String line = br.readLine();
            StringBuilder sb = new StringBuilder();
            while (line != null) {
                sb.append(line + "\n");
                line = br.readLine();
            }
            return sb.toString();
        }
        return "";
    }

    /**
     * 作者:SharpChan
     * 日期：2020.09.09
     * 描述：发送get请求
     */
    public String doGet(String url,Map<String, String> paramMap) throws IOException {

        if(!CollectionUtils.isEmpty(paramMap)){
            url = url+"?";
            for(Map.Entry<String, String> entry:paramMap.entrySet()){
                url = url + entry.getKey()+"="+entry.getValue()+"&";
            }
        }

        HttpGet httpget = new HttpGet(url);
        RequestConfig requestConfig = RequestConfig.custom().setConnectTimeout(50000).setSocketTimeout(50000)
                .setConnectionRequestTimeout(3000).build();

        httpget.setConfig(requestConfig);

        CloseableHttpClient httpclient = HttpClients.createDefault();
        CloseableHttpResponse response = httpclient.execute(httpget);

        if (response != null) {
            HttpEntity entity = response.getEntity(); // 获取返回实体
            if (entity != null) {
                return  EntityUtils.toString(entity);
            }
        }
        return "";
    }
}
