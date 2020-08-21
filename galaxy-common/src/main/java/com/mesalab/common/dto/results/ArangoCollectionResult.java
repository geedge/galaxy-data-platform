package com.mesalab.common.dto.results;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

/**
 * @Date: 2020-07-27 10:49
 * @Author : liuyongqiang
 * @ClassName : ArangoCollectionResult
 * @Description : Arango集合名称查询结果封装
 */
@Data
@AllArgsConstructor
public class ArangoCollectionResult {

    private boolean error;
    private int code;
    private List<ResultBean> result;

    @Data
    public static class ResultBean {
        private String id;
        private String name;
        private int status;
        private int type;
        private boolean isSystem;
        private String globallyUniqueId;
    }
}
