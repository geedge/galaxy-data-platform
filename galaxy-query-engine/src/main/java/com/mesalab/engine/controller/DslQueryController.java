package com.mesalab.engine.controller;

import com.mesalab.common.model.DSLObject;
import com.mesalab.common.dto.results.BaseResult;
import com.mesalab.common.exception.BusinessException;
import com.mesalab.common.util.BaseResultUtil;
import com.mesalab.common.annotation.SysLog;
import com.mesalab.engine.component.DSLRouter;
import com.mesalab.engine.component.DSLValidate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Date: 2020-08-17 10:55
 * @Author : liuyongqiang
 * @ClassName : DslQueryController
 * @Description : 基于DSL语言的单表数据查询
 */
@Slf4j
@RestController
@RequestMapping("/v1/dsl/")
public class DslQueryController {

    @Autowired
    protected DSLRouter dslRouter;

    @Autowired
    protected DSLValidate dslValidate;

    @PostMapping("query")
    @SysLog
    public BaseResult query(@RequestBody DSLObject dslObject){
        BaseResult baseResult;
        try {
            dslValidate.executeValidate(dslObject);
            baseResult = dslRouter.router(dslObject);
        } catch (BusinessException e) {
            return BaseResultUtil.failure(
                    e.getErrorStatus(),
                    e.getErrorCode(),
                    e.getErrorMessage());
        }
        return baseResult;
    }
}
