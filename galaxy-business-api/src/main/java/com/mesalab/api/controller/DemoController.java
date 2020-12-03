package com.mesalab.api.controller;

import com.mesalab.api.service.DemoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Date: 2020-12-03 09:46
 * @Author : liuyongqiang
 * @ClassName : DemoController
 * @Description : 内部服务调用Demo
 */
@RestController
@RequestMapping("demo")
public class DemoController {

    @Autowired
    DemoService demoService;

    @RequestMapping("index")
    public String index() {
        return demoService.demo();
    }

}
