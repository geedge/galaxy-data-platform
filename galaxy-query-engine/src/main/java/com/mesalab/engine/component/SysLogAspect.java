package com.mesalab.engine.component;

import com.google.gson.Gson;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

/**
 * @Date: 2020-07-30 11:54
 * @Author : liuyongqiang
 * @ClassName : SysLogAspect
 * @Description : 切面实现日志记录
 */
@Aspect
@Component
public class SysLogAspect {

    @Autowired
    IDGenerator idGenerator;

    @Autowired
    LoggerCache loggerCache;

    @Pointcut("@annotation(com.mesalab.common.annotation.SysLog)")
    public void log() {
    }


    /**
     * 加入注解自动记录方法日志
     *
     * @param joinPoint
     * @return
     * @throws Throwable
     */
    @Around(value = "log()")
    public Object logAround(ProceedingJoinPoint joinPoint) throws Throwable {
        // 获取执行方法的类的名称（包名加类名）
        String className = joinPoint.getTarget().getClass().getName();
        // 获取实例和方法
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        // 从缓存中获取日志实例
        Logger log = loggerCache.getLoggerByClassName(className);
        // 记录日志
        long logId = idGenerator.snowflakeId();
        log.info("["+logId+"]"+" - Execute:"+className + "." + method.getName());
        Object[] args = joinPoint.getArgs();
        log.info("["+logId+"]"+" - Params:" + new Gson().toJson(args));
        // 执行方法获取返回值
        Object proceed = joinPoint.proceed();
        // 记录日志
        log.info("["+logId+"]"+" - Returns:" + new Gson().toJson(proceed));
        // 返回
        return proceed;
    }
}
