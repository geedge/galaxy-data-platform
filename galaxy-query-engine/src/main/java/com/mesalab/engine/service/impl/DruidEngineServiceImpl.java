package com.mesalab.engine.service.impl;

import com.google.gson.Gson;
import com.mesalab.engine.common.exception.BusinessException;
import com.mesalab.engine.common.pojo.BaseResult;
import com.mesalab.engine.common.pojo.DSLObject;
import com.mesalab.engine.common.pojo.DruidParser;
import com.mesalab.engine.common.util.BaseResultUtil;
import com.mesalab.engine.service.DruidEngineService;
import com.mesalab.engine.service.EngineExecuteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;
import java.util.List;
import java.util.Map;

/**
 * @Date: 2020-07-20 10:05
 * @Author : liuyongqiang
 * @ClassName : DruidEngineServiceImpl
 * @Description : Druid数据查询引擎实现
 */
@Service("druidEngineService")
public class DruidEngineServiceImpl implements DruidEngineService {

    @Autowired
    DruidParser druidDSLParse;

    @Autowired
    EngineExecuteService executeService;

    /**
     * @param dslObject: DSL查询对象
     * @Description: 单表数据查询
     * @Author: liuyongqiang
     * @Date: 2020/7/21 9:41
     * @return: com.mesalab.common.base.BaseResult
     **/
    @Override
    public BaseResult dslQuery(DSLObject dslObject) throws BusinessException {
        String executeSql = druidDSLParse.allQuery(dslObject);
        String jsonResult = executeService.executeDruidSql(executeSql);
        List<Map<String, Object>> results = new Gson().fromJson(jsonResult, List.class);
        dataFormat(results);
        return BaseResultUtil.success(results);
    }


    /**
     * @param dslObject:
     * @Description: 查询数据源下的所有表
     * @Author: liuyongqiang
     * @Date: 2020/7/23 15:16
     * @return: com.mesalab.common.base.BaseResult
     **/
    @Override
    public BaseResult tablesQuery(DSLObject dslObject) throws BusinessException {
        String executeSql = DruidParser.TABLES_QUERY;
        String jsonResult = executeService.executeDruidSql(executeSql);
        List<Map<String, Object>> results = new Gson().fromJson(jsonResult, List.class);
        return BaseResultUtil.success(results);
    }

    /**
     * @param dslObject:
     * @Description: 查询指定表中的所有字段
     * @Author: liuyongqiang
     * @Date: 2020/7/23 11:08
     * @return: com.mesalab.common.base.BaseResult
     **/
    @Override
    public BaseResult schemaQuery(DSLObject dslObject) throws BusinessException {
        String dataSource = dslObject.getDataSource();
        String executeSql = MessageFormat.format(DruidParser.SCHEAM_QUERY, dataSource);
        String jsonResult = executeService.executeDruidSql(executeSql);
        List<Map<String, Object>> results = new Gson().fromJson(jsonResult, List.class);
        return BaseResultUtil.success(results);
    }

    /**
     * @param results:
     * @Description: 数据格式化
     * @Author: liuyongqiang
     * @Date: 2020/7/28 16:56
     * @return: java.util.List<java.util.Map < java.lang.String, java.lang.Object>>
     **/
    private void dataFormat(List<Map<String, Object>> results) {
        for (Map<String, Object> map : results) {
            for (String key : map.keySet()) {
                if (map.get(key) instanceof Double) {
                    map.put(key, ((Double) map.get(key)).longValue());
                }
            }
        }
    }
}
