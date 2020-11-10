package com.mesalab.engine.controller;

import cn.hutool.core.util.StrUtil;
import com.mesalab.common.dto.results.BaseResult;
import com.mesalab.common.enums.ResultCodeEnum;
import com.mesalab.common.enums.ResultStatusEnum;
import com.mesalab.common.exception.BusinessException;
import com.mesalab.common.util.BaseResultUtil;
import com.mesalab.common.annotation.SysLog;
import com.mesalab.engine.service.MetadataService;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Date: 2020-08-17 15:57
 * @Author : liuyongqiang
 * @ClassName : MetadataController
 * @Description : 元数据相关接口
 */
@Slf4j
@RestController
@RequestMapping(value = "/metadata")
public class MetadataController {

    @Autowired
    MetadataService metadataService;

    @SysLog
    @GetMapping("/schema/v1/{type}/{name}")
    public BaseResult getSchema(@PathVariable(value = "type") String type, @PathVariable(value = "name") String name) {
        if(StrUtil.isBlank(type)||StrUtil.isBlank(name)){
            return BaseResultUtil.failure(ResultStatusEnum.NOT_FOUND.getCode(),ResultStatusEnum.NOT_FOUND.getMessage());
        }
        BaseResult baseResult;
        try {
            baseResult = metadataService.getSchemaInfo(type, name);
        } catch (BusinessException e) {
            return BaseResultUtil.failure(HttpStatus.SC_NOT_FOUND, ResultCodeEnum.PARAM_SYNTAX_ERROR.getCode(), e.getMessage());
        }
        return baseResult;
    }
}
