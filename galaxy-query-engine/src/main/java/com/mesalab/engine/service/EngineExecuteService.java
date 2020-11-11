package com.mesalab.engine.service;

import com.mesalab.engine.common.exception.BusinessException;

/**
 * @Date: 2020-07-22 16:11
 * @Author : liuyongqiang
 * @ClassName : EngineExecuteService
 * @Description : 引擎执行查询接口
 */
public interface EngineExecuteService {

    /**
     * @param sql:
     * @Description: 执行Druid查询语句
     * @Author: liuyongqiang
     * @Date: 2020/7/22 16:16
     * @return: java.lang.String
     **/
    String executeDruidSql(String sql) throws BusinessException;

    /**
     * @param sql:
     * @Description: 执行ClickHouse查询语句
     * @Author: liuyongqiang
     * @Date: 2020/7/22 16:16
     * @return: java.lang.String
     **/
    String executeClickHouseSql(String sql) throws BusinessException;

}
