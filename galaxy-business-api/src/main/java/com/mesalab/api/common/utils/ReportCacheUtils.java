package com.mesalab.api.common.utils;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;

/**
 * @author wangwei
 * @version 1.0
 * @date 2020/6/5 2:04 下午
 */
@Slf4j
public class ReportCacheUtils {

    /**
     * 通过SQL生成查询唯一kEY
     *
     * @param sql
     * @param resultId
     * @return
     */
    public static String getQueryId(String resultId, String sql) {
        return DigestUtils.md5Hex(resultId + sql.trim());
    }

}
