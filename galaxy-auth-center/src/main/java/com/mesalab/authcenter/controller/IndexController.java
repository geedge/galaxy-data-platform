package com.mesalab.authcenter.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("auth")
public class IndexController {

    @GetMapping("index")
    public String index() {
        log.info("galaxy-auth-center index execute...");
        return "index";
    }
}
