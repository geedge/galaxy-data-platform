package com.mesalab.engine.service.impl;

import com.mesalab.common.exception.BusinessException;
import com.mesalab.common.annotation.SysLog;
import com.mesalab.engine.service.AuthorizeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

/**
 * @Date: 2020-07-19 14:24
 * @Author : liuyongqiang
 * @ClassName : AuthorizeServiceImpl
 * @Description : JWT统一鉴权服务实现
 */
@Slf4j
@Service("authorizeService")
public class AuthorizeServiceImpl implements AuthorizeService {

    @Autowired
    RestTemplate restTemplate;

    @Value("${arango.login}")
    String arangoLogin;

    /**
     * @Description: ArangoDBJwt登录
     * @Author: liuyongqiang
     * @Date: 2020/7/20 10:18
     * @return: java.lang.String
     **/
    @SysLog
    @Override
    public String arangoJwtLogin() throws BusinessException {
        ResponseEntity<String> responseEntity = restTemplate.postForEntity(arangoLogin,null,String.class);
        return responseEntity.getBody();
    }

}
