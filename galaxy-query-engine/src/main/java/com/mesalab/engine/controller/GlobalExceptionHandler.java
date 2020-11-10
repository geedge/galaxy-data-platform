package com.mesalab.engine.controller;

import com.mesalab.common.dto.results.BaseResult;
import com.mesalab.common.exception.BusinessException;
import com.mesalab.common.util.BaseResultUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletResponse;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {


    @ExceptionHandler(Exception.class)
    public BaseResult handleException(Exception e) {
        return BaseResultUtil.failure(e.getMessage());
    }


    @ExceptionHandler({BusinessException.class})
    public BaseResult handleBusinessException(BusinessException e, HttpServletResponse response) {
        response.setStatus(e.getErrorStatus());
        log.error("Execute Query Error: {}", e.getMessage() + ";" + (e.getCause() != null ? e.getCause().getMessage() : ""));
        return BaseResultUtil.failure(e.getErrorStatus(), e.getErrorCode(),
                (e.getMessage() != null ? e.getMessage() : e.getErrorMessage()) + "  " + (e.getCause() != null ? e.getCause().getMessage() : ""));
    }


}
