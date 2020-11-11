package com.mesalab.engine.service.impl;


import com.google.gson.Gson;
import com.mesalab.engine.common.dto.ClickHouseResult;
import com.mesalab.engine.common.exception.BusinessException;
import com.mesalab.engine.common.pojo.BaseResult;
import com.mesalab.engine.common.pojo.ClickHouseParser;
import com.mesalab.engine.common.pojo.DSLObject;
import com.mesalab.engine.common.util.BaseResultUtil;
import com.mesalab.engine.service.ClickHouseEngineService;
import com.mesalab.engine.service.EngineExecuteService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;

/**
 * @Date: 2020-07-20 10:04
 * @Author : liuyongqiang
 * @ClassName : ClickHouseEngineServiceImpl
 * @Description : ClickHouse数据查询引擎实现
 */
@Slf4j
@Service("clickHouseEngineService")
public class ClickHouseEngineServiceImpl implements ClickHouseEngineService {

    @Autowired
    ClickHouseParser clickHouseDSLParser;

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
        String executeSql = clickHouseDSLParser.allQuery(dslObject);
        String jsonResult = executeService.executeClickHouseQuery(executeSql);
        return assemble(jsonResult);
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
        String executeSql = ClickHouseParser.TABLES_QUERY;
        String jsonResult = executeService.executeClickHouseQuery(executeSql);
        return assemble(jsonResult);
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
        String executeSql = MessageFormat.format(ClickHouseParser.SCHEAM_QUERY, dataSource);
        return assemble(executeService.executeClickHouseQuery(executeSql));
    }

    /**
     * @param jsonResult:
     * @Description: 组装返回结果
     * @Author: liuyongqiang
     * @Date: 2020/7/23 16:52
     * @return: com.mesalab.common.base.BaseResult
     **/
    private BaseResult assemble(String jsonResult) {
        ClickHouseResult clickHouseResultDTO = new Gson().fromJson(jsonResult, ClickHouseResult.class);
        BaseResult baseResult = BaseResultUtil.success(clickHouseResultDTO.getData());
        baseResult.setStatistics(clickHouseResultDTO.getStatistics());
        baseResult.setMeta(clickHouseResultDTO.getMeta());
        return baseResult;
    }
}
