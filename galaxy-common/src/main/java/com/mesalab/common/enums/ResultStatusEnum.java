package com.mesalab.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * BaseResult的http状态编码枚举
 */
@Getter
@AllArgsConstructor
public enum ResultStatusEnum {

    /**
     * SUCCESS: 200 成功
     * FAIL: 400 失败
     * NOT_FOUND： 404 不存在
     * SERVER_ERROR: 500 网络服务异常
     */
    SUCCESS(200, "成功"),
    FAIL(400, "失败"),
    NOT_FOUND(404, "不存在"),
    REQ_FORBIDDEN(403, "重复请求"),
    SERVER_ERROR(500, "服务异常");

    private int code;
    private String message;

}
