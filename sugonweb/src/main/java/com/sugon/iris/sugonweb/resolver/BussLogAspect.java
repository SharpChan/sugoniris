package com.sugon.iris.sugonweb.resolver;

import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSON;
import com.sugon.iris.sugonannotation.annotation.system.BussLog;
import com.sugon.iris.sugoncommon.publicUtils.PublicUtils;
import com.sugon.iris.sugondata.mybaties.mapper.db2.SysBusinessLogMapper;
import com.sugon.iris.sugondomain.beans.baseBeans.Error;
import com.sugon.iris.sugondomain.beans.baseBeans.RestResult;
import com.sugon.iris.sugondomain.beans.system.User;
import com.sugon.iris.sugondomain.entities.mybatiesEntity.db2.BusinessLogEntity;
import com.sugon.iris.sugondomain.enums.BusinessLog_Enum;
import com.sugon.iris.sugondomain.enums.ErrorCode_Enum;
import com.sugon.iris.sugondomain.xstream.systemLog.Aspt;
import com.sugon.iris.sugondomain.xstream.systemLog.Log;
import com.sugon.iris.sugonservice.service.httpClientService.HttpClientService;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.XmlFriendlyNameCoder;
import com.thoughtworks.xstream.io.xml.XppDriver;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import javax.annotation.Resource;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Aspect
@Component
@Lazy(false)
public class BussLogAspect {

    public static ExecutorService executorService = Executors.newFixedThreadPool(1);

    @Resource
    private SysBusinessLogMapper sysBusinessLogMapper;

    @Resource
    private HttpClientService httpClientServiceImpl;

    @Around(value = "@annotation(com.sugon.iris.sugonannotation.annotation.system.BussLog) && @annotation(bussLog)")
    public RestResult<Object> begin(ProceedingJoinPoint pdj, BussLog bussLog) throws Throwable {
        Object[] args = pdj.getArgs();
        ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request =servletRequestAttributes.getRequest();

        String cztj = JSONUtil.toJsonStr(args);
        for(Object obj : args){
            if(obj instanceof ServletRequest || obj instanceof ServletResponse || obj instanceof HttpSession){
               continue;
            }
            cztj += JSON.toJSONString(obj)+"||";
        }
        if(cztj.length()>200){
            cztj = cztj.substring(0,199);
        }

        HttpSession session = null;
        User obj = null;

        try {
            session = request.getSession();
            obj = (User) session.getAttribute("user");
        }catch (Exception e){
            e.printStackTrace();
        }

        RestResult<Object> restResult = (RestResult<Object>) pdj.proceed(args);

        try {
        if(null == obj){
           obj = (User)session.getAttribute("user");
        }

        String url = request.getRequestURL().toString();
        String a1 = url.substring(url.lastIndexOf("/"));
        url = url.replace(a1,"");
        String a2 = url.substring(url.lastIndexOf("/"));
        String a3 = a2+a1;
        String ip = request.getRemoteAddr();
        Timestamp timestamp = new Timestamp(new Date().getTime());
        BusinessLogEntity businessLog = new BusinessLogEntity();
        if(null != obj) {
            businessLog.setUserId(obj.getId());
            businessLog.setUserName(obj.getUserName());
        }
        businessLog.setAccessTime(timestamp);
        businessLog.setIp(ip);
        BusinessLog_Enum bsEnum = BusinessLog_Enum.getEnumByUrl(a3);
        if(obj != null && ! (null == obj.getId()) && null != bsEnum) {
            businessLog.setBusiness( bsEnum.getName());
            final User userFinal = obj;
            final String cztjFinal = cztj;

            //多线程进行日志保存和日志上传
            executorService.execute(new Runnable() {
                 @Override
                 public void run() {
                     sysBusinessLogMapper.saveBusinessLog(businessLog);
                     if(PublicUtils.getConfigMap().get("environment").equals("0")){
                         try {
                             systemLogsForPost( userFinal, businessLog, bsEnum,cztjFinal);
                         }catch (Exception e){
                             if(null != restResult) {
                                 if (CollectionUtils.isEmpty(restResult.getErrorList())) {
                                     List<Error> errorList = new ArrayList<>();
                                     errorList.add(new Error(ErrorCode_Enum.SYS_03_000.getCode(), ErrorCode_Enum.SYS_03_000.getMessage()));
                                 } else {
                                     restResult.getErrorList().add(new Error(ErrorCode_Enum.SYS_03_000.getCode(), ErrorCode_Enum.SYS_03_000.getMessage()));
                                 }
                             }
                         }
                     }
                 }
             });
        }
        }catch (Exception e){
            e.printStackTrace();
        }
        return restResult;
    }

    private void systemLogsForPost(User obj,BusinessLogEntity businessLog,BusinessLog_Enum bsEnum,String cztj) throws Exception {
        SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
        String url = "http://10.35.142.136:8000/api/rzsj/acceptLogs";
        Map<String, Object> paramMap = new HashMap<>();
        Aspt aspt = new Aspt();
        List<Log> logs = new ArrayList<>();
        aspt.setVersion("1.0");
        aspt.setRegID("320500020003");
        aspt.setLogs(logs);
        Log log = new Log();
        logs.add(log);
        log.setXtid("320500020003");
        log.setXtmc("经济犯罪多维分析系统");
        log.setMkid(bsEnum.getId());
        log.setMkmc(bsEnum.getName());
        log.setYhzh(obj.getUserName());
        log.setYhxm(obj.getXm());
        log.setYhjh(obj.getPoliceNo());
        log.setYhsfzh(obj.getIdCard());
        log.setYhdwdm(obj.getXtyhbmbh());
        log.setYhdwmc(obj.getXtyhbmmc());
        log.setZddz(businessLog.getIp());
        log.setCzlx(bsEnum.getCzlx());
        log.setCztj(cztj);
        log.setCzsj(df.format(businessLog.getAccessTime()));
        log.setCzjg("1");
        log.setCznr("");
        log.setSbyy("");
        log.setYwxtrzid("");

        XStream xStream = new XStream(new XppDriver(new XmlFriendlyNameCoder("_-", "_")));//解决下划线问题
        xStream.autodetectAnnotations(true); //自动检测注解
        xStream.processAnnotations(Aspt.class); //应用Bean类的注解
        String toXML = xStream.toXML(aspt);
        toXML = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"+toXML;
        paramMap.put("xtid","320500020003");
        paramMap.put("logsXml",toXML);
        httpClientServiceImpl.postOtherSystem(url,paramMap);
    }
}
