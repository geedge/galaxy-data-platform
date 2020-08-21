package com.mesalab.engine.component.dialect;

import com.mesalab.engine.component.bean.ApiQueryBean;
import com.mesalab.common.dto.results.BaseResult;
import com.zdjizhi.utils.StringUtil;
import lombok.Data;

@Data
public abstract class AbstractEngineDialect implements Dialect {
    public ApiQueryBean param;
    public BaseResult baseResult;

    abstract void executeUDF();

    public void initialize(ApiQueryBean param, BaseResult baseResult) {
        this.param = param;
        if (StringUtil.isNotEmpty(param.getEngineQuerySource())) {
            param.setQuery(param.getEngineQuerySource().getSqlBody());
        }
        this.baseResult = baseResult;
    }
}
