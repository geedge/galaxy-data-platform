package com.mesalab.hos.dao;

import com.mesalab.hos.entity.ResEntity;

/**
 * Created by wk on 2020/4/20.
 */

public interface BucketDao {

    ResEntity createDataTable(String tableName);

    ResEntity createIndexTable(String tableName);

    ResEntity deleteTable(String tableName);

    ResEntity truncateTable(String tableName);

    ResEntity changeDataTableTTL(int day, String tableName);

    ResEntity changeIndexTableTTL(int day, String tableName);

    ResEntity getTableListForNs(String user);

    Boolean ifExistTable(String tableName);

    long getBucketCreateTime(String bucket);
}
