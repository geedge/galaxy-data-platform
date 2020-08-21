package com.mesalab.engine.controller;

import com.mesalab.common.dto.results.BaseResult;
import com.mesalab.common.enums.QueryTypeEnum;
import com.mesalab.common.enums.ResultCodeEnum;
import com.mesalab.common.enums.ResultStatusEnum;
import com.mesalab.common.exception.BusinessException;
import com.mesalab.engine.service.KnowledgeService;
import com.zdjizhi.utils.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.EnumUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@Slf4j
@RestController
@RequestMapping("/knowledge")
public class KnowledgeController {

    @Autowired
    KnowledgeService knowledgeService;

    @GetMapping("/info")
    public String info() {
        return "You Konow, For Graph Search!";
    }

    /***
     * @Description
     * @Date 2020\7\16
     * @Param [request, dSLObject]
     * @return com.mesalab.common.base.BaseResult
     **/
    @PostMapping("/v1/")
    public BaseResult knowledgeQuery(HttpServletRequest request, @RequestBody Object object) {
        if (EnumUtils.isValidEnum(QueryTypeEnum.class,StringUtil.upperCase(request.getQueryString()))) {
            log.info("服务推荐接口, 参数: queryString is {},params is {}", request.getQueryString(), object);
            return knowledgeService.query(object);
        } else {
            log.error("服务推荐接口, 错误:queryString is {},params is {}", request.getQueryString(), object);
            throw new BusinessException(ResultStatusEnum.FAIL.getCode(), ResultCodeEnum.PARAM_SYNTAX_ERROR.getCode(), "Not Support", null);
        }
    }
}
