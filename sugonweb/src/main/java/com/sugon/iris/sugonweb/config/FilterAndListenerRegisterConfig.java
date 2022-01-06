package com.sugon.iris.sugonweb.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletListenerRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FilterAndListenerRegisterConfig {
	private static Logger logger = LoggerFactory.getLogger(FilterAndListenerRegisterConfig.class);

	@Bean
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public ServletListenerRegistrationBean servletListenerRegistrationBean() {
		logger.info("***************SingleSignOutHttpSessionListener**************************");
		ServletListenerRegistrationBean registration = new ServletListenerRegistrationBean<>();
		registration.setListener(new org.jasig.cas.client.session.SingleSignOutHttpSessionListener());
		registration.setOrder(0);
		return registration;

	}

	@Bean
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public FilterRegistrationBean singleSignOutFilter() {
		logger.info("***************SingleSignOutFilter**************************");
		FilterRegistrationBean registration = new FilterRegistrationBean();
		registration.setFilter(new org.jasig.cas.client.session.SingleSignOutFilter());
		//registration.addUrlPatterns("/a/cas/login_cas");
		registration.addUrlPatterns("/account/loginForCa");
        //registration.addInitParameter("exclusions", "/api/v1/*,*.js,*.gif,*.jpg,*.jpeg,*.png,*.css,*.ico,*.json,*.TTF,*.map,*.woff2,*.pdf");
		registration.setName("singleSignOutFilter");
		registration.setOrder(0);
		return registration;

	}

	@Bean
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public FilterRegistrationBean authenticationFilter() {
		logger.info("***************AuthenticationFilter**************************");
		FilterRegistrationBean registration = new FilterRegistrationBean();
		registration.setFilter(new org.jasig.cas.client.authentication.AuthenticationFilter());
		registration.addUrlPatterns("/account/loginForCa");
		registration.setName("authenticationFilter");
		registration.setOrder(1);
		// 添加不需要过滤的格式信息.
		// 更换为自己的服务器地址
		registration.addInitParameter("serverName", "http://50.73.71.248:8090");
		registration.addInitParameter("casServerLoginUrl", "http://tyyh.szh.js:9080/cas/login");
		return registration;
	}

	@Bean
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public FilterRegistrationBean cas20ProxyReceivingTicketValidationFilter() {
		logger.info("***************Cas20ProxyReceivingTicketValidationFilter**************************");
		FilterRegistrationBean registration = new FilterRegistrationBean();
		registration.setFilter(new org.jasig.cas.client.validation.Cas20ProxyReceivingTicketValidationFilter());
		registration.addUrlPatterns("/account/loginForCa");
		registration.setName("cas20ProxyReceivingTicketValidationFilter");
		registration.setOrder(2);
		// 更换为自己的服务器地址
		registration.addInitParameter("serverName", "http://50.73.71.248:8090");
		registration.addInitParameter("casServerUrlPrefix", "http://tyyh.szh.js:9080/cas");
		return registration;

	}

	@Bean
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public FilterRegistrationBean httpServletRequestWrapperFilter() {
		logger.info("***************HttpServletRequestWrapperFilter**************************");
		FilterRegistrationBean registration = new FilterRegistrationBean();
		registration.setFilter(new org.jasig.cas.client.util.HttpServletRequestWrapperFilter());
		registration.addUrlPatterns("/account/loginForCa");
		registration.setName("httpServletRequestWrapperFilter");
		registration.setOrder(3);
		return registration;

	}

	@Bean
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public FilterRegistrationBean assertionThreadLocalFilter() {
		logger.info("***************AssertionThreadLocalFilter**************************");
		FilterRegistrationBean registration = new FilterRegistrationBean();
		registration.setFilter(new org.jasig.cas.client.util.AssertionThreadLocalFilter());
		registration.addUrlPatterns("/account/loginForCa");
		registration.setName("assertionThreadLocalFilter");
		registration.setOrder(4);
		return registration;
	}
}
