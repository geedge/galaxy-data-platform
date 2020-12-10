package com.mesalab.hos.dao.Impl;

import com.mesalab.hos.config.HbaseProperties;
import com.mesalab.hos.dao.BucketDao;
import com.mesalab.hos.entity.ResEntity;
import com.mesalab.hos.entity.bucketlist.Bucket;
import com.mesalab.hos.entity.bucketlist.BucketListResponse;
import com.mesalab.hos.entity.bucketlist.BucketOwner;
import com.mesalab.hos.util.PublicUtil;
import org.apache.hadoop.hbase.Cell;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.TableNotFoundException;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.io.compress.Compression;
import org.apache.hadoop.hbase.util.Bytes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.DigestUtils;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by wk1 on 2020/7/14.
 */
@Component
public class BucketDaoImpl implements BucketDao {

    private final org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(this.getClass());
    @Autowired
    private Connection hbaseConnection;
    @Autowired
    HbaseProperties hbaseProperties;

    @Override
    public ResEntity createDataTable(String tableName) {
        ResEntity resEntity = new ResEntity();
        resEntity.setCode("");
        resEntity.setIsSuccess(false);
        try (Admin admin = hbaseConnection.getAdmin()) {
            List<ColumnFamilyDescriptor> families = new ArrayList<>();
            ColumnFamilyDescriptor dataColumnFamily = ColumnFamilyDescriptorBuilder.newBuilder("data".getBytes())
                    .setValue(ColumnFamilyDescriptorBuilder.IS_MOB, "true")
                    .setValue(ColumnFamilyDescriptorBuilder.MOB_THRESHOLD, 0 + "")
                    .setCompressionType(Compression.Algorithm.GZ)
                    .build();
            ColumnFamilyDescriptor metaColumnFamily = ColumnFamilyDescriptorBuilder.newBuilder("meta".getBytes())
                    .setCompressionType(Compression.Algorithm.GZ)
                    .build();
            families.add(dataColumnFamily);
            families.add(metaColumnFamily);
            TableDescriptor tableDescriptor = TableDescriptorBuilder.newBuilder(TableName.valueOf(tableName))
                    .setColumnFamilies(families)
                    .build();
            if (StringUtils.isEmpty(hbaseProperties.getRegion_start_key())) {
                admin.createTable(tableDescriptor);
            } else {
                byte[][] regionParts = PublicUtil.stringToByteArray(hbaseProperties.getRegion_start_key());
                admin.createTable(tableDescriptor, regionParts);
            }
            resEntity.setIsSuccess(true);
        } catch (Exception e) {
            logger.error(e.toString());
            resEntity.setCode("InternalError");
        }
        return resEntity;
    }

    @Override
    public ResEntity createIndexTable(String tableName) {
        ResEntity resEntity = new ResEntity();
        resEntity.setCode("");
        resEntity.setIsSuccess(false);
        try (Admin admin = hbaseConnection.getAdmin()) {
            List<ColumnFamilyDescriptor> indexTableFamilies = new ArrayList<>();
            ColumnFamilyDescriptor indexTableColumnFamily = ColumnFamilyDescriptorBuilder.newBuilder("meta".getBytes())
                    .setCompressionType(Compression.Algorithm.GZ)
                    .build();
            indexTableFamilies.add(indexTableColumnFamily);
            TableDescriptor indexTableDescriptor = TableDescriptorBuilder.newBuilder(TableName.valueOf(tableName))
                    .setColumnFamilies(indexTableFamilies)
                    .build();
            if (StringUtils.isEmpty(hbaseProperties.getRegion_start_key())) {
                admin.createTable(indexTableDescriptor);
            } else {
                byte[][] regionParts = PublicUtil.stringToByteArray(hbaseProperties.getRegion_start_key());
                admin.createTable(indexTableDescriptor, regionParts);
            }
            resEntity.setIsSuccess(true);
        } catch (Exception e) {
            logger.error("create indextable "+tableName+"error "+e.toString());
            resEntity.setCode("InternalError");
        }
        return resEntity;
    }

    @Override
    public ResEntity deleteTable(String tableName) {
        ResEntity resEntity = new ResEntity();
        resEntity.setCode("");
        resEntity.setIsSuccess(false);
        try (Admin admin = hbaseConnection.getAdmin()) {
            if (admin.tableExists(TableName.valueOf(tableName))){
                if (admin.isTableEnabled(TableName.valueOf(tableName))) {
                    admin.disableTable(TableName.valueOf(tableName));
                }
                admin.deleteTable(TableName.valueOf(tableName));
            }
            resEntity.setIsSuccess(true);
        } catch (TableNotFoundException e) {
            logger.error(e.toString());
            resEntity.setCode("NoSuchBucket");
        } catch (Exception e) {
            logger.error(e.toString());
            resEntity.setCode("InternalError");
        }
        return resEntity;
    }

    @Override
    public ResEntity truncateTable(String tableName) {
        ResEntity resEntity = new ResEntity();
        resEntity.setCode("");
        resEntity.setIsSuccess(false);
        try (Admin admin = hbaseConnection.getAdmin()) {
            if (admin.isTableEnabled(TableName.valueOf(tableName))) {
                admin.disableTable(TableName.valueOf(tableName));
            }
            admin.truncateTable(TableName.valueOf(tableName), true);
            resEntity.setIsSuccess(true);
        } catch (TableNotFoundException e) {
            logger.error(e.toString());
            resEntity.setCode("NoSuchBucket");
        } catch (Exception e) {
            logger.error(e.toString());
            resEntity.setCode("InternalError");
        }
        return resEntity;
    }

    @Override
    public ResEntity changeDataTableTTL(int day, String tableName) {
        ResEntity resEntity = new ResEntity();
        resEntity.setCode("");
        resEntity.setIsSuccess(false);
        try (Admin admin = hbaseConnection.getAdmin()) {
            TableDescriptor descriptor = admin.getDescriptor(TableName.valueOf(tableName));
            ColumnFamilyDescriptor columnFamily = descriptor.getColumnFamily("data".getBytes());
            long mobThreshold = columnFamily.getMobThreshold();
            ColumnFamilyDescriptor columnFamilyDescriptor = ColumnFamilyDescriptorBuilder.newBuilder("data".getBytes())
                    .setValue(ColumnFamilyDescriptorBuilder.TTL, Integer.toString(day * 24 * 60 * 60))
                    .setValue(ColumnFamilyDescriptorBuilder.IS_MOB, "true")
                    .setValue(ColumnFamilyDescriptorBuilder.MOB_THRESHOLD, mobThreshold + "")
                    .setCompressionType(Compression.Algorithm.GZ)
                    .build();
            admin.modifyColumnFamily(TableName.valueOf(tableName), columnFamilyDescriptor);
            columnFamilyDescriptor = ColumnFamilyDescriptorBuilder.newBuilder("meta".getBytes())
                    .setValue(ColumnFamilyDescriptorBuilder.TTL, Integer.toString(day * 24 * 60 * 60))
                    .setCompressionType(Compression.Algorithm.GZ)
                    .build();
            admin.modifyColumnFamily(TableName.valueOf(tableName), columnFamilyDescriptor);
            resEntity.setIsSuccess(true);
        } catch (TableNotFoundException e) {
            logger.error(e.toString());
            resEntity.setCode("NoSuchBucket");
        } catch (Exception e) {
            logger.error(e.toString());
            resEntity.setCode("InternalError");
        }
        return resEntity;
    }

    @Override
    public ResEntity changeIndexTableTTL(int day, String tableName) {
        ResEntity resEntity = new ResEntity();
        resEntity.setCode("");
        resEntity.setIsSuccess(false);
        try (Admin admin = hbaseConnection.getAdmin()) {
            ColumnFamilyDescriptor columnFamilyDescriptor = ColumnFamilyDescriptorBuilder.newBuilder("meta".getBytes())
                    .setValue(ColumnFamilyDescriptorBuilder.TTL, Integer.toString(day * 24 * 60 * 60))
                    .setCompressionType(Compression.Algorithm.GZ)
                    .build();
            admin.modifyColumnFamily(TableName.valueOf(tableName), columnFamilyDescriptor);
            resEntity.setIsSuccess(true);
        } catch (TableNotFoundException e) {
            logger.error("change ttl index_table"+tableName+"error +"+e.toString());
            resEntity.setCode("NoSuchBucket");
        } catch (Exception e) {
            logger.error("change ttl index_table"+tableName+"error +"+e.toString());
            resEntity.setCode("InternalError");
        }
        return resEntity;
    }

    @Override
    public ResEntity getTableListForNs(String user) {
        ResEntity resEntity = new ResEntity();
        resEntity.setCode("");
        resEntity.setIsSuccess(false);
        BucketListResponse bucketListResponse = new BucketListResponse();
        try (Admin admin = hbaseConnection.getAdmin()) {
            TableName[] tableNames = admin.listTableNamesByNamespace(user);
            ArrayList<Bucket> bucketList = new ArrayList<>();
            for (TableName tableName : tableNames) {
                String table = Bytes.toString(tableName.getName());
                if (table.contains(":")) {
                    table = table.split(":")[1];
                }
                if (!table.startsWith(hbaseProperties.getTime_index_table_prefix()) && !table.startsWith(hbaseProperties.getFilename_index_table_prefix()) && !table.startsWith(hbaseProperties.getPartfile_index_table_prefix())) {
                    bucketList.add(new Bucket(PublicUtil.getUTCTime(getBucketCreateTime(Bytes.toString(tableName.getName()))), table));
                }
            }
            bucketListResponse.setBucket(bucketList);
            bucketListResponse.setOwner(new BucketOwner(user, DigestUtils.md5DigestAsHex(user.getBytes())));
            resEntity.setReturnEntity(bucketListResponse);
            resEntity.setIsSuccess(true);
        } catch (Exception e) {
            logger.error(e.toString());
            resEntity.setCode("InternalError");
        }
        return resEntity;
    }


    @Override
    public Boolean ifExistTable(String tableName) {
        boolean ifExistTable = false;
        try (Admin admin = hbaseConnection.getAdmin()) {
            ifExistTable = admin.tableExists(TableName.valueOf(tableName));
        } catch (Exception e) {
            logger.error(e.toString());
        }
        return ifExistTable;
    }

    @Override
    public long getBucketCreateTime(String bucket) {
        long timestamp = 0;
        try (Table table = hbaseConnection.getTable(TableName.valueOf("hbase:meta"))) {
            Get get = new Get(Bytes.toBytes(bucket));
            Result result = table.get(get);
            Cell cell = result.getColumnLatestCell(Bytes.toBytes("table"), Bytes.toBytes("state"));
            timestamp = cell.getTimestamp();
        } catch (IOException e) {
            logger.error(e.toString());
        }
        return timestamp;
    }
}
