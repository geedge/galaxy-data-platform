package com.mesalab.api.modules.network.controller;

import com.mesalab.api.common.base.BaseResult;
import com.mesalab.api.common.base.BaseResultGenerator;
import com.mesalab.api.common.enums.ResultCodeEnum;
import com.mesalab.api.common.enums.ResultStatusEnum;
import com.mesalab.api.common.exception.BusinessException;
import com.mesalab.api.modules.network.dsl.DSLObject;
import com.mesalab.api.modules.network.dsl.DSLValidate;
import com.mesalab.api.modules.network.service.NetworkMonitorService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;


/**
 * @author wangwei
 * @version 1.0
 * @date 2020/6/30 10:38 上午
 */
@Slf4j
@RestController
@RequestMapping(value = "/")
public class NetworkMonitorController {

    @Autowired
    private DSLValidate dslValidate;
    @Autowired
    private NetworkMonitorService networkMonitorService;
    @Autowired
    RestTemplate restTemplate;
    private static final String PROTOCOL = "protocol";

    @PostMapping(value = "/traffic/v1/", produces = "application/json")
    public BaseResult trafficDistribution(HttpServletRequest request, @Validated @RequestBody DSLObject dslObject) {
        log.info("流量分布接口, 参数: queryString is {},params is {}", request.getQueryString(), dslObject);
        dslValidate.executeValidate(dslObject);
        if (PROTOCOL.equalsIgnoreCase(request.getQueryString())) {
            return networkMonitorService.getTrafficDistributedInfo(dslObject);
        } else {
            throw new BusinessException(ResultStatusEnum.FAIL.getCode(), ResultCodeEnum.PARAM_SYNTAX_ERROR.getCode(), "Support protocol only", null);
        }
    }
}
