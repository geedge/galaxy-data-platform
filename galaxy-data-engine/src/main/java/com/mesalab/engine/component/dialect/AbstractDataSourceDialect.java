package com.mesalab.engine.component.dialect;

import com.mesalab.engine.component.bean.ApiQueryBean;
import com.mesalab.common.dto.results.BaseResult;
import com.mesalab.common.util.BaseResultUtil;
import com.zdjizhi.utils.StringUtil;
import lombok.Data;

@Data
public abstract class AbstractDataSourceDialect implements Dialect {

    public ApiQueryBean param;
    public BaseResult baseResult;

    public void initialize(ApiQueryBean param) {
        this.param = param;
        if (StringUtil.isNotEmpty(param.getDbQuerySource())) {
            param.setSql(param.getDbQuerySource().getSqlBody());
        } else {
            param.setSql(param.getQuery());
        }
        baseResult = BaseResultUtil.success();
    }

    /**
     * SQL查询语句转换为方言所认知的查询语句。
     *
     * @param sql
     * @return
     */
    abstract String convertQuery(String sql);


    /**
     * 基于RBO(rule-base optimizer)获取最优化的SQL
     *
     * @return
     */
    abstract String findBestQuery();


}
