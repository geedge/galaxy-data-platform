package com.mesalab.auth.service;

import com.mesalab.common.exception.BusinessException;

/**
 * @Date: 2020-08-17 14:21
 * @Author : liuyongqiang
 * @ClassName : AuthorizeService
 * @Description : JWT授权服务
 */
public interface AuthorizeService {

    /**
     * @Description: ArangoDBJwt登录
     * @Author: liuyongqiang
     * @Date: 2020/7/20 10:18
     * @return: java.lang.String
     **/
    String arangoJwtLogin() throws BusinessException;

    /**
     * @Description: 验证ArangoJwtToken是否有效
     * @Author: liuyongqiang
     * @Date: 2020/8/27 18:07
     * @param token:
     * @return: boolean
     **/
    boolean tokenValidate(String token)throws BusinessException;
}
