package com.mesalab.engine.component;

import cn.hutool.core.util.StrUtil;
import com.mesalab.common.model.DSLObject;
import com.mesalab.common.dto.results.BaseResult;
import com.mesalab.common.enums.*;
import com.mesalab.common.exception.BusinessException;
import com.mesalab.engine.service.ArangoEngineService;
import com.mesalab.engine.service.ClickHouseEngineService;
import com.mesalab.engine.service.DruidEngineService;
import org.apache.http.HttpStatus;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.regex.Pattern;

/**
 * @Date: 2020-07-20 18:10
 * @Author : wangwei
 * @ClassName : DSLValidate
 * @Description : DSL格式校验
 */
@Component
@SuppressWarnings("unchecked")
public class DSLValidate {

    public static Pattern periodOfPT = Pattern.compile("PT(\\d+)[SMH]", Pattern.CASE_INSENSITIVE);
    public static Pattern periodOfP = Pattern.compile("P(\\d+)[DWMY]", Pattern.CASE_INSENSITIVE);
    public static Pattern strFormatDateTime = Pattern.compile("\\d{4}-\\d{2}-\\d{2}\\s+\\d{2}:\\d{2}:\\d{2}", Pattern.CASE_INSENSITIVE);

    @Autowired
    DruidEngineService druidEngineService;
    @Autowired
    ClickHouseEngineService clickHouseEngineService;
    @Autowired
    ArangoEngineService arangoDBEngineService;
    @Value("${engine.maxCacheNum}")
    int maxCacheNum;


    /**
     * 1.dbType字段必须在支持的范围之内(ArangoDB、ClickHouse、Druid)
     * 2.dataSource数据表是否在库中存在
     * 3.limit不能大于100000
     * 4.时间格式校验
     * 5.intervals.fieldValues长度必须==2
     * 6.intervals.type暂时只支持between方式
     * 7.sort.type支持desc和asc
     * 8.match.type在:["exactly"|"prefix"|"suffix"|"substring"]内
     * 9.range.type在:["gt"|"lt"|"eq"|"ge"|"le"|"ne"]
     * 10.对表中的字段做是否存在的验证
     * 11.match.fieldValues中不允许出现以*开头$结束的值
     * 12.match.fieldKey字段类型不能为数值型
     * */
    public void executeValidate(DSLObject dslObject) throws BusinessException {
        if (Objects.isNull(dslObject)) {
            throw new BusinessException(HttpStatus.SC_BAD_REQUEST, ResultCodeEnum.PARAM_SYNTAX_ERROR.getCode(),
                    "DSLObject is invalid");
        }
        if (isValidEngine(dslObject.getDataEngine())) {
            if (!isValidDataSource(dslObject)) {
                throw new BusinessException(HttpStatus.SC_BAD_REQUEST, ResultCodeEnum.PARAM_SYNTAX_ERROR.getCode(),
                        "dataSource is invalid");
            }
        } else {
            throw new BusinessException(HttpStatus.SC_BAD_REQUEST, ResultCodeEnum.PARAM_SYNTAX_ERROR.getCode(),
                    "dataEngine is invalid");
        }
        if (Objects.isNull(dslObject.getQuery())) {
            return;
        }
        if (!isValidLimit(dslObject.getLimit())) {
            throw new BusinessException(HttpStatus.SC_BAD_REQUEST, ResultCodeEnum.PARAM_SYNTAX_ERROR.getCode(),
                    "Limit is invalid");
        }
        if (!isValidGranularity(dslObject)) {
            throw new BusinessException(HttpStatus.SC_BAD_REQUEST, ResultCodeEnum.PARAM_SYNTAX_ERROR.getCode(),
                    "query.Granularity value is invalid");
        }
        if (!isValidMatch(dslObject)) {
            throw new BusinessException(HttpStatus.SC_BAD_REQUEST, ResultCodeEnum.PARAM_SYNTAX_ERROR.getCode(),
                    "query.Match type is invalid");
        }
        if (!isValidRange(dslObject)) {
            throw new BusinessException(HttpStatus.SC_BAD_REQUEST, ResultCodeEnum.PARAM_SYNTAX_ERROR.getCode(),
                    "query.Range type is invalid");
        }
        if (!isValidIntervals(dslObject)) {
            throw new BusinessException(HttpStatus.SC_BAD_REQUEST, ResultCodeEnum.PARAM_SYNTAX_ERROR.getCode(),
                    "query.Intervals is invalid");
        }
        if (!isValidSort(dslObject)) {
            throw new BusinessException(HttpStatus.SC_BAD_REQUEST, ResultCodeEnum.PARAM_SYNTAX_ERROR.getCode(),
                    "query.Sort type is invalid");
        }
    }

    /**
     * 校验dataSource
     * 1.判定是否在{@link EngineTypeEnum}相应数据库中
     *
     * @param dslObject
     * @return
     */
    private boolean isValidDataSource(DSLObject dslObject) {
        if (StrUtil.isBlank(dslObject.getDataSource())) {
            return false;
        }
        if (EngineTypeEnum.ANALYSIS_ENGINE.getEngine().equals(dslObject.getDataEngine())) {
            BaseResult baseResult = arangoDBEngineService.tablesQuery(dslObject);
            return isDataSourceInResult(dslObject.getDataSource(), baseResult);
        } else if (EngineTypeEnum.BUSINESS_ENGINE.getEngine().equals(dslObject.getDataEngine())) {
            BaseResult baseResult = clickHouseEngineService.tablesQuery(dslObject);
            return isDataSourceInResult(dslObject.getDataSource(), baseResult);
        } else if (EngineTypeEnum.CACULATION_ENGINE.getEngine().equals(dslObject.getDataEngine())) {
            BaseResult baseResult = druidEngineService.tablesQuery(dslObject);
            return isDataSourceInResult(dslObject.getDataSource(), baseResult);
        }
        return false;
    }


    /**
     * 获取表中所有列
     *
     * @param dslObject
     * @return
     */
    private List getFields(DSLObject dslObject) {
        if (EngineTypeEnum.BUSINESS_ENGINE.getEngine().equalsIgnoreCase(dslObject.getDataEngine())) {
            BaseResult baseResult = clickHouseEngineService.schemaQuery(dslObject);
            return fieldsInDataSource(baseResult);
        } else if (EngineTypeEnum.CACULATION_ENGINE.getEngine().equalsIgnoreCase(dslObject.getDataEngine())) {
            BaseResult baseResult = druidEngineService.schemaQuery(dslObject);
            return fieldsInDataSource(baseResult);
        } else if (EngineTypeEnum.ANALYSIS_ENGINE.getEngine().equalsIgnoreCase(dslObject.getDataEngine())) {
            return new ArrayList();

        }
        throw new BusinessException(HttpStatus.SC_INTERNAL_SERVER_ERROR, ResultCodeEnum.UNKNOW_ERROR.getCode(),
                "field is invalid");
    }

    /**
     * 表中所有字段
     *
     * @param baseResult
     * @return
     */
    private List fieldsInDataSource(BaseResult baseResult) {
        if (baseResult.isSuccess()) {
            List<Map> data = (List<Map>) baseResult.getData();
            List fields = new ArrayList<>();
            data.forEach(o -> {
                        fields.add(o.get("name"));
                    }
            );
            return fields;
        } else {
            throw new BusinessException(HttpStatus.SC_INTERNAL_SERVER_ERROR, ResultCodeEnum.UNKNOW_ERROR.getCode(),
                    "get fields error: " + baseResult);
        }
    }

    /**
     * 判断表是否在对应dataEngine对应库
     *
     * @param dataSource
     * @param baseResult
     * @return
     */
    private boolean isDataSourceInResult(String dataSource, BaseResult baseResult) {
        if (baseResult.isSuccess()) {
            List<Map> data = (List<Map>) baseResult.getData();
            for (Map datum : data) {
                if (datum.get("name").equals(dataSource)) {
                    return true;
                }
            }
        } else {
            throw new BusinessException(HttpStatus.SC_INTERNAL_SERVER_ERROR, ResultCodeEnum.UNKNOW_ERROR.getCode(),
                    "get datasource error: " + baseResult);
        }
        return false;
    }

    /**
     * 时间粒度校验, 遵循ISO8601 durations 定义
     *
     * @param dslObject
     * @return
     */
    private boolean isValidGranularity(DSLObject dslObject) {
        String granularity = dslObject.getQuery().getGranularity();
        if (StrUtil.isBlank(granularity)) {
            return true;
        }
        if (periodOfP.matcher(granularity).find() || periodOfPT.matcher(granularity).find()) {
            return true;
        }
        return false;
    }

    /**
     * 校验range:
     * 1.是否属于{@link RangeTypeEnum}支持的类型
     *
     * @param dslObject
     * @return
     */
    private boolean isValidRange(DSLObject dslObject) {
        if (CollectionUtils.isEmpty(dslObject.getQuery().getRange())) {
            return true;
        }
        for (DSLObject.QueryBean.RangeBean rangeBean : dslObject.getQuery().getRange()) {
            if (RangeTypeEnum.getByType(rangeBean.getType()) == null) {
                return false;
            }
            checkField(dslObject, rangeBean.getFieldKey());
        }
        return true;
    }

    /**
     * 校验match:
     * 1.是否属于{@link MatchTypeEnum}限定类型
     * 2.不能以*开始、或$结尾
     *
     * @param dslObject
     * @return
     */
    private boolean isValidMatch(DSLObject dslObject) {
        if (CollectionUtils.isEmpty(dslObject.getQuery().getMatch())) {
            return true;
        }
        for (DSLObject.QueryBean.MatchBean matchBean : dslObject.getQuery().getMatch()) {
            if (MatchTypeEnum.getByType(matchBean.getType()) == null) {
                return false;
            }
            for (String fieldValue : matchBean.getFieldValues()) {
                if (fieldValue.startsWith("*") || fieldValue.endsWith("$")) {
                    throw new BusinessException(HttpStatus.SC_BAD_REQUEST, ResultCodeEnum.PARAM_SYNTAX_ERROR.getCode(), "Match fieldValues cannot startWith '*' or endWith '$'");
                }
            }
            checkField(dslObject, matchBean.getFieldKey());
        }
        return true;
    }

    /**
     * 校验sort:
     * 1.排序类型只能是DESC或ASC
     * 2.不限大小写
     *
     * @param dslObject
     * @return
     */
    private boolean isValidSort(DSLObject dslObject) {
        if (CollectionUtils.isEmpty(dslObject.getQuery().getSort())) {
            return true;
        }
        for (DSLObject.QueryBean.SortBean sortBean : dslObject.getQuery().getSort()) {
            if (!"DESC".equalsIgnoreCase(sortBean.getType()) && !"ASC".equalsIgnoreCase(sortBean.getType())) {
                throw new BusinessException(HttpStatus.SC_BAD_REQUEST, ResultCodeEnum.PARAM_SYNTAX_ERROR.getCode(), "sort type is illegal, must be asc or desc");
            }
            checkField(dslObject, sortBean.getFieldKey());
        }
        return true;
    }

    /**
     * 校验limit:
     * 1.limit范围[0,100000]
     *
     * @param limitStr
     * @return
     */
    private boolean isValidLimit(String limitStr) {
        if (StrUtil.isBlank(limitStr)) {
            return true;
        }
        int limit;
        String[] split = limitStr.split(",");
        if (split.length > 2) {
            throw new BusinessException(HttpStatus.SC_BAD_REQUEST, ResultCodeEnum.PARAM_SYNTAX_ERROR.getCode(), "Limit format error");
        } else if (split.length == 2) {
            limit = Integer.parseInt(split[0]) + Integer.parseInt(split[1]);
        } else {
            limit = Integer.parseInt(split[split.length - 1]);
        }
        if (0 > limit || maxCacheNum < limit) {
            throw new BusinessException(HttpStatus.SC_BAD_REQUEST, ResultCodeEnum.PARAM_SYNTAX_ERROR.getCode(), "Limit must be in the range [0, " + maxCacheNum + "]");
        }
        return true;
    }

    /**
     * 校验Interval:
     * 1.目前只支持between类型
     * 2.时间区间必须是 开始时间 < 结束时间
     *
     * @param dslObject
     */
    private boolean isValidIntervals(DSLObject dslObject) {
        if (CollectionUtils.isEmpty(dslObject.getQuery().getIntervals())) {
            return true;
        }
        for (DSLObject.QueryBean.IntervalsBean interval : dslObject.getQuery().getIntervals()) {
            if (Objects.isNull(IntervalTypeEnum.getByType(interval.getType().toUpperCase()))) {
                throw new BusinessException(HttpStatus.SC_BAD_REQUEST, ResultCodeEnum.PARAM_SYNTAX_ERROR.getCode(), "Interval type not support " + interval.getType());
            }
            if (interval.getFieldValues().size() != 2) {
                return false;
            }
            for (String fieldValue : interval.getFieldValues()) {
                if (!strFormatDateTime.matcher(fieldValue).find()) {
                    return false;
                }
            }

            try {
                DateTimeFormatter dateTimeFormatter = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");
                if (dateTimeFormatter.parseMillis(interval.getFieldValues().get(1)) - dateTimeFormatter.parseMillis(interval.getFieldValues().get(0)) < 0) {
                    throw new RuntimeException("Interval fieldValue should be [start, end]");
                }
            } catch (Exception e) {
                throw new BusinessException(HttpStatus.SC_BAD_REQUEST, ResultCodeEnum.PARAM_SYNTAX_ERROR.getCode(), e.getMessage());
            }
            checkField(dslObject, interval.getFieldKey());
        }
        return true;
    }

    /**
     * 校验dataEngine:
     * 1.是否属于{@link EngineTypeEnum}支持的类型
     *
     * @param dataEngine
     * @return
     */
    private boolean isValidEngine(String dataEngine) {
        if (StrUtil.isBlank(dataEngine)) {
            return false;
        }
        for (EngineTypeEnum engineTypeEnum : EngineTypeEnum.values()) {
            if (engineTypeEnum.getEngine().equals(dataEngine)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 校验field是否是相应表字段
     *
     * @param dslObject
     * @param fieldKey
     */
    private void checkField(DSLObject dslObject, String fieldKey) {
        //AnangoDB暂不校验
        if (EngineTypeEnum.ANALYSIS_ENGINE.getEngine().equalsIgnoreCase(dslObject.getDataEngine())) {
            return;
        }
        if (!getFields(dslObject).contains(fieldKey)) {
            throw new BusinessException(HttpStatus.SC_BAD_REQUEST, ResultCodeEnum.PARAM_SYNTAX_ERROR.getCode(), fieldKey + " is invalid");
        }
    }

}
