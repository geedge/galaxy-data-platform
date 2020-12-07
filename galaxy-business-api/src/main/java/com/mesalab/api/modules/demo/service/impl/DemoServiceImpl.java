package com.mesalab.api.modules.demo.service.impl;

import com.mesalab.api.common.base.BaseResult;
import com.mesalab.api.modules.demo.service.DemoService;
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
    public String info() {
        String url = "http://galaxy-auth-center/index/info";
        return restTemplate.getForObject(url, String.class);
    }

    @Override
    public BaseResult sqlQuery() {
        String url = "http://galaxy-query-engine/?query=select *  from sys_storage_log limit 10";
        return restTemplate.getForObject(url, BaseResult.class);
    }
}
