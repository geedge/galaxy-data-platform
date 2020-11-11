package com.mesalab.engine.common.pojo;

import cn.hutool.core.util.StrUtil;
import com.mesalab.engine.common.constant.SqlKeywords;
import com.mesalab.engine.common.enums.EngineTypeEnum;
import com.mesalab.engine.common.enums.MatchTypeEnum;
import com.mesalab.engine.common.enums.RangeTypeEnum;
import com.mesalab.engine.service.ClickHouseEngineService;
import com.mesalab.engine.service.DruidEngineService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.text.MessageFormat;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * @Date: 2020-07-22 17:35
 * @Author : liuyongqiang
 * @ClassName : DslParser
 * @Description : DSLParse解析抽象类
 */
@Slf4j
@Component
public abstract class DSLParser {

    //默认数据大小
    private final static String DEFAULT_LIMIT = "100000";

    @Autowired
    DruidEngineService druidEngine;

    @Autowired
    ClickHouseEngineService clickHouseEngine;


    /**
     * 解析granularity字段
     */
    public abstract String parseGranularity(DSLObject dslObject, String sql);

    /**
     * 解析 Sort对象
     */
    public abstract String parseSort(DSLObject dslObject, String sql);

    /**
     * 解析 Intervals对象
     */
    public abstract String parseIntervals(DSLObject dslObject, String sql);

    /**
     * @param dslObject:
     * @Description: 单表全列查询
     * @Author: liuyongqiang
     * @Date: 2020/7/23 18:23
     * @return: java.lang.String
     **/
    public String allQuery(DSLObject dslObject) {
        String selectSql = this.allColumn(dslObject);
        selectSql = parseQuery(dslObject, selectSql);
        return selectSql;
    }

    /**
     * @param dslObject:
     * @Description: 查询表中的所有列
     * @Author: liuyongqiang
     * @Date: 2020/7/23 17:24
     * @return: java.lang.String
     **/
    public String allColumn(DSLObject dslObject) {
        BaseResult baseResult = null;

        switch (EngineTypeEnum.getByEngine(dslObject.getDataEngine())) {
            case CACULATION_ENGINE:
                baseResult = druidEngine.schemaQuery(dslObject);
                break;
            case BUSINESS_ENGINE:
                baseResult = clickHouseEngine.schemaQuery(dslObject);
                break;
        }

        List<Map<String, Object>> results = (List<Map<String, Object>>) baseResult.getData();
        String column = "";
        for (Map<String, Object> map : results) {
            String name = String.valueOf(map.get("name"));
            //ClickHouse数据表中的时间字段做日期转换
            if (dslObject.getDataEngine().equals(EngineTypeEnum.BUSINESS_ENGINE.getEngine())) {
                if (name.lastIndexOf("_time") > 0) {
                    name = MessageFormat.format(" toDateTime({0}) as c_{1} ", name, name);
                }
            }
            //Druid数据表中的时间字段做日期转换
            if (dslObject.getDataEngine().equals(EngineTypeEnum.CACULATION_ENGINE.getEngine())) {
                if (name.equalsIgnoreCase("__time")) {
                    name = "TIME_FORMAT(__time,'yyyy-MM-dd HH:mm:ss') as c_time";
                }
            }
            //拼接表中的列名称
            column = column.concat(name).concat(",");
        }
        //去掉最后一个逗号
        column = column.substring(0, column.length() - 1);
        return SqlKeywords.SELECT
                .concat(column)
                .concat(SqlKeywords.FROM)
                .concat(dslObject.getDataSource());
    }


    /**
     * @param dslObject:
     * @param sql:
     * @Description: 解析query对象
     * @Author: liuyongqiang
     * @Date: 2020/7/23 14:29
     * @return: java.lang.String
     **/
    public String parseQuery(DSLObject dslObject, String sql) {
        if (Objects.nonNull(dslObject.getQuery())) {
            sql = parseGranularity(dslObject, sql);//解析Granularity
            sql = parseMatch(dslObject, sql);//解析Match
            sql = parseRange(dslObject, sql);//range解析
            sql = parseIntervals(dslObject, sql);//解析Intervals
            sql = parseSort(dslObject, sql);//解析Sort
        }
        sql = parseLimit(dslObject, sql);//limit解析
        log.info("Parse Query SQL:{}", sql);
        return sql;
    }

    /**
     * @param dslObject:
     * @param sql:
     * @Description: 解析 Range对象
     * @Author: liuyongqiang
     * @Date: 2020/7/23 17:47
     * @return: java.lang.String
     **/
    public String parseRange(DSLObject dslObject, String sql) {
        if (CollectionUtils.isEmpty(dslObject.getQuery().getRange())) {
            return sql;
        }
        List<DSLObject.QueryBean.RangeBean> rangeBeanList = dslObject.getQuery().getRange();
        for (DSLObject.QueryBean.RangeBean rangeBean : rangeBeanList) {
            String type = rangeBean.getType();
            String fieldKey = rangeBean.getFieldKey();
            List<String> values = rangeBean.getFieldValues();

            if (CollectionUtils.isEmpty(values)) break;

            String join = sql.contains(SqlKeywords.WHERE) ? SqlKeywords.AND : SqlKeywords.WHERE;
            String expr = RangeTypeEnum.getByType(type).getExpr();
            sql = sql.concat(join).concat(fieldKey).concat(expr).concat(values.get(0));

            if (values.size() > 1) {
                for (int i = 1; i < values.size(); i++) {
                    sql = sql.concat(SqlKeywords.OR)
                            .concat(fieldKey)
                            .concat(expr)
                            .concat(values.get(i));
                }
            }
        }
        return sql;
    }

    /**
     * @param dslObject:
     * @param sql:
     * @Description: 解析 Match对象
     * @Author: liuyongqiang
     * @Date: 2020/7/23 17:45
     * @return: java.lang.String
     **/
    public String parseMatch(DSLObject dslObject, String sql) {
        if (CollectionUtils.isEmpty(dslObject.getQuery().getMatch())) {
            return sql;
        }
        List<DSLObject.QueryBean.MatchBean> matchBeanList = dslObject.getQuery().getMatch();
        int index = 0;
        for (DSLObject.QueryBean.MatchBean matchBean : matchBeanList) {
            index++;
            String type = matchBean.getType();
            String fieldKey = matchBean.getFieldKey();
            List<String> values = matchBean.getFieldValues();

            if (CollectionUtils.isEmpty(values)) break;

            String join = index == 1 ? SqlKeywords.WHERE : SqlKeywords.AND;
            sql = sql.concat(join)
                    .concat(fieldKey)
                    .concat(SqlKeywords.LIKE)
                    .concat(parseLike(values.get(0), type));

            if (values.size() > 1) {
                for (int i = 1; i < values.size(); i++) {
                    sql = sql.concat(SqlKeywords.OR)
                            .concat(fieldKey)
                            .concat(SqlKeywords.LIKE)
                            .concat(parseLike(values.get(i), type));
                }
            }
        }
        return sql;
    }


    /**
     * @param dslObject:
     * @param sql:
     * @Description: 解析Limit对象
     * @Author: liuyongqiang
     * @Date: 2020/7/23 17:44
     * @return: java.lang.String
     **/
    public String parseLimit(DSLObject dslObject, String sql) {
        sql = sql.concat(SqlKeywords.LIMIT);
        if (StrUtil.isNotEmpty(dslObject.getLimit())) {
            sql = sql.concat(dslObject.getLimit());
        } else {
            sql = sql.concat(DEFAULT_LIMIT);
        }
        return sql;
    }

    /**
     * @param value:
     * @param type:
     * @Description: match.type转换
     * @Author: liuyongqiang
     * @Date: 2020/7/23 11:47
     * @return: java.lang.String
     **/
    public String parseLike(String value, String type) {
        switch (MatchTypeEnum.getByType(type)) {
            case REGEX:
                return parseRegex(value);//正则匹配
            case PREFIX:
                return "'" + value + "%'";//前缀匹配
            case SUFFIX:
                return "'%" + value + "'";//后缀匹配
            case EXACTLY:
                return "'" + value + "'";//完全匹配
            case SUBSTRING:
                return "'%" + value + "%'";//子串匹配
        }
        return "";
    }

    /**
     * @param value:
     * @Description: parseRegex
     * @Author: liuyongqiang
     * @Date: 2020/7/23 11:49
     * @return: java.lang.String
     **/
    public String parseRegex(String value) {
        if (value.startsWith("$")) {
            //以$开头，不以*结尾，无论关键字其他位置是否包含表示匹配方式的字符*和$，即表示完整匹配
            value = value.replaceAll("\\$", "");
            return "'" + value + "'";
        } else if (value.startsWith("*") && value.endsWith("*")) {
            //以*开始，以*结尾，无论关键字其他位置是否包含表示匹配方式的字符*和$，即表示子串匹配
            value = value.replaceAll("\\*", "");
            return "'%" + value + "%'";
        } else if (value.startsWith("*")) {
            //以*开始，不以*结尾，无论关键字其他位置是否包含表示匹配方式的字符*和$，即表示右匹配（后缀匹配）
            value = value.replaceAll("\\*", "");
            return "'%" + value + "'";
        } else if (value.endsWith("*")) {
            //以*结尾，不以*或者$开始，无论关键字其他位置是否包含表示匹配方式的字符*和$，即表示左匹配(前缀匹配)
            value = value.replaceAll("\\*", "");
            return "'" + value + "%'";
        } else {
            //不以*或者$开头，不以*结尾，无论关键字其他位置是否包含表示匹配方式的字符*和$，即表示子串匹配
            return "'%" + value + "%'";
        }
    }

}
