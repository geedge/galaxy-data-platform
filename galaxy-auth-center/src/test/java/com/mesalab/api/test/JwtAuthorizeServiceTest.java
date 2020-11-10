package com.mesalab.api.test;

import com.mesalab.auth.GalaxyAuthCenterApp;
import com.mesalab.auth.service.AuthorizeService;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @Date: 2020-08-17 14:33
 * @Author : liuyongqiang
 * @ClassName : JwtAuthorizeServiceTest
 * @Description : JWT授权服务测试
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = {GalaxyAuthCenterApp.class})// 指定启动类
@Slf4j
public class JwtAuthorizeServiceTest {

    @Autowired
    AuthorizeService jwtAuthorizeService;

    //ArangoDBJwt登录测试
    @Test
    public void arangoJwtLoginTest() {
        log.info(jwtAuthorizeService.arangoJwtLogin());
    }
}
