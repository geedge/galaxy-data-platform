package com.mesalab.engine.service;

import com.mesalab.engine.component.bean.SqlQueryBean;
import com.mesalab.engine.component.bean.ApiQueryBean;
import com.mesalab.common.dto.results.BaseResult;

/**
 * @Date: 2020-08-18 09:54
 * @Author : liuyongqiang
 * @ClassName : SqlEngineServiceImpl
 * @Description : 基于SQL的动态查询服务
 */
public interface SqlEngineService {

    /**
     * @Description: 执行查询
     * @Author: liuyongqiang
     * @Date: 2020/8/18 9:54
     * @param apiParam:
     * @return: com.mesalab.common.dto.results.BaseResult
     **/
    BaseResult executeQuery(ApiQueryBean apiParam);

    /**
     * @Description: 递归解析SQLQuerySource
     * @param param:
     * @param sqlQuerySource:
     * @return: void
     **/
    void convertQueryRecursive(ApiQueryBean param, SqlQueryBean sqlQuerySource);

    /**
     * @Description: 解析SQL
     * @param sql:
     * @return: com.mesalab.common.beans.SqlQueryBean
     **/
    SqlQueryBean parserSQLByAst(String sql);
}
