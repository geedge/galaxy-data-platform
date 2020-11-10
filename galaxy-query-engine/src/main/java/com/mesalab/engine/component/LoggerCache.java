package com.mesalab.engine.component;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.HashMap;

/**
 * @Date: 2020-07-30 11:53
 * @Author : liuyongqiang
 * @ClassName : LoggerCache
 * @Description : 日志实例缓存
 */
@Component
public class LoggerCache {

    /**
     * 日志实例记录在内存中
     */
    private HashMap<String, Logger> LOGERS = new HashMap<String, Logger>();

    /**
     * 根据类名获取缓存的日志实例
     * @param className 包名加类名 this.getClass().getName();
     * @return
     */
    public Logger getLoggerByClassName(String className) {
        // 从静态map中获取日志实例
        Logger logger = LOGERS.get(className);
        // 如果没取到
        if (logger == null) {
            // 创建一个日志实例
            logger = LoggerFactory.getLogger(className);
            // 加入到静态map中
            LOGERS.put(className, logger);
        }
        // 返回
        return logger;
    }
}
