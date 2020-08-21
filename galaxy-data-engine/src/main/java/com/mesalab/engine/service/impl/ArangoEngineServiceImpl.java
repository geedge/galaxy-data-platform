package com.mesalab.engine.service.impl;

import com.google.gson.Gson;
import com.mesalab.common.model.DSLObject;
import com.mesalab.common.dto.results.ArangoCollectionResult;
import com.mesalab.common.dto.results.ArangoCursorResult;
import com.mesalab.common.dto.results.BaseResult;
import com.mesalab.common.exception.BusinessException;
import com.mesalab.common.util.BaseResultUtil;
import com.mesalab.common.util.HttpClientUtil;
import com.mesalab.common.util.SqlKeywords;
import com.mesalab.common.util.SysConstant;
import com.mesalab.engine.component.ArangoParser;
import com.mesalab.engine.service.ArangoEngineService;
import com.mesalab.engine.service.AuthorizeService;
import com.mesalab.engine.service.EngineExecuteService;
import com.zdjizhi.utils.DateUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.Header;
import org.apache.http.message.BasicHeader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Date: 2020-07-20 10:03
 * @Author : liuyongqiang
 * @ClassName : ArangoEngineServiceImpl
 * @Description : ArangoDB数据查询引擎实现
 */
@Slf4j
@SuppressWarnings("unchecked")
@Service("arangoDBEngineService")
public class ArangoEngineServiceImpl implements ArangoEngineService {

    @Value("${arango.collection}")
    String arangoCollection;

    @Autowired
    ArangoParser arangoDSLParser;

    @Autowired
    EngineExecuteService executeService;

    @Autowired
    AuthorizeService authorizeService;

    @Override
    public BaseResult dslQuery(DSLObject dslObject) throws BusinessException {
        String executeSql = arangoDSLParser.allQuery(dslObject);
        String jsonResult = executeService.executeArangoDBSql(executeSql);
        ArangoCursorResult results = new Gson().fromJson(jsonResult, ArangoCursorResult.class);
        dataFormat(results);
        return BaseResultUtil.success(results.getResult());
    }



    @Override
    public BaseResult tablesQuery(DSLObject dslObject) throws BusinessException {
        String jwtStr = SysConstant.ARANGO_JWT_PRE.concat(authorizeService.arangoJwtLogin());
        Header header = new BasicHeader(HttpHeaders.AUTHORIZATION,jwtStr);
        String jsonResult = HttpClientUtil.httpGet(arangoCollection,header);
        ArangoCollectionResult collectionResultDTO = new Gson().fromJson(jsonResult, ArangoCollectionResult.class);
        List<Map<String,String>> resultList = new ArrayList<>();
        if(!CollectionUtils.isEmpty(collectionResultDTO.getResult())){
            for (ArangoCollectionResult.ResultBean resultBean:collectionResultDTO.getResult()){
                if(!resultBean.isSystem()){
                    Map<String,String> mapResult = new HashMap<>();
                    mapResult.put("id",resultBean.getId());
                    mapResult.put("name",resultBean.getName());
                    mapResult.put("status",String.valueOf(resultBean.getStatus()));
                    resultList.add(mapResult);
                }
            }
        }
        return BaseResultUtil.success(resultList);
    }

    @Override
    public BaseResult schemaQuery(DSLObject dslObject) throws BusinessException {
        return null;
    }

    /**
     * @Description: 数据格式转换
     * @Author: liuyongqiang
     * @Date: 2020/7/27 11:37
     * @param arangoResultDTO:
     * @return: com.mesalab.gateway.dtodefine.results.ArangoCursorResult
     **/
    private void dataFormat(ArangoCursorResult arangoResultDTO) {
        List<Map<String,Object>> result = arangoResultDTO.getResult();
        if(CollectionUtils.isEmpty(result)) return;
        for(Map<String,Object> map : result){
            for (String key : map.keySet()){
                //时间格式处理
                if(key.lastIndexOf(SqlKeywords.A_TIME)>0){
                    String time = String.format(SysConstant.DOUBLE_FORMAT, map.get(key));
                    map.put(key, DateUtils.convertTimestampToString(Long.valueOf(time),DateUtils.YYYY_MM_DD_HH24_MM_SS));
                }
                //数值类型数据处理
                if(map.get(key) instanceof Double){
                   map.put(key,((Double) map.get(key)).longValue());
                }
                if(map.get(key) instanceof ArrayList){
                    List<Object> values = (List<Object>) map.get(key);
                    List<Object> temp = new ArrayList<>();
                    for(Object val:values){
                        if(val instanceof Double){
                            temp.add(((Double) val).longValue());
                        }else{
                            temp.add(val);
                        }
                    }
                    map.put(key,temp);
                }
            }
        }
    }

}
