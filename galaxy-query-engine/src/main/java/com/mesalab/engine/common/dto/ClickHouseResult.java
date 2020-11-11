package com.mesalab.engine.common.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * @Date: 2020-07-23 16:21
 * @Author : liuyongqiang
 * @ClassName : ClickHouseResult
 * @Description : ClickHouse查询结果封装
 */
@Data
@AllArgsConstructor
public class ClickHouseResult implements Serializable {

    private int rows;
    private List<MetaBean> meta;
    private List<Map<String, Object>> data;
    private Map<String, Object> statistics;

    @Data
    public static class MetaBean {
        private String name;
        private String type;
    }

}
