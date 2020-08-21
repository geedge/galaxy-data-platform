package com.mesalab.engine.component;

import com.mesalab.common.model.DSLObject;
import com.mesalab.common.dto.results.BaseResult;
import com.mesalab.common.enums.EngineTypeEnum;
import com.mesalab.common.exception.BusinessException;
import com.mesalab.engine.service.ArangoEngineService;
import com.mesalab.engine.service.ClickHouseEngineService;
import com.mesalab.engine.service.DruidEngineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @Date: 2020-07-20 18:11
 * @Author : liuyongqiang
 * @ClassName : DSLRouter
 * @Description : DSL路由执行,负责调用查询引擎执行查询
 */
@Component
public class DSLRouter {

    @Autowired
    private ArangoEngineService arangoEngineService;

    @Autowired
    private ClickHouseEngineService clickHouseEngineService;

    @Autowired
    private DruidEngineService druidEngineService;

    /**
     * @Description: 查询引擎路由
     * @Author: liuyongqiang
     * @Date: 2020/7/22 15:59
     * @param dslObject:
     * @return: com.mesalab.common.base.BaseResult
     **/
    public BaseResult router(DSLObject dslObject) throws BusinessException {
        switch (EngineTypeEnum.getByEngine(dslObject.getDataEngine())){
            case ANALYSIS_ENGINE:
                return arangoEngineService.dslQuery(dslObject);
            case CACULATION_ENGINE:
                return druidEngineService.dslQuery(dslObject);
            case BUSINESS_ENGINE:
                return clickHouseEngineService.dslQuery(dslObject);
            default:
                return null;
        }
    }

}
