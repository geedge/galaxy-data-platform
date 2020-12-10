package com.mesalab.hos.dao.Impl;

import com.mesalab.hos.config.HosProperties;
import com.mesalab.hos.dao.IndexFileNameDao;
import com.mesalab.hos.entity.ColumnEntity;
import com.mesalab.hos.entity.select.Content;
import org.apache.hadoop.hbase.CompareOperator;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.TableNotFoundException;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.filter.BinaryComparator;
import org.apache.hadoop.hbase.filter.FamilyFilter;
import org.apache.hadoop.hbase.util.Bytes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

import static com.mesalab.hos.util.PublicUtil.stringTonextAscii;


/**
 * Created by wk on 2020/4/20.
 */
@Component
public class IndexFileNameDaoImpl implements IndexFileNameDao {

    private final org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(this.getClass());
    @Autowired
    private Connection hbaseConnection;
    @Autowired
    private HosProperties hosProperties;

    @Override
    public Boolean putFile(String tableName, String rowKey,String fileRowKey,String timestamp) {
        boolean isSuccess = false;
        try (Table table = hbaseConnection.getTable(TableName.valueOf(tableName))) {
            Put put = new Put(Bytes.toBytes(rowKey));
            put.addColumn(Bytes.toBytes("meta"), Bytes.toBytes("filename"), Bytes.toBytes(fileRowKey));
            put.addColumn(Bytes.toBytes("meta"), Bytes.toBytes("timestamp"), Bytes.toBytes(timestamp));
            table.put(put);
            isSuccess = true;
        } catch (Exception e) {
            logger.error(e.toString());
        }
        return isSuccess;
    }

    @Override
    public ColumnEntity getColumnWithTime(String tableName, String rowKey, String column) {
        String message;
        ColumnEntity ce = new ColumnEntity();
        try (Table table = hbaseConnection.getTable(TableName.valueOf(tableName))) {
            Get get = new Get(rowKey.getBytes());
            Result result = table.get(get);
            message = Bytes.toString(result.getValue(Bytes.toBytes("meta"), Bytes.toBytes(column)));
            long timestamp = result.getColumnLatestCell(Bytes.toBytes("meta"), Bytes.toBytes(column)).getTimestamp();
            ce.setTimestamp(timestamp);
            ce.setColumn(message);
            if (message == null) {
                logger.error("column not exit");
                ce = null;
            }
        } catch (Exception e) {
            logger.error(e.toString());
            ce = null;
        }
        return ce;
    }


    @Override
    public Boolean deleteFile(String tableName, String rowKey) {
        boolean issuccess = false;
        try (Table table = hbaseConnection.getTable(TableName.valueOf(tableName))) {
            Delete delete = new Delete(rowKey.getBytes());
            table.delete(delete);
            issuccess = true;
        } catch (Exception e) {
            logger.error(e.toString());
        }
        return issuccess;
    }

    @Override
    public Boolean ifExistRowKey(String tableName, String rowKey) {
        boolean ifExist = false;
        try (Table table = hbaseConnection.getTable(TableName.valueOf(tableName))) {
            Get get = new Get(Bytes.toBytes(rowKey));
            ifExist = table.exists(get);
        } catch (Exception e) {
            logger.error(e.toString());
        }
        return ifExist;
    }

    @Override
    public List<Content> getObjectListWithPrefixFilter(String tableName, int maxKeys, String startTime, String endTime, String prefix) throws Exception {
        List<Content> contents = new LinkedList<>();
        try (Table table = hbaseConnection.getTable(TableName.valueOf(tableName))) {
            Scan scan = new Scan();
            scan.withStartRow(Bytes.toBytes(prefix), false);
            scan.withStopRow(Bytes.toBytes(stringTonextAscii(prefix)), false);
            scan.setLimit(hosProperties.getMaxResultLimit());
            scan.setTimeRange(Long.parseLong(startTime + "000"), Long.parseLong(endTime + "999"));
            FamilyFilter filter = new FamilyFilter(CompareOperator.EQUAL, new BinaryComparator(Bytes.toBytes("meta")));
            ResultScanner resultScanner = table.getScanner(scan.setFilter(filter));
            for (Result result : resultScanner) {
                String filename = new String(result.getValue(Bytes.toBytes("meta"), Bytes.toBytes("filename")));
                String time = new String(result.getValue(Bytes.toBytes("meta"), Bytes.toBytes("timestamp")));
                Content ct = new Content();
                Instant timestamp = Instant.ofEpochMilli(Long.parseLong(time));
                ct.setKey(filename);
                ct.setLastModified(timestamp.toString());
                //t.setSize(fileSize);
                contents.add(ct);
            }

            return contents;

        } catch (TableNotFoundException e) {
            logger.error(e.toString());
            throw new TableNotFoundException();
        } catch (Exception e) {
            logger.error(e.toString());
            throw new Exception();
        }
    }
}
