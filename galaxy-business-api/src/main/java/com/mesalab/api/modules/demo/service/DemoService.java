package com.mesalab.api.modules.demo.service;

import com.mesalab.api.common.base.BaseResult;

/**
 * @Date: 2020-12-03 09:47
 * @Author : liuyongqiang
 * @ClassName : DemoService
 * @Description : 内部服务调用Demo
 */
public interface DemoService {

    String info();

    BaseResult sqlQuery();

}
