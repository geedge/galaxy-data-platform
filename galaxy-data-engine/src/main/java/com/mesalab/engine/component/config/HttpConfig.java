package com.mesalab.engine.component.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Data
@Component
public class HttpConfig {

    @Value("${httpclient.connectTimeout}")
    private Integer connectTimeout;
    @Value("${httpclient.socketTimeout}")
    private Integer socketTimeout;
    @Value("${httpclient.maxConnection}")
    private Integer maxConnection;
    @Value("${httpclient.maxPerRoute}")
    private Integer maxPerRoute;

    private String  userAgent = "Mozilla/5.0(Windows NT 6.1;Win64; x64; rv:50.0) Gecko/20100101 Firefox/50.0";

}
