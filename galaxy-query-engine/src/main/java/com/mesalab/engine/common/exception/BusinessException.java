package com.mesalab.engine.common.exception;

import com.mesalab.engine.common.enums.ResultCodeEnum;
import com.mesalab.engine.common.enums.ResultStatusEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * 业务异常
 *
 * @author dazzlzy
 * @date 2018/3/22
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class BusinessException extends RuntimeException {

    /**
     * 返回HTTP状态码
     */
    private int errorStatus = ResultStatusEnum.SERVER_ERROR.getCode();

    /**
     * 内部执行错误码
     */
    private String errorCode = ResultCodeEnum.UNKNOW_ERROR.getCode();

    /**
     * 异常信息
     */
    private String errorMessage;


    public BusinessException(String errorMessage) {
        this.errorMessage = errorMessage;
    }


    public BusinessException(String errorMessage, Throwable e) {
        super(errorMessage, e);
    }

    public BusinessException(int errorStatus, String errorCode, String errorMessage, Throwable e) {
        super(errorMessage, e);
        this.errorStatus = errorStatus;
        this.errorCode = errorCode;
    }
}
