package com.mesalab.engine.service;

import com.mesalab.common.dto.params.NetworkParam;
import com.mesalab.common.dto.results.BaseResult;

/**
 * @author wangwei
 * @version 1.0
 * @date 2020/6/30 4:20 下午
 */
public interface NetworkService {

    BaseResult getTrafficDistributedInfo(NetworkParam param);
}
