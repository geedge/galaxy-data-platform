package com.mesalab.hos.config;

import org.apache.catalina.Context;
import org.apache.catalina.webresources.StandardRoot;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created by wk1 on 2020/4/29.
 */
@Configuration
public class WebConfig {

    @Value("${tomcat.cacheMaxSize}")
    Integer cacheMaxSize;


    @Bean
    public TomcatServletWebServerFactory servletContainer() {
        return new TomcatServletWebServerFactory() {
            @Override
            protected void postProcessContext(Context context) {

                StandardRoot standardRoot = new StandardRoot(context);
                standardRoot.setCacheMaxSize(cacheMaxSize);
                context.setResources(standardRoot);
            }
        };
    }


}
