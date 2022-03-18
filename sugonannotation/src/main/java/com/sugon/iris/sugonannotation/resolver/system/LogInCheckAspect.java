package com.sugon.iris.sugonannotation.resolver.system;

import com.sugon.iris.sugonannotation.annotation.system.LogInCheck;
import com.sugon.iris.sugondomain.beans.baseBeans.Error;
import com.sugon.iris.sugondomain.beans.baseBeans.RestResult;
import com.sugon.iris.sugondomain.enums.ErrorCode_Enum;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import com.sugon.iris.sugondomain.beans.system.User;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Aspect
@Component
@Lazy(false)
public class LogInCheckAspect {
    /**
     * 定义切入点：对要拦截的方法进行定义与限制，如包、类
     *
     * 1、execution(public * *(..)) 任意的公共方法
     * 2、execution（* set*（..）） 以set开头的所有的方法
     * 3、execution（* com.lingyejun.annotation.LoggerApply.*（..））com.lingyejun.annotation.LoggerApply这个类里的所有的方法
     * 4、execution（* com.lingyejun.annotation.*.*（..））com.lingyejun.annotation包下的所有的类的所有的方法
     * 5、execution（* com.lingyejun.annotation..*.*（..））com.lingyejun.annotation包及子包下所有的类的所有的方法
     * 6、execution(* com.lingyejun.annotation..*.*(String,?,Long)) com.lingyejun.annotation包及子包下所有的类的有三个参数，第一个参数为String类型，第二个参数为任意类型，第三个参数为Long类型的方法
     * 7、execution(@annotation(com.lingyejun.annotation.Lingyejun))
     */
    //@Pointcut("@annotation(com.sugon.iris.sugoniris.annotation.LogInCheck)")
    //private void cutMethod() {

    //}

    /**
     * 前置通知：在目标方法执行前调用
     */
    @Around(value = "@annotation(com.sugon.iris.sugonannotation.annotation.system.LogInCheck) && @annotation(logInCheck)")
    public RestResult<Object> begin(ProceedingJoinPoint pjp, LogInCheck logInCheck) throws Throwable {
        ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes)RequestContextHolder.getRequestAttributes();
        HttpServletRequest request =servletRequestAttributes.getRequest();
        HttpSession session =request.getSession();
        User obj = (User)session.getAttribute("user");
        RestResult<Object> restResult = new RestResult();
        Set<Error> errorSet = new HashSet<>();
        List<Error> errorList = new ArrayList<>();
        restResult.setErrorList(errorList);

        Object[] args = pjp.getArgs();
        if(null == obj){
            restResult.setFlag("FAILED");
            errorSet.add(new Error(ErrorCode_Enum.SYS_00_000.getCode(),ErrorCode_Enum.SYS_00_000.getMessage()));
        }else if(obj.isLocked() && logInCheck.doLock()){
            restResult.setFlag("FAILED");
            errorSet.add(new Error(ErrorCode_Enum.SYS_01_000.getCode(),ErrorCode_Enum.SYS_01_000.getMessage()));
        }

        else if(logInCheck.doProcess()){
            restResult = (RestResult<Object>) pjp.proceed(args);
        }
        errorList.addAll(errorSet);
        return restResult;
    }
}
