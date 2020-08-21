package com.mesalab.engine.service;

import com.mesalab.common.dto.results.BaseResult;
import com.mesalab.common.exception.BusinessException;
import org.apache.avro.Schema;

import java.util.Map;

/**
 * @Date: 2020-08-18 14:14
 * @Author : liuyongqiang
 * @ClassName : MetadataService
 * @Description : 元数据管理服务
 */
public interface MetadataService {
    /**
     * 查询schema信息
     *
     * @param type
     * @param name
     * @return
     */
    BaseResult getSchemaInfo(String type, String name) throws BusinessException;

    /**
     * 根据表名获取partitionKey
     *
     * @param tableName
     * @return
     */
    String getPartitionKey(String tableName);

    /**
     * Schema Doc中: 通过key获取value
     *
     * @param tableName
     * @param key
     * @return
     */
    String getValueByKeyInSchemaDoc(String tableName, String key);

    /**
     * 通过数据源，获取所属数据库类型
     *
     * @param tableName
     * @return
     */
    String getDBTypeByTableName(String tableName);


    /**
     * 通过数据源，获取所对应数据库名称
     *
     * @param tableName
     * @return
     */
    String getDBNameByTableName(String tableName);


    /**
     * 获取所有已注册schema
     *
     * @return
     */
    Map<String, Schema> getAllSchema();
}
