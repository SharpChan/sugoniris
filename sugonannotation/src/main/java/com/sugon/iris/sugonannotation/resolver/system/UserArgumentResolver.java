package com.sugon.iris.sugonannotation.resolver.system;

import com.sugon.iris.sugonannotation.annotation.system.CurrentUser;
import com.sugon.iris.sugondomain.beans.userBeans.User;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import javax.servlet.http.HttpServletRequest;
import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;

public class UserArgumentResolver implements HandlerMethodArgumentResolver {
    private final Logger logger =  LogManager.getLogger(UserArgumentResolver.class);

    /**
     * 过滤出符合条件的参数，这里指的是加了 CurrentUser 注解的参数
     *
     * @param parameter
     * @return
     */
    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        AnnotatedElement annotatedElement = parameter.getAnnotatedElement();
        Annotation[] annotations = annotatedElement.getAnnotations();
        logger.info(annotations.toString());
        return parameter.hasParameterAnnotation(CurrentUser.class);
    }

    @Override
    public User resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest
            webRequest, WebDataBinderFactory binderFactory) {
        HttpServletRequest servletRequest = webRequest.getNativeRequest(HttpServletRequest.class);
        User user = (User)servletRequest.getSession().getAttribute("user");
        if(null != user){
            return  user;
        }
        return null;
    }
}
