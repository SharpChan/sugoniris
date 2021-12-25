package com.sugon.iris.sugonweb.resolver;

import com.sugon.iris.sugonannotation.annotation.system.BussLog;
import com.sugon.iris.sugondata.mybaties.mapper.db2.SysBusinessLogMapper;
import com.sugon.iris.sugondomain.beans.baseBeans.RestResult;
import com.sugon.iris.sugondomain.beans.system.User;
import com.sugon.iris.sugondomain.entities.mybatiesEntity.db2.BusinessLogEntity;
import com.sugon.iris.sugondomain.enums.BusinessLog_Enum;
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
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.*;

@Aspect
@Component
@Lazy(false)
public class BussLogAspect {

    @Resource
    private SysBusinessLogMapper sysBusinessLogMapper;

    @Resource
    private HttpClientService httpClientServiceImpl;

    @Around(value = "@annotation(com.sugon.iris.sugonannotation.annotation.system.BussLog) && @annotation(bussLog)")
    public RestResult<Object> begin(ProceedingJoinPoint pdj, BussLog bussLog) throws Throwable {
        Object[] args = pdj.getArgs();

        try {
        ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request =servletRequestAttributes.getRequest();
        HttpSession session =request.getSession();
        User obj = (User)session.getAttribute("user");
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
        businessLog.setBusiness( bsEnum.getName());
        if(obj != null && ! (null == obj.getId())) {
            sysBusinessLogMapper.saveBusinessLog(businessLog);



        }
        }catch (Exception e){
            e.printStackTrace();
        }
        RestResult<Object> restResult = (RestResult<Object>) pdj.proceed(args);
        return restResult;
    }

    private void systemLogsForPost(User obj,String ip,BusinessLogEntity businessLog,BusinessLog_Enum bsEnum) throws IOException {

        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        String url = "http://10.35.142.136:8000/api/rzsj/acceptLogs";
        Map<String, Object> paramMap = new HashMap<>();
        Aspt aspt = new Aspt();
        List<Log> logs = new ArrayList<>();
        aspt.setLogs(logs);
        Log log = new Log();
        logs.add(log);
        log.setRzid("");
        log.setXtid("");
        log.setXtmc("经济犯罪多维分析系统");
        log.setMkid(bsEnum.getId());
        log.setMkmc(bsEnum.getName());
        log.setYhzh(obj.getUserName());
        log.setYhxm("");
        log.setYhjh(obj.getPoliceNo());
        log.setYhsfzh(obj.getIdCard());
        log.setYhdwdm("");
        log.setYhdwmc("");
        log.setZddz("");
        log.setCzlx("1");
        log.setCzti("");
        log.setCzsj(df.format(businessLog.getAccessTime()));
        log.setCzjg("");
        log.setCznr("");
        log.setSbyy("");
        log.setYwxtrzid("");

        XStream xStream = new XStream(new XppDriver(new XmlFriendlyNameCoder("_-", "_")));//解决下划线问题
        xStream.autodetectAnnotations(true); //自动检测注解
        xStream.processAnnotations(Aspt.class); //应用Bean类的注解
        String toXML = xStream.toXML(aspt);
        paramMap.put("xtid","320500");
        paramMap.put("logsXml",toXML);
        httpClientServiceImpl.postOtherSystem(url,paramMap);
    }

}
