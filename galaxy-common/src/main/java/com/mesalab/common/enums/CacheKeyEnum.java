package com.mesalab.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @Date: 2020-07-22 14:05
 * @Author : liuyongqiang
 * @ClassName : CacheKeyEnum
 * @Description : 缓存Key定义
 */
@Getter
@AllArgsConstructor
public enum CacheKeyEnum {

    ARONGO_JWT_KEY("arongo_jwt_key");

    private String key;

}
