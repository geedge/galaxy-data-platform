package com.mesalab.auth.service.impl;

import com.google.common.collect.ImmutableMap;
import com.google.gson.Gson;
import com.mesalab.auth.component.config.ArangoConfig;
import com.mesalab.auth.service.AuthorizeService;
import com.mesalab.common.annotation.SysLog;
import com.mesalab.common.enums.CacheKeyEnum;
import com.mesalab.common.exception.BusinessException;
import com.mesalab.common.util.HttpClientUtil;
import com.mesalab.common.util.MemoryCache;
import com.mesalab.common.util.SysConstant;
import com.zdjizhi.utils.JsonMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.Header;
import org.apache.http.message.BasicHeader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
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

    @Autowired
    ArangoConfig arangoConfig;

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
        log.info("从缓存中获取的Token为:{}", cacheVal);
        if (Objects.nonNull(cacheVal)) {
            if (tokenValidate(String.valueOf(cacheVal))) {
                log.info("从缓存中获取的Token未失效!");
                return String.valueOf(cacheVal);
            } else {
                log.info("从缓存中获取的Token已失效!");
            }
        }

        Map<String, String> params = new HashMap<>();
        params.put("username", arangoConfig.getUserName());
        params.put("password", arangoConfig.getPassWord());
        String jwtStr;

        try {
            String jsonRes = HttpClientUtil.httpPost(arangoConfig.getAuthUrl(), new Gson().toJson(params));
            params = new Gson().fromJson(jsonRes, Map.class);
            jwtStr = params.get(SysConstant.ARANGO_JWT_STR);
            log.info("请求arango授权接口获取的jwt:{}", jwtStr);
            //将jwt字符串做缓存处理,过期时间为7天
            MemoryCache.getInstance().put(cacheKey, jwtStr, 604800);
        } catch (BusinessException e) {
            log.info("请求arango授权接口发生异常,异常信息:{}", e.getMessage());
            throw new BusinessException(e.getMessage());
        }

        return jwtStr;
    }

    /**
     * @param token:
     * @Description: 验证ArangoJwtToken是否有效
     * @Author: liuyongqiang
     * @Date: 2020/8/27 18:07
     * @return: boolean
     **/
    @Override
    public boolean tokenValidate(String token) throws BusinessException {
        Map queryMap = ImmutableMap.of("query", "");
        String jwtStr = SysConstant.ARANGO_JWT_PRE.concat(token);
        Header header = new BasicHeader(HttpHeaders.AUTHORIZATION, jwtStr);
        try {
            HttpClientUtil.httpPost(arangoConfig.getQueryurl(), JsonMapper.toJsonString(queryMap), header);
        } catch (BusinessException e) {
            if (e.getErrorMessage() != null && e.getErrorMessage().contains("not authorized to execute this request")) {
                return false;
            } else {
                return true;
            }
        }
        return true;
    }

}
