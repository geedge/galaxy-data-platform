package com.mesalab.hos.service.impl;

import com.mesalab.hos.config.HbaseProperties;
import com.mesalab.hos.dao.BucketDao;
import com.mesalab.hos.entity.ResEntity;
import com.mesalab.hos.service.BucketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

/**
 * Created by wk1 on 2020/7/14.
 */
@Service
@ConditionalOnProperty(value = "hos.simple", havingValue = "1")
public class BucketServiceImpl implements BucketService {
    private final org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(this.getClass());

    @Autowired
    private BucketDao bucketDao;
    @Autowired
    HbaseProperties hbaseProperties;

    @Override
    public ResEntity changeTTL(int day, String user, String bucket) {
        ResEntity resEntity = new ResEntity();
        resEntity.setCode("");
        resEntity.setIsSuccess(false);
        String tableName = user + ":" + bucket;
        String indexTimeTableName = user + ":" + hbaseProperties.getTime_index_table_prefix() + bucket;
        String indexFilenameTableName = user + ":" + hbaseProperties.getFilename_index_table_prefix() + bucket;
        String partFileTableName = user + ":" + hbaseProperties.getPartfile_index_table_prefix() + bucket;
        try {
            if (!bucketDao.ifExistTable(tableName) || !bucketDao.ifExistTable(indexTimeTableName) || !bucketDao.ifExistTable(indexFilenameTableName) || !bucketDao.ifExistTable(partFileTableName)) {
                return new ResEntity("NoSuchBucket", false, "");
            }
            resEntity = bucketDao.changeDataTableTTL(day, tableName);
            if (!resEntity.getIsSuccess()) {
                return resEntity;
            }
            resEntity = bucketDao.changeIndexTableTTL(day, indexTimeTableName);
            if (!resEntity.getIsSuccess()) {
                return resEntity;
            }
            resEntity = bucketDao.changeIndexTableTTL(day, indexFilenameTableName);
            if (!resEntity.getIsSuccess()) {
                return resEntity;
            }
            resEntity = bucketDao.changeIndexTableTTL(day, partFileTableName);
        } catch (Exception e) {
            logger.error(e.toString());
            resEntity.setIsSuccess(false);
            resEntity.setCode("InternalError");
        }
        return resEntity;
    }

    @Override
    public ResEntity truncateData(String user, String bucket) {
        ResEntity resEntity = new ResEntity();
        resEntity.setCode("");
        resEntity.setIsSuccess(false);
        String tableName = user + ":" + bucket;
        String indexTimeTableName = user + ":" + hbaseProperties.getTime_index_table_prefix() + bucket;
        String indexFilenameTableName = user + ":" + hbaseProperties.getFilename_index_table_prefix() + bucket;
        String partFileTableName = user + ":" + hbaseProperties.getPartfile_index_table_prefix() + bucket;
        try {
            if (!bucketDao.ifExistTable(tableName) || !bucketDao.ifExistTable(indexTimeTableName) || !bucketDao.ifExistTable(indexFilenameTableName) || !bucketDao.ifExistTable(partFileTableName)) {
                return new ResEntity("NoSuchBucket", false, "");
            }
            resEntity = bucketDao.truncateTable(tableName);
            if (!resEntity.getIsSuccess()) {
                return resEntity;
            }
            resEntity = bucketDao.truncateTable(indexTimeTableName);
            if (!resEntity.getIsSuccess()) {
                return resEntity;
            }
            resEntity = bucketDao.truncateTable(indexFilenameTableName);
            if (!resEntity.getIsSuccess()) {
                return resEntity;
            }
            resEntity = bucketDao.truncateTable(partFileTableName);
        } catch (Exception e) {
            logger.error(e.toString());
            resEntity.setIsSuccess(false);
            resEntity.setCode("InternalError");
        }
        return resEntity;
    }

    @Override
    public ResEntity getBucketList(String user) {
        return bucketDao.getTableListForNs(user);
    }

    @Override
    public ResEntity createBucket(String user, String bucket) {
        ResEntity resEntity = new ResEntity();
        resEntity.setCode("");
        resEntity.setIsSuccess(false);
        String tableName = user + ":" + bucket;
        String indexTimeTableName = user + ":" + hbaseProperties.getTime_index_table_prefix() + bucket;
        String indexFilenameTableName = user + ":" + hbaseProperties.getFilename_index_table_prefix() + bucket;
        String partFileTableName = user + ":" + hbaseProperties.getPartfile_index_table_prefix() + bucket;
        try {
            if (bucketDao.ifExistTable(tableName) || bucketDao.ifExistTable(indexTimeTableName) || bucketDao.ifExistTable(indexFilenameTableName) || bucketDao.ifExistTable(partFileTableName)) {
                return new ResEntity("BucketAlreadyOwnedByYou", false, "");
            }
            resEntity = bucketDao.createDataTable(tableName);
            if (resEntity.getIsSuccess()) {
                resEntity = bucketDao.createIndexTable(indexTimeTableName);
                if (resEntity.getIsSuccess()) {
                    resEntity = bucketDao.createIndexTable(indexFilenameTableName);
                    if (resEntity.getIsSuccess()) {
                        resEntity = bucketDao.createIndexTable(partFileTableName);
                        if (!resEntity.getIsSuccess()) {
                            bucketDao.deleteTable(tableName);
                            bucketDao.deleteTable(indexTimeTableName);
                            bucketDao.deleteTable(indexFilenameTableName);
                        }
                    } else {
                        bucketDao.deleteTable(tableName);
                        bucketDao.deleteTable(indexTimeTableName);
                    }
                } else {
                    bucketDao.deleteTable(tableName);
                }
            }
        } catch (Exception e) {
            logger.error(e.toString());
            resEntity.setIsSuccess(false);
            resEntity.setCode("InternalError");
        }
        return resEntity;
    }

    @Override
    public ResEntity deleteBucket(String user, String bucket) {
        ResEntity resEntity = new ResEntity();
        resEntity.setCode("");
        resEntity.setIsSuccess(false);
        String tableName = user + ":" + bucket;
        String indexTimeTableName = user + ":" + hbaseProperties.getTime_index_table_prefix() + bucket;
        String indexFilenameTableName = user + ":" + hbaseProperties.getFilename_index_table_prefix() + bucket;
        String partFileTableName = user + ":" + hbaseProperties.getPartfile_index_table_prefix() + bucket;
        try {
                resEntity = bucketDao.deleteTable(tableName);
                if (!resEntity.getIsSuccess()) {
                    return resEntity;
                }
                resEntity = bucketDao.deleteTable(indexTimeTableName);
                if (!resEntity.getIsSuccess()) {
                    return resEntity;
                }
                resEntity = bucketDao.deleteTable(indexFilenameTableName);
                if (!resEntity.getIsSuccess()) {
                    return resEntity;
                }
                resEntity = bucketDao.deleteTable(partFileTableName);
        } catch (Exception e) {
            logger.error(e.toString());
            resEntity.setIsSuccess(false);
            resEntity.setCode("InternalError");
        }
        return resEntity;
    }


}