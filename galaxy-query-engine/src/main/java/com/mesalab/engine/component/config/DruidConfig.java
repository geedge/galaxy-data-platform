package com.mesalab.engine.component.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @Date: 2020-08-18 17:57
 * @Author : liuyongqiang
 * @ClassName : DruidConfig
 * @Description : DruidConfig
 */
@Data
@Component
public class DruidConfig {

    @Value("${druid.query.url}")
    private String queryUrl;
    @Value("${druid.dbname}")
    private String dbname;
    @Value("${druid.sqlTimeZone}")
    private String sqlTimeZone;

    private String skipEmptyBuckets = "true";
    private long timeout = 600000;

}
