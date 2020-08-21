package com.mesalab.engine.component.dialect;

import com.google.common.base.Stopwatch;
import com.google.common.collect.Maps;
import com.mesalab.common.util.DataTypeUtil;
import com.mesalab.engine.component.bean.SqlQueryBean;
import com.mesalab.engine.component.bean.ApiQueryBean;
import com.mesalab.common.dto.results.BaseResult;
import com.mesalab.common.enums.DBTypeEnum;
import com.mesalab.common.enums.QueryFormatEnum;
import com.mesalab.common.enums.ResultCodeEnum;
import com.mesalab.common.util.BaseResultUtil;
import com.mesalab.common.util.SqlHelper;
import com.mesalab.engine.component.SpringContext;
import com.mesalab.engine.component.SqlFunction;
import com.mesalab.engine.component.config.DruidConfig;
import com.mesalab.engine.component.config.EngineConfig;
import com.mesalab.engine.service.HttpClientService;
import com.zdjizhi.utils.JsonMapper;
import com.zdjizhi.utils.StringUtil;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpStatus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Slf4j
@SuppressWarnings("unchecked")
public class DruidDialect extends AbstractDataSourceDialect {

    private HttpClientService httpClientService = (HttpClientService) SpringContext.getBean("httpClientService");
    private DruidConfig druidConfig = (DruidConfig) SpringContext.getBean("druidConfig");
    private EngineConfig engineConfig = (EngineConfig) SpringContext.getBean("engineConfig");
    private int pageOffset;
    private Stopwatch watch = Stopwatch.createUnstarted();

    public DruidDialect(ApiQueryBean param) {
        initialize(param);
    }

    @Override
    public boolean supportsLimit() {
        return true;
    }


    @Override
    public String convertQuery(String sql) {
        return new Converter(sql).generateStandard().generateDateFunction().build();

    }

    @Override
    public String findBestQuery() {

        return new Optimizer().generateQueryLimit().generateSQL().build();

    }

    @Override
    public Dialect executeQuery() {

        watch.start();
        generateBaseResult(convertQuery(findBestQuery()));
        return this;

    }

    @Override
    public Dialect executeAdministrativeQuery() {
        generateBaseResult(param.getQuery());
        return this;
    }

    private void generateBaseResult(String sql) {

        Map<String, String> results = executeHttpPost(sql);

        if (results.get("status").equals(String.valueOf(HttpStatus.SC_OK))) {
            baseResult = BaseResultUtil.generate(Integer.valueOf(results.get("status")), ResultCodeEnum.EXECUTE_SUCCESS.getCode(), null,
                    (List<Object>) JsonMapper.fromJsonString(results.get("result"), Object.class), null, null, QueryFormatEnum.JSON.getValue());
        } else {
            baseResult = BaseResultUtil.generate(Integer.valueOf(results.get("status")), ResultCodeEnum.SQL_EXECUTION_ERROR.getCode(), results.get("message"),
                    results.get("result"), null, null, param.getFormat());
        }
    }

    /**
     * Druid Http Query Interface
     *
     * @param sql SQL语句
     * @return
     */
    private Map<String, String> executeHttpPost(String sql) {
        StringBuilder urlBuilder = new StringBuilder(druidConfig.getQueryUrl());
        DruidQueryParam druidQueryParam = new DruidQueryParam();
        druidQueryParam.setQuery(sql);
        druidQueryParam.getContext().put("sqlTimeZone", druidConfig.getSqlTimeZone());
        druidQueryParam.getContext().put("skipEmptyBuckets", druidConfig.getSkipEmptyBuckets());
        druidQueryParam.setResultFormat("object");
        log.info("DB Engine is :{}, Execute Query is: {}", DBTypeEnum.DRUID.getValue(), JsonMapper.toJsonString(druidQueryParam));
        return httpClientService.httpPost(urlBuilder.toString(), JsonMapper.toJsonString(druidQueryParam));
    }


    @Override
    public Dialect executeSyntaxCheck() {
        String query = convertQuery(param.getDbQuerySource().getSqlBody());
        Map<String, String> results = executeHttpPost("select 1");

        baseResult = BaseResultUtil.generate(Integer.valueOf(results.get("status")), null, query,
                (List<Object>) JsonMapper.fromJsonString(results.get("result"), Object.class), null, null, param.getFormat());
        log.info("option=syntax-check:  original sql: {}, after transformation(return message) info: {}", param.getQuery(), query);
        return this;
    }


    @Override
    public BaseResult build() {

        Object dataObject = baseResult.getData();

        if (baseResult.isSuccess()) {
            if (StringUtil.isBlank(baseResult.getMessage())) {
                baseResult.setMessage("ok");
            }
            List<Object> allResults = (List<Object>) dataObject;
            if (pageOffset > 0 && StringUtil.isNotEmpty(allResults)) {
                if (pageOffset >= allResults.size()) {
                    allResults = new ArrayList<>();
                } else {
                    allResults = allResults.subList(pageOffset, allResults.size());
                }
            }
            Map<String, Object> statistics = Maps.newLinkedHashMap();
            statistics.put("elapsed", watch.elapsed(TimeUnit.MILLISECONDS));
            statistics.put("rows_read", allResults.size());
            baseResult.setData(allResults);
            baseResult.setMeta(getMeta(allResults));
            baseResult.setStatistics(statistics);
        } else if (baseResult.getStatus().equals(HttpStatus.SC_NOT_FOUND)) {
            baseResult = BaseResultUtil.failure(HttpStatus.SC_NOT_FOUND, ResultCodeEnum.SQL_EXECUTION_ERROR.getCode(),
                    StringUtil.isEmpty(dataObject) ? "System Runtime Exception,Please contact Galaxy Data Platform Project manager. " : String.valueOf(dataObject));
        } else if (baseResult.getStatus().equals(HttpStatus.SC_INTERNAL_SERVER_ERROR)) {
            baseResult = BaseResultUtil.failure(HttpStatus.SC_INTERNAL_SERVER_ERROR, ResultCodeEnum.SQL_EXECUTION_ERROR.getCode(),
                    StringUtil.isEmpty(dataObject) ? "System Runtime Exception,Please contact Galaxy Data Platform Project manager. " : String.valueOf(dataObject));
        } else {
            baseResult = BaseResultUtil.failure(HttpStatus.SC_INTERNAL_SERVER_ERROR, ResultCodeEnum.SQL_EXECUTION_ERROR.getCode(),
                    StringUtil.isEmpty(dataObject) ? "System Runtime Exception,Please contact Galaxy Data Platform Project manager. " : String.valueOf(dataObject));
        }
        return baseResult;
    }

    private List getMeta(List<Object> results) {
        List meta = new ArrayList<Map<String, String>>();
        if (results.size() > 0) {
            Map<String, Object> map = (Map) results.get(0);
            for (String key : map.keySet()) {
                Object value = map.get(key);
                Map<String, String> temp = new HashMap<>();
                temp.put("name", key);
                if (value instanceof Integer) {
                    temp.put("type", DataTypeUtil.BIGINT);
                } else if (value instanceof Double) {
                    temp.put("type", DataTypeUtil.DOUBLE);
                } else {
                    temp.put("type", DataTypeUtil.VARCHAR);
                }
                meta.add(temp);
            }
        }
        return meta;
    }

    class Converter {
        String sql;

        public Converter(String sql) {
            this.sql = sql;
        }

        /**
         * SQL转换为DRUID语法格式
         * 1. 不支持limit offset,rows 转为 limit rows
         *
         * @return
         */
        public Converter generateStandard() {
            SqlQueryBean dbQuerySource = param.getDbQuerySource();
            if (StringUtil.isNotBlank(dbQuerySource.getLimit()) &&
                    dbQuerySource.getLimit().split(",").length >= 2) {

                String[] limits = dbQuerySource.getLimit().trim().split(",");
                int offset = Integer.valueOf(limits[0].trim());
                int rowNum = Integer.valueOf(limits[1].trim());
                pageOffset = offset;
                sql = SqlHelper.getLimitString(dbQuerySource.getSqlBody(), 0, (offset + rowNum));
            }
            return this;
        }


        public Converter generateDateFunction() {
            sql = SqlFunction.generateDateFunction(sql, DBTypeEnum.DRUID.getValue());
            return this;
        }


        public String build() {
            return sql;
        }
    }


    class Optimizer {
        String sql = null;

        /**
         * 对SQL最内层的结果数据集进行限制
         * 参见查询网关系统参数：engine.maxCacheNum
         *
         * @return
         */
        public Optimizer generateQueryLimit() {
            setDefaultResultRows(StringUtil.isEmpty(param.getEngineQuerySource()) ? param.getDbQuerySource() : param.getEngineQuerySource(), engineConfig.getDefaultResultNum());
            setMaxCacheResultRows(param.getDbQuerySource(), engineConfig.getMaxCacheNum());
            return this;
        }


        private void setDefaultResultRows(SqlQueryBean sqlQuerySource, int defaultResultRows) {
            if (StringUtil.isBlank(sqlQuerySource.getLimit())) {
                sqlQuerySource.setSqlBody(sqlQuerySource.getSqlBody() + " limit " + defaultResultRows);
                sqlQuerySource.setLimit(String.valueOf(defaultResultRows));
            }

        }

        /**
         * 设置最大缓存数据集
         *
         * @param sqlQuerySource
         * @return
         */
        private void setMaxCacheResultRows(SqlQueryBean sqlQuerySource, int maxCacheResultRows) {
            SqlQueryBean innerSqlQuerySource = sqlQuerySource;
            while (StringUtil.isNotEmpty(innerSqlQuerySource.getSubSelect())) {
                innerSqlQuerySource = innerSqlQuerySource.getSubSqlQuerySources().get(0);
            }

            if (!innerSqlQuerySource.isEnableLimit()) {
                return;
            }

            String orginInnerSql = innerSqlQuerySource.getSqlBody();

            String limitInnerSql = innerSqlQuerySource.getSqlBody();
            if (StringUtil.isNotBlank(innerSqlQuerySource.getLimit())) {

                String[] limits = innerSqlQuerySource.getLimit().split(",");

                if (Integer.valueOf(limits[limits.length - 1]) > maxCacheResultRows) {
                    limits[limits.length - 1] = String.valueOf(maxCacheResultRows);

                    if (limits.length == 2) {
                        limitInnerSql = SqlHelper.getLimitString(limitInnerSql, Integer.valueOf(limits[0]), Integer.valueOf(limits[1]));
                    } else {
                        limitInnerSql = SqlHelper.getLimitString(limitInnerSql, 0, Integer.valueOf(limits[0]));
                    }

                    innerSqlQuerySource.setLimit(String.join(",", limits).toString());
                    innerSqlQuerySource.setSqlBody(limitInnerSql);

                }

            } else {
                limitInnerSql = innerSqlQuerySource.getSqlBody() + " limit " + maxCacheResultRows;
                innerSqlQuerySource.setLimit(String.valueOf(maxCacheResultRows));
                innerSqlQuerySource.setSqlBody(limitInnerSql);

            }

            if (StringUtil.isNotEmpty(sqlQuerySource.getSubSqlQuerySources())) {
                sqlQuerySource.setSqlBody(sqlQuerySource.getSqlBody().replace(orginInnerSql, limitInnerSql));
            }

        }

        public Optimizer generateSQL() {
            SqlQueryBean sqlQuerySource = param.getDbQuerySource();
            sql = sqlQuerySource.getSqlBody();
            return this;
        }

        public String build() {
            return sql;
        }
    }

    @Data
    class DruidQueryParam {
        private String query;
        private Map<String, String> context = Maps.newHashMap();
        private String resultFormat;

    }
}
