package com.sugon.iris.sugonannotation.resolver.system;

import com.sugon.iris.sugonannotation.annotation.system.BussLog;
import com.sugon.iris.sugonannotation.annotation.system.LogInCheck;
import com.sugon.iris.sugondomain.beans.baseBeans.Error;
import com.sugon.iris.sugondomain.beans.baseBeans.RestResult;
import com.sugon.iris.sugondomain.beans.system.User;
import com.sugon.iris.sugondomain.enums.ErrorCode_Enum;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.*;

@Aspect
@Component
@Lazy(false)
public class BussLogAspect {


    @Around(value = "@annotation(com.sugon.iris.sugonannotation.annotation.system.BussLog) && @annotation(bussLog)")
    public void begin(ProceedingJoinPoint pdj, BussLog bussLog) throws Throwable {
        ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request =servletRequestAttributes.getRequest();
        HttpSession session =request.getSession();
        User obj = (User)session.getAttribute("user");
        String url = request.getRequestURL().toString();
        String ip = request.getRemoteAddr();
    }

}
