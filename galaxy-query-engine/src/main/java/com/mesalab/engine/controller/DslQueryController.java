package com.mesalab.engine.controller;

import com.mesalab.engine.common.exception.BusinessException;
import com.mesalab.engine.common.pojo.BaseResult;
import com.mesalab.engine.common.pojo.DSLObject;
import com.mesalab.engine.common.util.BaseResultUtil;
import com.mesalab.engine.component.DSLRouter;
import com.mesalab.engine.component.DSLVerification;
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
    protected DSLVerification verification;

    @PostMapping("query")
    public BaseResult query(@RequestBody DSLObject dslObject) {
        BaseResult baseResult;
        try {
            verification.executeValidate(dslObject);
            baseResult = dslRouter.executeRouter(dslObject);
        } catch (BusinessException e) {
            return BaseResultUtil.failure(
                    e.getErrorStatus(),
                    e.getErrorCode(),
                    e.getErrorMessage());
        }
        return baseResult;
    }
}
