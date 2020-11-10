package com.mesalab.engine.component;

import cn.hutool.core.util.StrUtil;
import com.mesalab.common.model.DSLObject;
import com.mesalab.common.util.SqlKeywords;
import com.mesalab.engine.service.ClickHouseEngineService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.text.MessageFormat;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @Date: 2020-07-22 18:00
 * @Author : liuyongqiang
 * @ClassName : ClickHouseDSLParser
 * @Description : ClickHouse DSL语法解析器
 */
@Slf4j
@Component
public class ClickHouseParser extends DSLParser {

    @Autowired
    ClickHouseEngineService clickHouseEngineService;

    //表字段和数据类型查询SQL
    public final static String SCHEAM_QUERY = " desc {0} ";

    //查询所有表
    public final static String TABLES_QUERY = " show tables ";

    public static Pattern periodOfPT = Pattern.compile("PT(\\d+)(\\w+)", Pattern.CASE_INSENSITIVE);

    public static Pattern periodOfP = Pattern.compile("P(\\d+)(\\w+)", Pattern.CASE_INSENSITIVE);


    @Override
    public String parseGranularity(DSLObject dslObject, String sql) {
        if(StrUtil.isEmpty(dslObject.getQuery().getGranularity())){
            return sql;
        }
        String granularity = dslObject.getQuery().getGranularity();
        Matcher matcherPT = periodOfPT.matcher(granularity);
        Matcher matcherP = periodOfP.matcher(granularity);
        String format = "";

        if(matcherPT.find()){
            String PTnum = matcherPT.group(1);
            String PTunit = matcherPT.group(2);
            if ("S".equalsIgnoreCase(PTunit)) {
                format = MessageFormat.format(SqlKeywords.C_SECOND,PTnum);
            } else if ("M".equalsIgnoreCase(PTunit)) {
                format = MessageFormat.format(SqlKeywords.C_MINUTE,PTnum);
            } else if ("H".equalsIgnoreCase(PTunit)) {
                format = MessageFormat.format(SqlKeywords.C_HOUR,PTnum);
            }
        }

        if(matcherP.find()){
            String Pnum = matcherP.group(1);
            String Punit = matcherP.group(2);
            if ("D".equalsIgnoreCase(Punit)) {
                format = MessageFormat.format(SqlKeywords.C_DAY,Pnum);
            } else if ("W".equalsIgnoreCase(Punit)) {
                format = MessageFormat.format(SqlKeywords.C_WEEK,Pnum);
            } else if ("M".equalsIgnoreCase(Punit)) {
                format = MessageFormat.format(SqlKeywords.C_MONTH,Pnum);
            } else if ("Y".equalsIgnoreCase(Punit)) {
                format = MessageFormat.format(SqlKeywords.C_YEAR,Pnum);
            }
        }
        sql = sql.replace(SqlKeywords.SELECT, SqlKeywords.SELECT.concat(format).concat(","));
        return sql;
    }

    @Override
    public String parseSort(DSLObject dslObject, String sql) {
        if(CollectionUtils.isEmpty(dslObject.getQuery().getSort())){
            return sql;
        }
        String sortType = dslObject.getQuery().getSort().get(0).getType();
        sql = sql.concat(SqlKeywords.ORDER_BY
                .concat(dslObject.getQuery().getSort().get(0).getFieldKey()))
                .concat(" ")
                .concat(sortType);
        if(dslObject.getQuery().getSort().size()>1){
            List<DSLObject.QueryBean.SortBean> sortBeans = dslObject.getQuery().getSort();
            for (int i=1;i<sortBeans.size();i++){
                sql = sql.concat(" , ")
                        .concat(sortBeans.get(i).getFieldKey())
                        .concat(" ")
                        .concat(sortBeans.get(i).getType());
            }
        }
        return sql;
    }

    @Override
    public String parseIntervals(DSLObject dslObject, String sql) {
        if(CollectionUtils.isEmpty(dslObject.getQuery().getIntervals())){
            return sql;
        }

        List<DSLObject.QueryBean.IntervalsBean> intervalsBeanList = dslObject.getQuery().getIntervals();
        for (DSLObject.QueryBean.IntervalsBean intervalsBean : intervalsBeanList){
            String fieldKey = intervalsBean.getFieldKey();
            List<String> values = intervalsBean.getFieldValues();
            if(CollectionUtils.isEmpty(values)) break;
            sql = sql.concat(sql.contains(SqlKeywords.WHERE)? SqlKeywords.AND: SqlKeywords.WHERE)
                    .concat(fieldKey)
                    .concat(SqlKeywords.BWTWEEN)
                    .concat(MessageFormat.format(SqlKeywords.C_TO_DATE_TIME,values.get(0)))
                    .concat(SqlKeywords.AND)
                    .concat(MessageFormat.format(SqlKeywords.C_TO_DATE_TIME,values.get(1)));
        }
        return sql;
    }

}
