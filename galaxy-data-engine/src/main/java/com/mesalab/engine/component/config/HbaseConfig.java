package com.mesalab.engine.component.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @Date: 2020-08-18 17:35
 * @Author : liuyongqiang
 * @ClassName : HbaseConfig
 * @Description : HbaseConfig
 */
@Data
@Component
public class HbaseConfig {
    @Value("${hbase.url}")
    private String url;
    @Value("${hbase.dbname}")
    private String dbName;
    @Value("${hbase.tableName}")
    private String tableName;
    @Value("${hbase.columnFamily}")
    private String columnFamily;
    @Value("${hbase.columnName}")
    private String columnName;
}
