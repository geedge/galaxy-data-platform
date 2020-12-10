package com.mesalab.hos.dao.Impl;

import com.mesalab.hos.config.HbaseProperties;
import com.mesalab.hos.dao.PartFileDao;
import com.mesalab.hos.entity.FileEntity;
import com.mesalab.hos.entity.ResEntity;
import com.mesalab.hos.entity.getMultipartUpload.ListPartsResponse;
import com.mesalab.hos.entity.getMultipartUpload.Part;
import com.mesalab.hos.entity.getMultipartUpload.Upload;
import com.mesalab.hos.entity.partEntity;
import org.apache.hadoop.hbase.*;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.filter.RegexStringComparator;
import org.apache.hadoop.hbase.filter.SingleColumnValueFilter;
import org.apache.hadoop.hbase.util.Bytes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

import static com.mesalab.hos.util.PublicUtil.getRowKey;


/**
 * Created by wk on 2020/4/20.
 */
@Component
public class PartfileDaoImpl implements PartFileDao {

    private final org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(this.getClass());
    @Autowired
    private Connection hbaseConnection;
    @Autowired
    HbaseProperties hbaseProperties;

    @Override
    public ResEntity putFile(FileEntity fileEntity) {
        ResEntity resEntity = new ResEntity();
        resEntity.setCode("");
        resEntity.setIsSuccess(false);
        resEntity.setReturnEntity(getRowKey(fileEntity.getFileName()));
        try (Table table = hbaseConnection.getTable(TableName.valueOf(fileEntity.getUser() + ":index_partfile_" + fileEntity.getBucket()))) {
            Put put = new Put(Bytes.toBytes(getRowKey(fileEntity.getFileName())));
            put.addColumn(Bytes.toBytes("meta"), Bytes.toBytes("message"), Bytes.toBytes(fileEntity.getMessage()));
            put.addColumn(Bytes.toBytes("meta"), Bytes.toBytes("contenttype"), Bytes.toBytes(fileEntity.getFileType()));
            put.addColumn(Bytes.toBytes("meta"), Bytes.toBytes("filename"), Bytes.toBytes(fileEntity.getFileName()));
            put.addColumn(Bytes.toBytes("meta"), Bytes.toBytes("partname"), Bytes.toBytes(""));
            table.put(put);
            resEntity.setIsSuccess(true);
        } catch (TableNotFoundException e) {
            logger.error("part object error"+e.toString());
            resEntity.setCode("NoSuchBucket");
        } catch (Exception e) {
            logger.error("part object error"+e.toString());
            resEntity.setCode("InternalError");
        }
        return resEntity;
    }

    @Override
    public ResEntity appendColumn(String tableName, String fileName, String columnName, String columnValue) {
        ResEntity resEntity = new ResEntity();
        resEntity.setCode("");
        resEntity.setIsSuccess(false);
        try (Table table = hbaseConnection.getTable(TableName.valueOf(tableName))) {
            Append append = new Append(Bytes.toBytes(fileName));
            append.addColumn(Bytes.toBytes("meta"), Bytes.toBytes(columnName), Bytes.toBytes(columnValue));
            table.append(append);
            resEntity.setIsSuccess(true);
        } catch (TableNotFoundException e) {
            logger.error("part object error"+e.toString());
            resEntity.setCode("NoSuchBucket");
        } catch (Exception e) {
            logger.error("part object error"+e.toString());
            resEntity.setCode("InternalError");
        }
        return resEntity;
    }

    @Override
    public ResEntity getFile(String tableName, String key) {
        ResEntity resEntity = new ResEntity();
        resEntity.setCode("");
        resEntity.setIsSuccess(false);
        partEntity partFile = new partEntity();
        try (Table table = hbaseConnection.getTable(TableName.valueOf(tableName))) {
            Get get = new Get(Bytes.toBytes(key));
            Result result = table.get(get);
            String filename = Bytes.toString(result.getValue(Bytes.toBytes("meta"), Bytes.toBytes("filename")));
            String message = Bytes.toString(result.getValue(Bytes.toBytes("meta"), Bytes.toBytes("message")));
            String fileType = Bytes.toString(result.getValue(Bytes.toBytes("meta"), Bytes.toBytes("contenttype")));
            String partName = Bytes.toString(result.getValue(Bytes.toBytes("meta"), Bytes.toBytes("partname")));

            partFile.setFileName(filename);
            partFile.setFileType(fileType);
            partFile.setMessage(message);
            partFile.setUploadId(key);
            partFile.setPartName(partName);
            resEntity.setIsSuccess(true);
        } catch (TableNotFoundException e) {
            logger.error("part object error"+e.toString());
            resEntity.setCode("NoSuchBucket");
        } catch (Exception e) {
            logger.error("part object error"+e.toString());
            resEntity.setCode("InternalError");
        }
        resEntity.setReturnEntity(partFile);
        return resEntity;
    }

    @Override
    public ResEntity deleteFile(String tableName, String filename) {
        ResEntity resEntity = new ResEntity();
        resEntity.setCode("");
        resEntity.setIsSuccess(false);
        try (Table table = hbaseConnection.getTable(TableName.valueOf(tableName))) {
            Delete delete = new Delete(filename.getBytes());
            table.delete(delete);
            resEntity.setIsSuccess(true);
        } catch (Exception e) {
            logger.error("part object error"+e.toString());
        }
        return resEntity;
    }

    @Override
    public Boolean ifExistRowKey(String tableName, String key) {
        boolean ifExist = false;
        try (Table table = hbaseConnection.getTable(TableName.valueOf(tableName))) {
            Get get = new Get(Bytes.toBytes(key));
            ifExist = table.exists(get);
        } catch (Exception e) {
            logger.error("part object error"+e.toString());
        }
        return ifExist;
    }

    @Override
    public ResEntity getMultipartUploadObjects(String tableName,ResEntity resEntity) {
        List<Upload> uploadList = new ArrayList<>();
        try (Table table = hbaseConnection.getTable(TableName.valueOf(tableName))){
            Scan scan = new Scan();
            ResultScanner scanner = table.getScanner(scan);
            for (Result rss : scanner) {
                Upload upload = new Upload();
                String uploadId = Bytes.toString(rss.getRow());
                Cell fileName = rss.getColumnLatestCell("meta".getBytes(), "filename".getBytes());
                long initiated = fileName.getTimestamp();
                String key = Bytes.toString(CellUtil.cloneValue(fileName));
                upload.setKey(key);
                upload.setInitiated(String.valueOf(initiated));
                upload.setUploadId(uploadId);
                uploadList.add(upload);
            }
            resEntity.setReturnEntity(uploadList);
        }catch (TableNotFoundException e) {
            logger.error("part object error"+e.toString());
            resEntity.setCode("NoSuchBucket");
        } catch (Exception e) {
            logger.error("part object error"+e.toString());
            resEntity.setCode("InternalError");
        }
        return resEntity;
    }

    @Override
    public ResEntity getMultipartUploadObjectsByPrefix(String tableName, ResEntity resEntity, String prefix) {
        List<Upload> uploadList = new ArrayList<>();
        try (Table table = hbaseConnection.getTable(TableName.valueOf(tableName))){
            Scan scan = new Scan();
            SingleColumnValueFilter singleColumnValueFilter = new SingleColumnValueFilter(
                    "meta".getBytes(),
                    "filename".getBytes(),
                    CompareOperator.EQUAL,
                    new RegexStringComparator(prefix+".*"));
            singleColumnValueFilter.setFilterIfMissing(true);
            scan.setFilter(singleColumnValueFilter);
            ResultScanner scanner = table.getScanner(scan);
            for (Result rss : scanner) {
                Upload upload = new Upload();
                String uploadId = Bytes.toString(rss.getRow());
                Cell fileName = rss.getColumnLatestCell("meta".getBytes(), "filename".getBytes());
                long initiated = fileName.getTimestamp();
                String key = Bytes.toString(CellUtil.cloneValue(fileName));
                upload.setKey(key);
                upload.setInitiated(String.valueOf(initiated));
                upload.setUploadId(uploadId);
                uploadList.add(upload);
            }
            resEntity.setReturnEntity(uploadList);
        }catch (TableNotFoundException e) {
            logger.error("part object error"+e.toString());
            resEntity.setCode("NoSuchBucket");
        } catch (Exception e) {
            logger.error("part object error"+e.toString());
            resEntity.setCode("InternalError");
        }
        return resEntity;
    }

    @Override
    public ResEntity getObjectParts(String user,String bucket, String object, ResEntity resEntity) {
        List<Part> partList = new ArrayList<>();
        ListPartsResponse listPartsResponse = (ListPartsResponse)resEntity.getReturnEntity();
        try (Table partTable = hbaseConnection.getTable(TableName.valueOf(user + ":"+ hbaseProperties.getPartfile_index_table_prefix() + bucket));
             Table dataTable = hbaseConnection.getTable(TableName.valueOf(user + ":" + bucket))){
            Get get = new Get(getRowKey(object).getBytes());

            Result partResult = partTable.get(get);
            String uploadId = Bytes.toString(partResult.getRow());
            listPartsResponse.setUploadId(uploadId);
            String objectPart = Bytes.toString(CellUtil.cloneValue(partResult.getColumnLatestCell("meta".getBytes(), "partname".getBytes())));
            String[] objectPartArr = objectPart.split(",");
            for (String s : objectPartArr) {
                Part part = new Part();
                Get dataGet = new Get(getRowKey(object+"|"+s).getBytes());
                Result dataResult = dataTable.get(dataGet);
                Cell fileSizeCell = dataResult.getColumnLatestCell("meta".getBytes(), "filesize".getBytes());
                long timeStamp = fileSizeCell.getTimestamp();
                String partEtag = Bytes.toString(dataResult.getRow());
                String size = Bytes.toString(CellUtil.cloneValue(fileSizeCell));
                part.setPartNumber(Integer.parseInt(s));
                part.setETag(partEtag);
                part.setSize(Integer.parseInt(size));
                part.setLastModified(timeStamp + "");
                partList.add(part);
            }
            listPartsResponse.setPart(partList);
            resEntity.setReturnEntity(listPartsResponse);
        }catch (TableNotFoundException e) {
            logger.error("part object error"+e.toString());
            resEntity.setCode("NoSuchBucket");
        } catch (Exception e) {
            logger.error("part object error"+e.toString());
            resEntity.setCode("InternalError");
        }
        return resEntity;
    }
}
