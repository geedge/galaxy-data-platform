package com.mesalab.api.modules.demo.controller;

import com.mesalab.api.common.base.BaseResult;
import com.mesalab.api.modules.demo.service.DemoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Date: 2020-12-03 09:46
 * @Author : liuyongqiang
 * @ClassName : DemoController
 * @Description : 内部服务调用Demo
 */
@Slf4j
@RestController
@RequestMapping("demo")
public class DemoController {

    @Autowired
    DemoService demoService;

    @Value("${spring.application.name}")
    private String applicationName;

    @Value("${server.port}")
    private String serverPort;

    @RequestMapping("index")
    public String index() {
        log.info(">>>applicationName:{}>>>Port:{}", applicationName, serverPort);
        return demoService.info();
    }

    @RequestMapping("sqlQuery")
    public BaseResult sqlQuery(){
        return demoService.sqlQuery();
    }


}
