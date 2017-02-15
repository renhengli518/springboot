package com.renhengli.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import com.renhengli.interceptor.ErrorInterceptor;  
@Configuration  
public class MyWebAppConfigurer extends WebMvcConfigurerAdapter {  
    @Override  
    public void addInterceptors(InterceptorRegistry registry) {  
        // 多个拦截器组成一个拦截器链  
        // addPathPatterns 用于添加拦截规则  
        // excludePathPatterns 用户排除拦截  
        registry.addInterceptor(new ErrorInterceptor()).addPathPatterns("/**");  
        super.addInterceptors(registry);  
    }  
    //这种方式会在默认的基础上增加/static/**映射到classpath:/static/，不会影响默认的方式，可以同时使用
//    @Override
//    public void addResourceHandlers(ResourceHandlerRegistry registry) {
//        registry.addResourceHandler("/static/**")
//                .addResourceLocations("classpath:/static/");
//    }
}  