package com.mesalab.engine.service.impl;

import com.google.gson.Gson;
import com.mesalab.common.model.DSLObject;
import com.mesalab.common.dto.results.BaseResult;
import com.mesalab.common.dto.results.ClickHouseResult;
import com.mesalab.common.exception.BusinessException;
import com.mesalab.common.util.BaseResultUtil;
import com.mesalab.engine.component.ClickHouseParser;
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
@SuppressWarnings("unchecked")
@Service("clickHouseEngineService")
public class ClickHouseEngineServiceImpl implements ClickHouseEngineService {

    @Autowired
    ClickHouseParser clickHouseDSLParser;

    @Autowired
    EngineExecuteService executeService;

    /**
     * @Description: 单表数据查询
     * @Author: liuyongqiang
     * @Date: 2020/7/21 9:41
     * @param dslObject: DSL查询对象
     * @return: com.mesalab.common.base.BaseResult
     **/
    @Override
    public BaseResult dslQuery(DSLObject dslObject) throws BusinessException {
        String executeSql = clickHouseDSLParser.allQuery(dslObject);
        String jsonResult = executeService.executeClickHouseSql(executeSql);
        return assemble(jsonResult);
    }

    /**
     * @Description: 查询数据源下的所有表
     * @Author: liuyongqiang
     * @Date: 2020/7/23 15:16
     * @param dslObject:
     * @return: com.mesalab.common.base.BaseResult
     **/
    @Override
    public BaseResult tablesQuery(DSLObject dslObject) throws BusinessException {
        String executeSql = ClickHouseParser.TABLES_QUERY;
        String jsonResult = executeService.executeClickHouseSql(executeSql);
        return assemble(jsonResult);
    }

    /**
     * @Description: 查询指定表中的所有字段
     * @Author: liuyongqiang
     * @Date: 2020/7/23 11:08
     * @param dslObject:
     * @return: com.mesalab.common.base.BaseResult
     **/
    @Override
    public BaseResult schemaQuery(DSLObject dslObject) throws BusinessException {
        String dataSource = dslObject.getDataSource();
        String executeSql = MessageFormat.format(ClickHouseParser.SCHEAM_QUERY,dataSource);
        return assemble(executeService.executeClickHouseSql(executeSql));
    }

    /**
     * @Description: 组装返回结果
     * @Author: liuyongqiang
     * @Date: 2020/7/23 16:52
     * @param jsonResult:
     * @return: com.mesalab.common.base.BaseResult
     **/
    private BaseResult assemble(String jsonResult){
        ClickHouseResult clickHouseResultDTO = new Gson().fromJson(jsonResult, ClickHouseResult.class);
        BaseResult baseResult = BaseResultUtil.success(clickHouseResultDTO.getData());
        baseResult.setStatistics(clickHouseResultDTO.getStatistics());
        baseResult.setMeta(clickHouseResultDTO.getMeta());
        return baseResult;
    }
}
