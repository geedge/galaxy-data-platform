package com.mesalab.engine.component;


import com.mesalab.engine.common.enums.EngineTypeEnum;
import com.mesalab.engine.common.exception.BusinessException;
import com.mesalab.engine.common.pojo.BaseResult;
import com.mesalab.engine.common.pojo.DSLObject;
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
    private ClickHouseEngineService clickHouseEngineService;

    @Autowired
    private DruidEngineService druidEngineService;

    /**
     * @param dslObject:
     * @Description: 查询引擎路由
     * @Author: liuyongqiang
     * @Date: 2020/7/22 15:59
     * @return: com.mesalab.common.base.BaseResult
     **/
    public BaseResult executeRouter(DSLObject dslObject) throws BusinessException {
        switch (EngineTypeEnum.getByEngine(dslObject.getDataEngine())) {
            case CACULATION_ENGINE:
                return druidEngineService.dslQuery(dslObject);
            case BUSINESS_ENGINE:
                return clickHouseEngineService.dslQuery(dslObject);
            default:
                return null;
        }
    }

}
