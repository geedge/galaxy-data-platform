package com.mesalab.engine.common.pojo;

import cn.hutool.core.util.StrUtil;
import com.mesalab.engine.common.constant.SqlKeywords;
import com.mesalab.engine.service.DruidEngineService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.text.MessageFormat;
import java.util.List;

/**
 * @Date: 2020-07-22 17:00
 * @Author : liuyongqiang
 * @ClassName : DruidDSLParse
 * @Description : Druid DSL语法解析器
 */
@Slf4j
@Component
public class DruidParser extends DSLParser {

    @Autowired
    DruidEngineService druidEngineService;

    //表字段和数据类型查询SQL
    public final static String SCHEAM_QUERY = "SELECT COLUMN_NAME as name, DATA_TYPE as type FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_NAME = ''{0}''";

    //查询所有表
    public final static String TABLES_QUERY = "SELECT TABLE_NAME AS name FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_TYPE = 'TABLE'";


    @Override
    public String parseGranularity(DSLObject dslObject, String sql) {
        if (StrUtil.isEmpty(dslObject.getQuery().getGranularity())) {
            return sql;
        }
        String granularity = dslObject.getQuery().getGranularity();
        sql = sql.replace(SqlKeywords.SELECT, SqlKeywords.SELECT
                .concat(MessageFormat.format(SqlKeywords.D_TIME_FLOOR, granularity))
                .concat(","));
        return sql;
    }


    @Override
    public String parseSort(DSLObject dslObject, String sql) {
        if (CollectionUtils.isEmpty(dslObject.getQuery().getSort())) {
            return sql;
        }
        String sortType = dslObject.getQuery().getSort().get(0).getType();
        sql = sql.concat(SqlKeywords.ORDER_BY
                .concat(SqlKeywords.D_TIME))
                .concat(sortType);
        return sql;
    }

    @Override
    public String parseIntervals(DSLObject dslObject, String sql) {
        if (CollectionUtils.isEmpty(dslObject.getQuery().getIntervals())) {
            return sql;
        }
        List<DSLObject.QueryBean.IntervalsBean> intervalsBeanList = dslObject.getQuery().getIntervals();
        for (DSLObject.QueryBean.IntervalsBean intervalsBean : intervalsBeanList) {
            String fieldKey = intervalsBean.getFieldKey();
            List<String> values = intervalsBean.getFieldValues();

            if (CollectionUtils.isEmpty(values)) break;

            String join = sql.contains(SqlKeywords.WHERE) ? SqlKeywords.AND : SqlKeywords.WHERE;

            sql = sql.concat(join).concat(fieldKey)
                    .concat(SqlKeywords.BWTWEEN)
                    .concat("'" + values.get(0) + "'")
                    .concat(SqlKeywords.AND)
                    .concat("'" + values.get(1) + "'");
        }
        return sql;
    }

}
