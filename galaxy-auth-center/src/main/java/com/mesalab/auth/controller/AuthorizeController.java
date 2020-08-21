package com.mesalab.auth.controller;

import com.mesalab.auth.service.AuthorizeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Date: 2020-08-17 14:25
 * @Author : liuyongqiang
 * @ClassName : JwtAuthorizeController
 * @Description : JWT授权服务
 */
@Slf4j
@RestController
@RequestMapping("/authorize/")
public class AuthorizeController {

    @Autowired
    AuthorizeService jwtAuthorizeService;

    @PostMapping("arangoJwtLogin")
    public String arangoJwtLogin(){
        return jwtAuthorizeService.arangoJwtLogin();
    }
}
