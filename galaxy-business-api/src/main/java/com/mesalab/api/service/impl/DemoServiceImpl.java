package com.mesalab.api.service.impl;

import com.mesalab.api.service.DemoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

/**
 * @Date: 2020-12-03 09:48
 * @Author : liuyongqiang
 * @ClassName : DemoServiceImpl
 * @Description : 内部服务调用Demo
 */
@Service("demoService")
public class DemoServiceImpl implements DemoService {

    @Autowired
    RestTemplate restTemplate;

    @Override
    public String demo() {
        String url = "http://galaxy-auth-center/auth/index";
        return restTemplate.getForObject(url,String.class);
    }
}
