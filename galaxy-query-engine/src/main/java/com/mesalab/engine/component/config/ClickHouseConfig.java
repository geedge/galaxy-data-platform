package com.mesalab.engine.component.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @Date: 2020-08-18 17:57
 * @Author : liuyongqiang
 * @ClassName : ClickHouseConfig
 * @Description : ClickHouseConfig
 */
@Data
@Component
public class ClickHouseConfig {

    @Value("${clickhouse.query.url}")
    private String queryUrl;

    @Value("${clickhouse.dbname}")
    private String dbName;

    @Value("${clickhouse.real.time.username}")
    private String realTimeUserName;

    @Value("${clickhouse.real.time.password}")
    private String realTimePassword;

    @Value("${clickhouse.long.term.username}")
    private String longTermUserName;

    @Value("${clickhouse.long.term.password}")
    private String longTermPassword;

    private int enableHttpCompression = 1;

    private String systemDBName = "system";

    private long socketTimeout;
}
