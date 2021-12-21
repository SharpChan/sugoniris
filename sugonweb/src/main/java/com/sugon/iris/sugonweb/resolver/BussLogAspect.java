package com.sugon.iris.sugonweb.resolver;

import com.sugon.iris.sugonannotation.annotation.system.BussLog;
import com.sugon.iris.sugondata.mybaties.mapper.db2.SysBusinessLogMapper;
import com.sugon.iris.sugondomain.beans.baseBeans.RestResult;
import com.sugon.iris.sugondomain.beans.system.User;
import com.sugon.iris.sugondomain.entities.mybatiesEntity.db2.BusinessLogEntity;
import com.sugon.iris.sugondomain.enums.BusinessLog_Enum;
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
import java.sql.Timestamp;
import java.util.Date;

@Aspect
@Component
@Lazy(false)
public class BussLogAspect {

    @Resource
    private SysBusinessLogMapper sysBusinessLogMapper;

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
        businessLog.setBusiness( BusinessLog_Enum.getEnumByUrl(a3).getName());
        if(obj != null && ! (null == obj.getId())) {
            sysBusinessLogMapper.saveBusinessLog(businessLog);
        }
        }catch (Exception e){
            e.printStackTrace();
        }
        RestResult<Object> restResult = (RestResult<Object>) pdj.proceed(args);
        return restResult;
    }

}
