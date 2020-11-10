package com.mesalab.engine.component;

import com.mesalab.common.model.DSLObject;
import com.mesalab.common.enums.RangeTypeEnum;
import com.mesalab.common.util.SqlKeywords;
import com.zdjizhi.utils.DateUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.text.MessageFormat;
import java.util.List;
import java.util.Objects;

/**
 * @Date: 2020-07-22 18:02
 * @Author : liuyongqiang
 * @ClassName : ArangoDSLParser
 * @Description : ArangoDB DSL语法解析器
 */
@Slf4j
@Component
public class ArangoParser extends DSLParser {

    public final static String ALL_QUERY = "FOR {0} IN {1} RETURN {2}";

    @Override
    public String allQuery(DSLObject dslObject) {
        String dataSource = dslObject.getDataSource();
        String aql = MessageFormat.format(
                ArangoParser.ALL_QUERY,
                dataSource.toLowerCase(),
                dataSource.toUpperCase(),
                dataSource.toLowerCase());
        aql = parseQuery(dslObject,aql);
        return aql;
    }

    @Override
    public String parseQuery(DSLObject dslObject, String aql) {
        if(!Objects.isNull(dslObject.getQuery())){
            aql = parseGranularity(dslObject,aql);
            aql = parseMatch(dslObject,aql);
            aql = parseRange(dslObject,aql);
            aql = parseIntervals(dslObject,aql);
            aql = parseSort(dslObject,aql);
        }
        aql = parseLimit(dslObject,aql);
        log.info("Parse Query AQL:{}",aql);
        return aql;
    }

    @Override
    public String parseGranularity(DSLObject dslObject, String aql) {
        return aql;
    }

    @Override
    public String parseLimit(DSLObject dslObject, String aql) {

        if(StringUtils.isEmpty(dslObject.getLimit())){
            return aql;
        }

        StringBuilder stringBuilder = new StringBuilder(aql);
        int index = stringBuilder.indexOf(SqlKeywords.A_RETURN);
        stringBuilder.insert(index, SqlKeywords.A_LIMIT
        .concat(SqlKeywords.A_SPACE).concat(dslObject.getLimit()).concat(SqlKeywords.A_SPACE));

        return stringBuilder.toString();
    }

    @Override
    public String parseRange(DSLObject dslObject, String aql) {
        if(CollectionUtils.isEmpty(dslObject.getQuery().getRange())){
            return aql;
        }

        StringBuilder builder1 = new StringBuilder(aql);
        StringBuilder builder2;
        String dataSource = dslObject.getDataSource();
        List<DSLObject.QueryBean.RangeBean> rangeBeanList =  dslObject.getQuery().getRange();

        for (DSLObject.QueryBean.RangeBean rangeBean:rangeBeanList){
            String type = rangeBean.getType();
            String fieldKey = rangeBean.getFieldKey();
            List<String> values = rangeBean.getFieldValues();
            if(CollectionUtils.isEmpty(values)) break;

            String expr = RangeTypeEnum.getByType(type).getExpr();
            String join = aql.contains(SqlKeywords.A_FILTER)? SqlKeywords.A_AND: SqlKeywords.A_FILTER;
            builder1.insert(getIndex(aql,builder1),join.concat(SqlKeywords.A_SPACE).concat(dataSource.toLowerCase()).concat(".")
            .concat(fieldKey).concat(expr).concat(values.get(0)).concat(SqlKeywords.A_SPACE));
            builder2 = new StringBuilder(builder1.toString());

            if(values.size()>1){
                for (int i=1;i<values.size();i++){
                    builder2.insert(getIndex(builder1.toString(),builder2),
                    SqlKeywords.A_OR.concat(SqlKeywords.A_SPACE)
                    .concat(dataSource.toLowerCase()).concat(SqlKeywords.A_SPOT).concat(fieldKey)
                    .concat(expr).concat(values.get(i)).concat(SqlKeywords.A_SPACE));
                }
            }
            aql = builder2.toString();
        }
        return aql;
    }

    @Override
    public String parseSort(DSLObject dslObject, String aql) {
        if(CollectionUtils.isEmpty(dslObject.getQuery().getSort())){
            return aql;
        }
        StringBuilder stringBuilder = new StringBuilder(aql);
        int index;
        if(aql.contains(SqlKeywords.A_LIMIT)){
            index = stringBuilder.indexOf(SqlKeywords.A_LIMIT);
        }else{
            index = stringBuilder.indexOf(SqlKeywords.A_RETURN);
        }
        List<DSLObject.QueryBean.SortBean> sortBeans = dslObject.getQuery().getSort();
        stringBuilder.insert(index, SqlKeywords.A_SORT.concat(SqlKeywords.A_SPACE)
                .concat(dslObject.getDataSource().toLowerCase())
                .concat(SqlKeywords.A_SPOT)
                .concat(sortBeans.get(0).getFieldKey())
                .concat(SqlKeywords.A_SPACE)
                .concat(sortBeans.get(0).getType())
                .concat(SqlKeywords.A_SPACE));
        return stringBuilder.toString();
    }

    @Override
    public String parseIntervals(DSLObject dslObject, String aql) {
        if(CollectionUtils.isEmpty(dslObject.getQuery().getIntervals())){
            return aql;
        }
        StringBuilder builder1 = new StringBuilder(aql);
        String dataSource = dslObject.getDataSource();
        List<DSLObject.QueryBean.IntervalsBean> intervalsBeanList = dslObject.getQuery().getIntervals();
        for (DSLObject.QueryBean.IntervalsBean intervalsBean : intervalsBeanList){
            String fieldKey = intervalsBean.getFieldKey();
            List<String> values = intervalsBean.getFieldValues();
            if(CollectionUtils.isEmpty(values)) break;

            String join = aql.contains(SqlKeywords.A_FILTER)? SqlKeywords.A_AND: SqlKeywords.A_FILTER;
            builder1.insert(getIndex(aql,builder1),join.concat(SqlKeywords.A_SPACE)
            .concat(dataSource.toLowerCase()).concat(SqlKeywords.A_SPOT).concat(fieldKey).concat(SqlKeywords.A_SPACE).concat(">=")
            .concat(SqlKeywords.A_SPACE).concat(String.valueOf(DateUtils.convertStringToTimestamp(values.get(0),DateUtils.YYYY_MM_DD_HH24_MM_SS)))
            .concat(SqlKeywords.A_SPACE).concat(SqlKeywords.A_AND).concat(SqlKeywords.A_SPACE).concat(dataSource.toLowerCase()).concat(SqlKeywords.A_SPOT)
            .concat(fieldKey).concat(SqlKeywords.A_SPACE).concat("<").concat(SqlKeywords.A_SPACE)
            .concat(String.valueOf(DateUtils.convertStringToTimestamp(values.get(1),DateUtils.YYYY_MM_DD_HH24_MM_SS))).concat(SqlKeywords.A_SPACE));
            aql = builder1.toString();
        }
        return aql;
    }

    @Override
    public String parseMatch(DSLObject dslObject, String aql) {
        if(CollectionUtils.isEmpty(dslObject.getQuery().getMatch())){
            return aql;
        }
        StringBuilder builder1 = new StringBuilder(aql);
        StringBuilder builder2;
        String dataSource = dslObject.getDataSource();
        List<DSLObject.QueryBean.MatchBean> matchBeanList =  dslObject.getQuery().getMatch();
        for (DSLObject.QueryBean.MatchBean matchBean : matchBeanList){
            String type = matchBean.getType();
            String fieldKey = matchBean.getFieldKey();
            List<String> values = matchBean.getFieldValues();
            if(CollectionUtils.isEmpty(values)) break;

            String join = aql.contains(SqlKeywords.A_FILTER)? SqlKeywords.A_AND: SqlKeywords.A_FILTER;
            builder1.insert(getIndex(aql,builder1),join.concat(SqlKeywords.A_SPACE)
            .concat(dataSource.toLowerCase()).concat(SqlKeywords.A_SPOT)
            .concat(fieldKey).concat(SqlKeywords.A_SPACE)
            .concat(SqlKeywords.A_LIKE).concat(SqlKeywords.A_SPACE)
            .concat(parseLike(values.get(0),type)).concat(SqlKeywords.A_SPACE));

            builder2 = new StringBuilder(builder1.toString());
            if(values.size()>1){
                for (int i=1;i<values.size();i++){
                    builder2.insert(getIndex(builder1.toString(),builder2), SqlKeywords.A_OR
                    .concat(SqlKeywords.A_SPACE).concat(dataSource.toLowerCase()).concat(SqlKeywords.A_SPOT)
                    .concat(fieldKey).concat(SqlKeywords.A_SPACE).concat(SqlKeywords.A_LIKE).concat(SqlKeywords.A_SPACE)
                    .concat(parseLike(values.get(i),type)).concat(SqlKeywords.A_SPACE));
                }
            }
            aql = builder2.toString();
        }
        return aql;
    }

    private int getIndex(String str,StringBuilder builder){
        int index;
        if(str.contains(SqlKeywords.A_SORT)){
            index = builder.indexOf(SqlKeywords.A_SORT);
        }else if(str.contains(SqlKeywords.A_LIMIT)){
            index = builder.indexOf(SqlKeywords.A_LIMIT);
        }else{
            index = builder.indexOf(SqlKeywords.A_RETURN);
        }
        return index;
    }
}
