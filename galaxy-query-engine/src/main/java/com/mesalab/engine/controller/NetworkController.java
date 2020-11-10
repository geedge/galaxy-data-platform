package com.mesalab.engine.controller;

import com.mesalab.common.dto.params.NetworkParam;
import com.mesalab.common.dto.results.BaseResult;
import com.mesalab.common.enums.ResultCodeEnum;
import com.mesalab.common.enums.ResultStatusEnum;
import com.mesalab.common.exception.BusinessException;
import com.mesalab.engine.service.NetworkService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * @author wangwei
 * @version 1.0
 * @date 2020/6/30 10:38 上午
 */
@Slf4j
@RestController
@RequestMapping("/")
public class NetworkController {

    @Autowired
    private NetworkService networkService;
    private static final String PROTOCOL = "protocol";

    @PostMapping("/traffic/v1/")
    public BaseResult trafficDistribution(HttpServletRequest request, @Validated @RequestBody NetworkParam param) {
        if (PROTOCOL.equalsIgnoreCase(request.getQueryString())) {
            log.info("流量分布接口, 参数: queryString is {},params is {}", request.getQueryString(), param);
            return networkService.getTrafficDistributedInfo(param);
        } else {
            throw new BusinessException(ResultStatusEnum.FAIL.getCode(), ResultCodeEnum.PARAM_SYNTAX_ERROR.getCode(), "Support protocol only", null);
        }
    }
}
