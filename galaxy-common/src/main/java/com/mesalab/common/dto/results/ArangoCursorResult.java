package com.mesalab.common.dto.results;

import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * @Date: 2020-07-27 10:13
 * @Author : liuyongqiang
 * @ClassName : ArangoCursorResult
 * @Description : Arango文档查询结果封装
 */
@Data
@AllArgsConstructor
public class ArangoCursorResult {


    private boolean hasMore;
    private boolean cached;
    private ExtraBean extra;
    private boolean error;
    private int code;
    private List<Map<String,Object>> result;

    @Data
    public static class ExtraBean {

        private StatsBean stats;
        private ProfileBean profile;
        private List<?> warnings;

        @Data
        public static class StatsBean {
            private int writesExecuted;
            private int writesIgnored;
            private int scannedFull;
            private int scannedIndex;
            private int filtered;
            private int httpRequests;
            private double executionTime;
            private int peakMemoryUsage;
        }

        @Data
        public static class ProfileBean {
            private double initializing;
            private double parsing;
            @SerializedName("optimizing ast")
            private double _$OptimizingAst41;
            @SerializedName("loading collections")
            private double _$LoadingCollections53;
            @SerializedName("instantiating plan")
            private double _$InstantiatingPlan79;
            @SerializedName("optimizing plan")
            private double _$OptimizingPlan284;
            private double executing;
            private double finalizing;

        }
    }
}
