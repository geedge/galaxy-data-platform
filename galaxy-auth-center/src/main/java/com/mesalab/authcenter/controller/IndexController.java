package com.mesalab.authcenter.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("index")
public class IndexController {

    @Value("${spring.application.name}")
    private String applicationName;

    @Value("${server.port}")
    private String serverPort;

    @GetMapping("info")
    public Map<String, Object> info() {
        log.info(">>>applicationName:{}>>>Port:{}", applicationName, serverPort);
        Map<String, Object> results = new HashMap<>();
        results.put("applicationName", applicationName);
        results.put("serverPort", serverPort);
        return results;
    }
}
