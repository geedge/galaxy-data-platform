package com.mesalab.engine.service;

import com.mesalab.common.exception.BusinessException;

/**
 * @Date: 2020-07-19 14:22
 * @Author : liuyongqiang
 * @ClassName : AuthorizeService
 * @Description : JWT统一鉴权服务
 */

public interface AuthorizeService {

    /**
     * @Description: ArangoDBJwt登录
     * @Author: liuyongqiang
     * @Date: 2020/7/20 10:18
     * @return: java.lang.String
     **/
    String arangoJwtLogin() throws BusinessException;
}
