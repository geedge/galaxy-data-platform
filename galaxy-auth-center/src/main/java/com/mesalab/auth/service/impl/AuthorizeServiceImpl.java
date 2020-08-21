package com.mesalab.auth.service.impl;

import com.google.gson.Gson;
import com.mesalab.auth.service.AuthorizeService;
import com.mesalab.common.annotation.SysLog;
import com.mesalab.common.enums.CacheKeyEnum;
import com.mesalab.common.exception.BusinessException;
import com.mesalab.common.util.HttpClientUtil;
import com.mesalab.common.util.MemoryCache;
import com.mesalab.common.util.SysConstant;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * @Date: 2020-08-17 14:22
 * @Author : liuyongqiang
 * @ClassName : AuthorizeServiceImpl
 * @Description : JWT授权服务
 */
@Slf4j
@SuppressWarnings("unchecked")
@Service("jwtAuthorizeService")
public class AuthorizeServiceImpl implements AuthorizeService {

    @Value("${arango.username}")
    String arangoUserName;
    @Value("${arango.password}")
    String arangoPassWord;
    @Value("${arango.authurl}")
    String arangoAuthUrl;

    /**
     * @Description: ArangoDBJwt登录
     * @Author: liuyongqiang
     * @Date: 2020/7/20 10:18
     * @return: java.lang.String
     **/
    @SysLog
    @Override
    public String arangoJwtLogin() throws BusinessException {

        String cacheKey = CacheKeyEnum.ARONGO_JWT_KEY.getKey();
        Object cacheVal = MemoryCache.getInstance().get(cacheKey);
        log.info("从缓存中获取的arangoJwt字符串为:{}",cacheVal);
        if(Objects.nonNull(cacheVal))
            return String.valueOf(cacheVal);

        Map<String, String> params = new HashMap<>();
        params.put("username", arangoUserName);
        params.put("password", arangoPassWord);
        String jwtStr = null;

        try {
            String jsonRes = HttpClientUtil.httpPost(arangoAuthUrl,new Gson().toJson(params));
            params = new Gson().fromJson(jsonRes,Map.class);
            jwtStr = params.get(SysConstant.ARANGO_JWT_STR);
            log.info("请求arango授权接口获取的jwt:{}",jwtStr);
            //将jwt字符串做缓存处理,过期时间为7天
            MemoryCache.getInstance().put(cacheKey,jwtStr,604800);
        } catch (BusinessException e) {
            log.info("请求arango授权接口发生异常,异常信息:{}",e.getMessage());
            throw new BusinessException(e.getMessage());
        }

        return jwtStr;
    }
}
