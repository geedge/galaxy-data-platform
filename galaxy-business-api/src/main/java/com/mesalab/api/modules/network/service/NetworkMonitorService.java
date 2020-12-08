package com.mesalab.api.modules.network.service;


import com.mesalab.api.common.base.BaseResult;
import com.mesalab.api.modules.network.dsl.DSLObject;

/**
 * @author wangwei
 * @version 1.0
 * @date 2020/6/30 4:20 下午
 */


public interface NetworkMonitorService {

    BaseResult getTrafficDistributedInfo(DSLObject dslObject);

}
