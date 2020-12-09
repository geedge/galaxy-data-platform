package com.mesalab.report.util;

import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by wk1 on 2019/5/23.
 */


public class Logs {
    private static Logger logger = LoggerFactory.getLogger(Logs.class);

    //debug方法，其他error、warn类似定义
    public static void debug(Object obj) {
        //如果更改日志出工工具，只需修改该行代码即可。如：可改为 System.out.println(obj);
        logger.debug(new Gson().toJson(obj));
    }

    public static void error(Object obj) {
        //如果更改日志出工工具，只需修改该行代码即可。如：可改为 System.out.println(obj);
        logger.error(new Gson().toJson(obj));
    }

    public static void info(Object obj) {
        //如果更改日志出工工具，只需修改该行代码即可。如：可改为 System.out.println(obj);
        logger.info(new Gson().toJson(obj));
    }
}
