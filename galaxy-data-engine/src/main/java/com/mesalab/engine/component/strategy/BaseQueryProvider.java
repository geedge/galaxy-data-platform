package com.mesalab.engine.component.strategy;

import com.google.common.collect.ImmutableMap;
import com.mesalab.common.exception.BusinessException;
import com.mesalab.common.util.HttpClientUtil;
import com.mesalab.common.util.SysConstant;
import com.mesalab.engine.component.config.ArangoConfig;
import com.mesalab.engine.service.AuthorizeService;
import com.zdjizhi.utils.JsonMapper;
import org.apache.http.Header;
import org.apache.http.message.BasicHeader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @description: for base method
 * @author: zhq
 * @create: 2020-07-13
 **/
@Component
public class BaseQueryProvider {

    @Autowired
    ArangoConfig arangoConfig;

    @Autowired
    AuthorizeService authorizeService;

    /***
     * @Author zhq
     * @Description
     * @Date 2020\8\12 0012
     * @Param [aql]
     * @return java.lang.Object
     **/
    public Object queryArango(String aql) throws BusinessException {
        try {
            Map queryMap = ImmutableMap.of("query", aql);
            String jwtStr = SysConstant.ARANGO_JWT_PRE.concat(authorizeService.arangoJwtLogin());
            Header header = new BasicHeader(HttpHeaders.AUTHORIZATION, jwtStr);
            String res = HttpClientUtil.httpPost(arangoConfig.getQueryurl(), JsonMapper.toJsonString(queryMap), header);
            Map jsonMap = (Map) JsonMapper.fromJsonString(res, Map.class);
            Object result = jsonMap.get("result");
            return result;
        } catch (BusinessException e) {
            throw e;
        }
    }
}
