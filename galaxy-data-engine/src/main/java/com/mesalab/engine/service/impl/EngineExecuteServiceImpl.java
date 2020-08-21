package com.mesalab.engine.service.impl;

import com.google.gson.Gson;
import com.mesalab.common.exception.BusinessException;
import com.mesalab.common.util.HttpClientUtil;
import com.mesalab.common.annotation.SysLog;
import com.mesalab.engine.service.AuthorizeService;
import com.mesalab.engine.service.EngineExecuteService;
import org.apache.http.Header;
import org.apache.http.message.BasicHeader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import java.util.HashMap;
import java.util.Map;

/**
 * @Date: 2020-07-22 16:15
 * @Author : liuyongqiang
 * @ClassName : EngineExecuteServiceImpl
 * @Description : 引擎执行查询实现
 */
@Service("engineExecuteService")
public class EngineExecuteServiceImpl implements EngineExecuteService {

    final Header JSON_HEADER = new BasicHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);

    @Autowired
    AuthorizeService authorizedService;

    @Value("${druid.query.url}")
    String druidQueryUrl;
    @Value("${arango.query.url}")
    String arangoQueryUrl;
    @Value("${clickhouse.query.url}")
    String clickHouseQueryUrl;
    @Value("${clickhouse.dbname}")
    String clickHouseDbName;
    @Value("${clickhouse.real.time.username}")
    String ckRealTimeUsername;
    @Value("${clickhouse.real.time.password}")
    String ckRealTimePassword;

    @SysLog
    @Override
    public String executeDruidSql(String sql) throws BusinessException {
        Map<String,String> queryParams = new HashMap<>();
        queryParams.put("query",sql);
        String jsonParam = new Gson().toJson(queryParams);
        return HttpClientUtil.httpPost(druidQueryUrl,jsonParam,JSON_HEADER);
    }

    @SysLog
    @Override
    public String executeClickHouseSql(String sql) throws BusinessException {
        String url = clickHouseQueryUrl
                .concat("/?user=")
                .concat(ckRealTimeUsername)
                .concat("&password=")
                .concat(ckRealTimePassword)
                .concat("&database=")
                .concat(clickHouseDbName)
                .concat("&query=").concat(sql)
                .concat(" FORMAT JSON");
        return HttpClientUtil.httpGet(url);
    }

    @SysLog
    @Override
    public String executeArangoDBSql(String aql) throws BusinessException {
        Map<String,String> queryParams = new HashMap<>();
        queryParams.put("query",aql);
        String jwtStr = "bearer " + authorizedService.arangoJwtLogin();
        Header header = new BasicHeader(HttpHeaders.AUTHORIZATION,jwtStr);
        String jsonParam = new Gson().toJson(queryParams);
        return HttpClientUtil.httpPost(arangoQueryUrl,jsonParam,header);
    }
}
