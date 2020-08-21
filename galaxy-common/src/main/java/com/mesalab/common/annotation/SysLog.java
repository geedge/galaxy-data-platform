package com.mesalab.common.annotation;

import java.lang.annotation.*;

/**
 * @Date: 2020-07-30 11:52
 * @Author : liuyongqiang
 * @ClassName : SysLog
 * @Description : 日志注解
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface SysLog {
}
