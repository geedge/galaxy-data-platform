package com.mesalab.hos.config;


import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.annotation.Resource;

/**
 * Created by wk1 on 2020/5/25.
 */
@Configuration
public class WebFilterConfig implements WebMvcConfigurer {
    //实现拦截器 要拦截的路径以及不拦截的路径
    @Resource
    private HosInterceptor hosInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        //注册自定义拦截器，添加拦截路径和排除拦截路径
        registry.addInterceptor(hosInterceptor).addPathPatterns("/hos/**").excludePathPatterns("/admin/login");
    }
}