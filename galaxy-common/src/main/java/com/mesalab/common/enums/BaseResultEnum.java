package com.mesalab.common.enums;

/**
 * @Date: 2020-08-05 14:18
 * @Author : liuyongqiang
 * @ClassName : BaseResultEnum
 * @Description : BaseResults枚举类
 */
public enum BaseResultEnum {

    SUCCESS(200,"成功"),
    BAD_REQUEST(400 ,"错误请求"),
    FORBID_REQUEST(403 ,"禁止请求"),
    NOT_FOUND(404 ,"未找到"),
    UNKNOWN_ERROR(500 ,"未知错误");

    private Integer code;
    private String message;

    BaseResultEnum(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    public Integer getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
