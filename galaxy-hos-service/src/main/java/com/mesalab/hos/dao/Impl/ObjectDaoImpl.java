package com.mesalab.hos.dao.Impl;

import com.mesalab.hos.dao.ObjectDao;
import com.mesalab.hos.entity.FileEntity;
import com.mesalab.hos.entity.ResEntity;
import com.mesalab.hos.entity.ColumnEntity;
import org.apache.hadoop.hbase.CellUtil;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.TableNotFoundException;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.util.Bytes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

import static com.mesalab.hos.util.PublicUtil.byteMerger;
import static com.mesalab.hos.util.PublicUtil.getRowKey;


/**
 * Created by wk on 2020/4/20.
 */
@Component
public class ObjectDaoImpl implements ObjectDao {

    private final org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(this.getClass());
    @Autowired
    private Connection hbaseConnection;

    @Override
    public ResEntity putFile(FileEntity fileEntity) {
        ResEntity resEntity = new ResEntity();
        resEntity.setCode("");
        resEntity.setIsSuccess(false);
        resEntity.setReturnEntity(getRowKey(fileEntity.getFileName()));
        try (Table table = hbaseConnection.getTable(TableName.valueOf(fileEntity.getUser() + ":" + fileEntity.getBucket()))) {
            Put put = new Put(Bytes.toBytes(getRowKey(fileEntity.getFileName())));
            put.addColumn(Bytes.toBytes("meta"), Bytes.toBytes("message"), Bytes.toBytes(fileEntity.getMessage()));
            put.addColumn(Bytes.toBytes("meta"), Bytes.toBytes("contenttype"), Bytes.toBytes(fileEntity.getFileType()));
            put.addColumn(Bytes.toBytes("meta"), Bytes.toBytes("filesize"), Bytes.toBytes(String.valueOf(fileEntity.getFileSize())));
            //put.addColumn(Bytes.toBytes("meta"), Bytes.toBytes("filename"), Bytes.toBytes(fileEntity.getFileName()));
            put.addColumn(Bytes.toBytes("meta"), Bytes.toBytes("isparent"), Bytes.toBytes(fileEntity.getIsParent()));
            put.addColumn(Bytes.toBytes("meta"), Bytes.toBytes("filenamekey"), Bytes.toBytes(fileEntity.getIndexFileNameKey()));
            put.addColumn(Bytes.toBytes("meta"), Bytes.toBytes("timekey"), Bytes.toBytes(fileEntity.getIndexTimeKey()));
            put.addColumn(Bytes.toBytes("meta"), Bytes.toBytes("partname"),Bytes.toBytes(fileEntity.getPartName()));

            put.addColumn(Bytes.toBytes("data"), Bytes.toBytes("file"), fileEntity.getFile());
            table.put(put);
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
    public ResEntity putAppendFileMeta(FileEntity fileEntity) {
        ResEntity resEntity = new ResEntity();
        resEntity.setCode("");
        resEntity.setIsSuccess(false);
        try (Table table = hbaseConnection.getTable(TableName.valueOf(fileEntity.getUser() + ":" + fileEntity.getBucket()))) {
            Put put = new Put(Bytes.toBytes(getRowKey(fileEntity.getFileName())));
            put.addColumn(Bytes.toBytes("meta"), Bytes.toBytes("message"), Bytes.toBytes(fileEntity.getMessage()));
            put.addColumn(Bytes.toBytes("meta"), Bytes.toBytes("contenttype"), Bytes.toBytes(fileEntity.getFileType()));
            put.addColumn(Bytes.toBytes("meta"), Bytes.toBytes("filesize"), Bytes.toBytes(String.valueOf(fileEntity.getFileSize())));
            put.addColumn(Bytes.toBytes("meta"), Bytes.toBytes("filename"), Bytes.toBytes(fileEntity.getFileName()));
            put.addColumn(Bytes.toBytes("meta"), Bytes.toBytes("isparent"), Bytes.toBytes(fileEntity.getIsParent()));
            put.addColumn(Bytes.toBytes("meta"), Bytes.toBytes("filenamekey"), Bytes.toBytes(fileEntity.getIndexFileNameKey()));
            put.addColumn(Bytes.toBytes("meta"), Bytes.toBytes("timekey"), Bytes.toBytes(fileEntity.getIndexTimeKey()));
            Append append = new Append(Bytes.toBytes(getRowKey(fileEntity.getFileName())));
            append.addColumn(Bytes.toBytes("meta"), Bytes.toBytes("partname"),Bytes.toBytes(fileEntity.getPartName()));
           /// append.addColumn(Bytes.toBytes("meta"), Bytes.toBytes("filenamekey"), Bytes.toBytes(fileEntity.getIndexFileNameKey()));
           // append.addColumn(Bytes.toBytes("meta"), Bytes.toBytes("timekey"), Bytes.toBytes(fileEntity.getIndexTimeKey()));
            table.put(put);
            table.append(append);
            resEntity.setIsSuccess(true);
        } catch (TableNotFoundException e) {
            logger.error(e.toString());
            resEntity.setCode("NoSuchBucket");
        } catch (Exception e) {
            logger.error(e.toString());
            resEntity.setCode("InternalError");
        }
        return resEntity;    }

    @Override
    public ResEntity putColumn(String tableName, String rowKey, String column, String columnData) {
        ResEntity resEntity = new ResEntity();
        resEntity.setCode("");
        resEntity.setIsSuccess(false);
        try (Table table = hbaseConnection.getTable(TableName.valueOf(tableName))) {
            Put put = new Put(Bytes.toBytes(rowKey));
            put.addColumn(Bytes.toBytes("meta"), Bytes.toBytes(column),Bytes.toBytes(columnData));
            table.put(put);
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
    public ResEntity putAllFileMeta(FileEntity fileEntity) {
        ResEntity resEntity = new ResEntity();
        resEntity.setCode("");
        resEntity.setIsSuccess(false);
        try (Table table = hbaseConnection.getTable(TableName.valueOf(fileEntity.getUser() + ":" + fileEntity.getBucket()))) {
            Put put = new Put(Bytes.toBytes(getRowKey(fileEntity.getFileName())));
            put.addColumn(Bytes.toBytes("meta"), Bytes.toBytes("message"), Bytes.toBytes(fileEntity.getMessage()));
            put.addColumn(Bytes.toBytes("meta"), Bytes.toBytes("contenttype"), Bytes.toBytes(fileEntity.getFileType()));
            put.addColumn(Bytes.toBytes("meta"), Bytes.toBytes("filename"), Bytes.toBytes(fileEntity.getFileName()));
            put.addColumn(Bytes.toBytes("meta"), Bytes.toBytes("isparent"), Bytes.toBytes(fileEntity.getIsParent()));
            put.addColumn(Bytes.toBytes("meta"), Bytes.toBytes("filesize"), Bytes.toBytes(String.valueOf(fileEntity.getFileSize())));
            put.addColumn(Bytes.toBytes("meta"), Bytes.toBytes("filenamekey"), Bytes.toBytes(fileEntity.getIndexFileNameKey()));
            put.addColumn(Bytes.toBytes("meta"), Bytes.toBytes("timekey"), Bytes.toBytes(fileEntity.getIndexTimeKey()));
            if(fileEntity.getPartName()!=null) {
                put.addColumn(Bytes.toBytes("meta"), Bytes.toBytes("partname"), Bytes.toBytes(fileEntity.getPartName()));
            }
            else{
                put.addColumn(Bytes.toBytes("meta"), Bytes.toBytes("partname"), Bytes.toBytes(""));
            }
            table.put(put);
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
    public ResEntity getAllFileMeta(String tableName, String fileName) {
        ResEntity resEntity = new ResEntity();
        resEntity.setCode("");
        resEntity.setIsSuccess(false);
        FileEntity fileEntity = new FileEntity();
        try (Table table = hbaseConnection.getTable(TableName.valueOf(tableName))) {
            Get get = new Get(Bytes.toBytes(fileName));
            Result result = table.get(get);
            if (result.isEmpty()) {
                resEntity.setCode("NoSuchKey");
            }else {
                String message = Bytes.toString(CellUtil.cloneValue(result.getColumnLatestCell(Bytes.toBytes("meta"), Bytes.toBytes("message"))));
                String contentType = Bytes.toString(CellUtil.cloneValue(result.getColumnLatestCell(Bytes.toBytes("meta"), Bytes.toBytes("contenttype"))));
                String fileSize = Bytes.toString(CellUtil.cloneValue(result.getColumnLatestCell(Bytes.toBytes("meta"), Bytes.toBytes("filesize"))));
                String partname = Bytes.toString(CellUtil.cloneValue(result.getColumnLatestCell(Bytes.toBytes("meta"), Bytes.toBytes("partname"))));
                String fileNameKey = Bytes.toString(CellUtil.cloneValue(result.getColumnLatestCell(Bytes.toBytes("meta"), Bytes.toBytes("filenamekey"))));
                String timeKey = Bytes.toString(CellUtil.cloneValue(result.getColumnLatestCell(Bytes.toBytes("meta"), Bytes.toBytes("timekey"))));
                int isParent = Bytes.toInt(CellUtil.cloneValue(result.getColumnLatestCell(Bytes.toBytes("meta"), Bytes.toBytes("isparent"))));
                fileEntity.setMessage(message);
                fileEntity.setFileType(contentType);
                fileEntity.setFileSize(Long.parseLong(fileSize));
                fileEntity.setPartName(partname);
                fileEntity.setIsParent(isParent);
                fileEntity.setIndexFileNameKey(fileNameKey);
                fileEntity.setIndexTimeKey(timeKey);
                resEntity.setReturnEntity(fileEntity);
                resEntity.setIsSuccess(true);
            }
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
    public Boolean ifMultiFile(String tableName, String rowKey) {
        boolean isMulti = false;
        try (Table table = hbaseConnection.getTable(TableName.valueOf(tableName))) {
            Get get = new Get(Bytes.toBytes(rowKey));
            Result result = table.get(get);
            int partStatus = Bytes.toInt(result.getValue(Bytes.toBytes("meta"), Bytes.toBytes("isparent")));
            if (partStatus > 0) {
                isMulti = true;
            }
        } catch (Exception e) {
            logger.error(e.toString());
        }
        return isMulti;
    }

    @Override
    public ResEntity appendColumn(String tableName, String rowKey, String column,String columnData) {
        ResEntity resEntity = new ResEntity();
        resEntity.setCode("");
        resEntity.setIsSuccess(false);
        try (Table table = hbaseConnection.getTable(TableName.valueOf(tableName))) {
            Append append = new Append(Bytes.toBytes(rowKey));
            append.addColumn(Bytes.toBytes("meta"), Bytes.toBytes(column),columnData.getBytes());
            table.append(append);
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
    public ResEntity appendIndexKey(FileEntity fileEntity) {
        ResEntity resEntity = new ResEntity();
        resEntity.setCode("");
        resEntity.setIsSuccess(false);
        try (Table table = hbaseConnection.getTable(TableName.valueOf(fileEntity.getUser() + ":" + fileEntity.getBucket()))) {
            Append append = new Append(Bytes.toBytes(getRowKey(fileEntity.getFileName())));
            append.addColumn(Bytes.toBytes("meta"), Bytes.toBytes("filenamekey"), Bytes.toBytes(fileEntity.getIndexFileNameKey()+","));
            append.addColumn(Bytes.toBytes("meta"), Bytes.toBytes("timekey"), Bytes.toBytes(fileEntity.getIndexTimeKey()+","));
            table.append(append);
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
    public ResEntity getFile(String tableName, String fileName) {
        ResEntity resEntity = new ResEntity();
        resEntity.setCode("");
        resEntity.setIsSuccess(false);
        FileEntity fileEntity = new FileEntity();
        try (Table table = hbaseConnection.getTable(TableName.valueOf(tableName))) {
            Get get = new Get(Bytes.toBytes(getRowKey(fileName)));
            Result result = table.get(get);
            if(result.isEmpty()){
                resEntity.setCode("NoSuchKey");
            }else {
                String message = "";
                String fileType = Bytes.toString(result.getValue(Bytes.toBytes("meta"), Bytes.toBytes("contenttype")));
                int isParent = Bytes.toInt(result.getValue(Bytes.toBytes("meta"), Bytes.toBytes("isparent")));
                byte[] file = new byte[0];
                if(isParent==1){
                    String partFile = Bytes.toString(result.getValue(Bytes.toBytes("meta"), Bytes.toBytes("partname")));
                    String[] partsArray = partFile.split(",");
                    TreeSet<Integer> partsSet = new TreeSet<>();
                    StringBuffer messageBuffer = new StringBuffer();
                    for (String s : partsArray) {
                        partsSet.add(Integer.parseInt(s));
                    }
                    for (int part : partsSet) {
                        Get getPart = new Get(Bytes.toBytes(getRowKey(fileName+"|"+part)));
                        Result partResult = table.get(getPart);
                        byte[] partByte = partResult.getValue(Bytes.toBytes("data"), Bytes.toBytes("file"));
                        String partMessage = Bytes.toString(partResult.getValue(Bytes.toBytes("meta"), Bytes.toBytes("message")));
                        messageBuffer.append(partMessage);
                        file = byteMerger(file, partByte);
                    }
                    message = messageBuffer.toString();
                } else{
                    file = result.getValue(Bytes.toBytes("data"), Bytes.toBytes("file"));
                    message = Bytes.toString(result.getValue(Bytes.toBytes("meta"), Bytes.toBytes("message")));

                }
                if (file == null) {
                    resEntity.setCode("NoSuchKey");
                } else {
                    fileEntity.setFileName(fileName);
                    fileEntity.setMessage(message);
                    fileEntity.setFile(file);
                    fileEntity.setFileType(fileType);
                    resEntity.setIsSuccess(true);
                }
            }
            resEntity.setReturnEntity(fileEntity);
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
    public ResEntity getColumn(String tableName, String fileName, String column) {
        ResEntity resEntity = new ResEntity();
        resEntity.setCode("");
        resEntity.setIsSuccess(false);
        String message;
        ColumnEntity ce = new ColumnEntity();
        try (Table table = hbaseConnection.getTable(TableName.valueOf(tableName))) {
            Get get = new Get(Bytes.toBytes(fileName));
            Result result = table.get(get);
            if (result.isEmpty()) {
                resEntity.setCode("NoSuchKey");
            }else {
                message = Bytes.toString(CellUtil.cloneValue(result.getColumnLatestCell(Bytes.toBytes("meta"), Bytes.toBytes(column))));
                long timestamp = result.getColumnLatestCell(Bytes.toBytes("meta"), Bytes.toBytes(column)).getTimestamp();
                ce.setTimestamp(timestamp);
                ce.setColumn(message);
                resEntity.setReturnEntity(ce);
                resEntity.setIsSuccess(true);
            }
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
    public ResEntity deleteFile(String tableName, String fileName) {
        ResEntity resEntity = new ResEntity();
        resEntity.setCode("");
        resEntity.setIsSuccess(false);
        try (Table table = hbaseConnection.getTable(TableName.valueOf(tableName))) {
            Delete delete = new Delete(fileName.getBytes());
            table.delete(delete);
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
    public ResEntity deleteMultipartUploadFile(String tableName, String filename) {
        ResEntity resEntity = getColumn(tableName, getRowKey(filename), "partname");
        if (!resEntity.getIsSuccess()){
            return resEntity;
        }
        ColumnEntity returnEntity = (ColumnEntity) resEntity.getReturnEntity();
        String[] partNames = returnEntity.getColumn().split(",");
        for (String partName : partNames){
            resEntity = deleteFile(tableName, getRowKey(filename+"|"+partName));
            if (!resEntity.getIsSuccess()){
                return resEntity;
            }
        }
        return deleteFile(tableName,getRowKey(filename));
    }

    @Override
    public Boolean ifExistRowKey(String tableName, String fileName) {
        boolean ifExist = false;
        try (Table table = hbaseConnection.getTable(TableName.valueOf(tableName))) {
            Get get = new Get(Bytes.toBytes(fileName));
            ifExist = table.exists(get);
        } catch (Exception e) {
            logger.error(e.toString());
        }
        return ifExist;
    }
}
