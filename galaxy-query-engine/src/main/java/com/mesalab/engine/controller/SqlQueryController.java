package com.mesalab.engine.controller;

import com.mesalab.engine.component.bean.ApiQueryBean;
import com.mesalab.common.dto.results.BaseResult;
import com.mesalab.common.util.BaseResultUtil;
import com.mesalab.common.annotation.SysLog;
import com.mesalab.engine.service.SqlEngineService;
import com.zdjizhi.utils.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Date: 2020-08-18 09:34
 * @Author : liuyongqiang
 * @ClassName : APIController
 * @Description : 基于SQL语言的动态查询
 */
@Slf4j
@RestController
@RequestMapping(value = "/")
public class SqlQueryController {

    @Autowired
    SqlEngineService sqlEngineService;

    //基于SQL的公共查询服务
    @SysLog
    @RequestMapping("/")
    public BaseResult queryByGet(ApiQueryBean apiParam) {
        if (StringUtil.isBlank(apiParam.getQuery())) {
            return BaseResultUtil.success4Message("ok");
        }
        return sqlEngineService.executeQuery(apiParam);
    }

}
