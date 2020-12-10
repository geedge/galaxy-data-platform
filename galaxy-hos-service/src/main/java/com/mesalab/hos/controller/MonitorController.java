package com.mesalab.hos.controller;

import com.alibaba.fastjson.JSONArray;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.*;
import com.amazonaws.util.IOUtils;
import com.mesalab.hos.config.HosProperties;
import com.mesalab.hos.util.PublicUtil;

import org.apache.hadoop.hbase.CellUtil;
import org.apache.hadoop.hbase.NamespaceNotFoundException;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.TableNotFoundException;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.util.Bytes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;


/**
 * Created by wk on 2020/4/16.
 */
@RestController
@Component
public class MonitorController {

    private final org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(this.getClass());

    @Autowired
    private Connection hbaseConnection;
    @Autowired
    private HosProperties hosProperties;

    private String endpoint = "http://127.0.0.1:9098/hos";
    private String accessKey = "default";
    private String secretKey = "default";
    private String user = "default";
    private String bucketName = "hostest";
    private String objectName = "aa";
    private String url = endpoint + "/" + bucketName + "/" + objectName;
    private String data = "aaaa";//文件数据
    private String appendData = "bbbb";//文件追加的数据
    private String message = "aa";//文件元数据
    private String appendMessage = "bb";//追加的元数据
    private int lifecycleDays = 10;//设置生命周期天数

    private BasicAWSCredentials awsCredentials = new BasicAWSCredentials(accessKey, secretKey);
    private AmazonS3 s3 = AmazonS3ClientBuilder.standard()
            .withCredentials(new AWSStaticCredentialsProvider(awsCredentials))
            .withEndpointConfiguration(new AwsClientBuilder.EndpointConfiguration(endpoint, "us-east-1"))
            .withChunkedEncodingDisabled(true)
            .build();

    @GetMapping(value = "/monitor")
    public String getReportStatus() {
        String json = "";
        try {

            Map checkmap = new TreeMap();
            checkmap.put("CreateBucket", testS3CreateBucket());
            checkmap.put("AppendFile", testS3AppendFile());
            checkmap.put("UploadFile", testS3UploadFile());
            checkmap.put("ChangeLifecycle", testS3ChangeLifecycle());
            checkmap.put("GetBucketList", testS3GetBucketList());
            if (hosProperties.getSimple() == 1) {
                checkmap.put("GetObjectList", testS3GetObjectList());
            }
            checkmap.put("DeleteBucket", testS3DeleteBucket());

            Object obj = JSONArray.toJSON(checkmap);
            json = obj.toString();
        } catch (Exception e) {
            logger.error(e.toString());
        }
        return json;
    }


    public String testS3CreateBucket() {

        String message = "";
        try {
            s3.createBucket(bucketName);
            try (Admin admin = hbaseConnection.getAdmin()) {
                admin.getNamespaceDescriptor(user);
                TableDescriptor descriptor = admin.getDescriptor(TableName.valueOf(user + ":" + bucketName));
                message = "创建桶验证成功";
            } catch (NamespaceNotFoundException e) {
                message = "创建桶验证失败：用户不存在";
            } catch (TableNotFoundException e) {
                message = "创建桶验证失败：桶不存在";
            } catch (Exception e) {
                message = "创建桶验证失败" + e.toString();
            }
        } catch (Exception e) {
            message = "创建桶失败" + e.toString();
        }
        return message;
    }


    public String testS3UploadFile() {

        String message = "";
        try (InputStream input = new ByteArrayInputStream(data.getBytes());
             Table table = hbaseConnection.getTable(TableName.valueOf(user + ":" + bucketName));
        ) {
            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setHeader("x-hos-meta-message", message);
            PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, objectName, input, metadata);
            s3.putObject(putObjectRequest);
            Result result = table.get(new Get(PublicUtil.getRowKey(objectName).getBytes()));
            String stringData = Bytes.toString(CellUtil.cloneValue(result.getColumnLatestCell("data".getBytes(), "file".getBytes())));
            String fileMessage = Bytes.toString(CellUtil.cloneValue(result.getColumnLatestCell("meta".getBytes(), "message".getBytes())));
            if (data.equals(stringData)) {
                if (message.equals(fileMessage)) {
                    message = "上传功能验证成功";
                } else {
                    message = "上传失败：元信息内容与上传的元信息内容不符";
                }
            } else {
                message = "上传失败：文件内容与上传内容不符";
            }
        } catch (Exception e) {

            message = "上传失败 " + e.toString();
        }
        return message;

    }


    public String testS3AppendFile() {

        String resultmessage = "追加功能验证成功";

        try (Table table = hbaseConnection.getTable(TableName.valueOf(user + ":" + bucketName))) {
            Result result;
            for (int i = 1; i <= 5; i++) {
                try (InputStream input = new ByteArrayInputStream((appendData + i).getBytes())) {
                    ObjectMetadata metadata = new ObjectMetadata();
                    metadata.setHeader("x-hos-upload-type", "append");
                    metadata.setHeader("x-hos-meta-message", appendMessage + i);
                    metadata.setHeader("x-hos-position", i);
                    PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, objectName, input, metadata);
                    s3.putObject(putObjectRequest);
                    result = table.get(new Get(PublicUtil.getRowKey(objectName + "|" + i).getBytes()));
                    String stringData = Bytes.toString(CellUtil.cloneValue(result.getColumnLatestCell("data".getBytes(), "file".getBytes())));
                    if (!stringData.equals(appendData + i)) {
                        resultmessage = "文件内容追加失败";
                    }
                    result = table.get(new Get(PublicUtil.getRowKey(objectName + "|" + i).getBytes()));

                } catch (Exception e) {

                    resultmessage = "文件追加失败" + e.toString();
                }
            }
            S3Object object = s3.getObject(bucketName, objectName);

            byte[] bytesData = IOUtils.toByteArray(object.getObjectContent());

            String fileMessage = object.getObjectMetadata().getRawMetadataValue("x-hos-meta-message").toString();

            String stringData = new String(bytesData);
            StringBuffer fileData = new StringBuffer();
            StringBuffer fm = new StringBuffer();
            for (int i = 1; i <= 5; i++) {
                fileData.append(appendData).append(i);
                fm.append(appendMessage).append(i);
            }
            for (int i = 1; i <= 5; i++) {
            }

            if (!(fileData.toString()).equals(stringData)) {
                resultmessage = "文件内容与预期不符";
            }
            if (!(fileMessage).equals(fm.toString())) {
                resultmessage = "文件元数据与预期不符";
            }
            s3.deleteObject(bucketName, objectName);
            boolean exists = table.exists(new Get(PublicUtil.getRowKey(objectName).getBytes()));
            if (exists) {
                resultmessage = "删除追加文件失败";
            }
        } catch (Exception e) {

            resultmessage = "文件追加验证失败" + e.toString();
        }
        return resultmessage;
    }

    public String testS3ChangeLifecycle() {

        String message = "";
        try {
            BucketLifecycleConfiguration lifecycleConfiguration = new BucketLifecycleConfiguration();
            BucketLifecycleConfiguration.Rule rule = new BucketLifecycleConfiguration.Rule();
            rule.setId("1");
            rule.setExpirationInDays(lifecycleDays);
            rule.setStatus("Enabled");
            rule.setPrefix("");
            ArrayList<BucketLifecycleConfiguration.Rule> rules = new ArrayList<>();
            rules.add(rule);
            lifecycleConfiguration.setRules(rules);
            s3.setBucketLifecycleConfiguration(bucketName, lifecycleConfiguration);
            message = "更改桶TTl验证成功";
        } catch (Exception e) {
            message = "更改ttl功能失败" + e.toString();
        }

        int dataTTL = 0;
        int metaTTL = 0;
        try (Admin admin = hbaseConnection.getAdmin()) {
            TableDescriptor tableDescriptor = admin.getDescriptor(TableName.valueOf(bucketName));
            ColumnFamilyDescriptor dataColumnFamily = tableDescriptor.getColumnFamily("data".getBytes());
            ColumnFamilyDescriptor metaColumnFamily = tableDescriptor.getColumnFamily("meta".getBytes());
            dataTTL = dataColumnFamily.getTimeToLive();
            metaTTL = metaColumnFamily.getTimeToLive();
            if (dataTTL != lifecycleDays * 24 * 60 * 60) {
                message = "生命周期设置失败：桶的生命周期和预期值不同";
            }
            if (metaTTL != lifecycleDays * 24 * 60 * 60) {
                message = "生命周期设置失败：桶的生命周期和预期值不同";
            }
        } catch (IOException e) {
            message = "生命周期设置失败 " + e.toString();
        }
        return message;
    }

    public String testS3GetBucketList() {

        String message = "";
        try {
            List<Bucket> buckets = s3.listBuckets(new ListBucketsRequest());
            ArrayList<String> bucketNameList = new ArrayList<>();
            ArrayList<String> bucketNameList1 = new ArrayList<>();
            for (Bucket bucket : buckets) {
                bucketNameList.add(bucket.getName());
            }
            message = "获取桶列表功能验证成功";
            try (Admin admin = hbaseConnection.getAdmin()) {
                TableName[] tableNames = admin.listTableNamesByNamespace(user);
                for (TableName tableName : tableNames) {
                    bucketNameList1.add(Bytes.toString(tableName.getName()));
                }
                if (bucketNameList.retainAll(bucketNameList1)) {
                    message = "获取桶列表错误：获取的桶列表不正确";
                }
            } catch (Exception e) {
                message = "获取桶列表错误" + e.toString();
            }

        } catch (Exception e) {
            message = "获取桶列表错误" + e.toString();
        }
        return message;
    }


    public String testS3DeleteBucket() {
        String message = "";
        s3.deleteBucket(bucketName);
        message = "删除桶功能验证成功";
        try (Admin admin = hbaseConnection.getAdmin()) {
            if (admin.tableExists(TableName.valueOf(user + ":" + bucketName))) {
                message = "桶删除失败";
            }
        } catch (Exception e) {
            message = "桶删除失败" + e.toString();
        }
        return message;
    }

    public String testS3GetObjectList() {
        String message;
        try {
            List<String> putObjs = new ArrayList<>();
            long startTime = System.currentTimeMillis() / 1000 - 100;
            putObjs.add("b-1");
            putObjs.add("b-2");
            putObjs.add("b-3");
            for (String obj : putObjs) {
                uploadObj(obj);
            }
            long endTime = System.currentTimeMillis() / 1000 + 100;
            String startAfter = startTime + "_" + endTime;

            //测试按时间+前缀查询列表
            ArrayList<String> objectNames = new ArrayList<>();
            ListObjectsV2Request listObjectsV2Request = new ListObjectsV2Request();
            listObjectsV2Request.withMaxKeys(10)
                    .withStartAfter(startAfter)
                    .withBucketName(bucketName)
                    .withPrefix("b");
            ListObjectsV2Result listObjectsV2Result = s3.listObjectsV2(listObjectsV2Request);
            List<S3ObjectSummary> S3ObjectSummarylist = listObjectsV2Result.getObjectSummaries();
            for (S3ObjectSummary S3Object : S3ObjectSummarylist) {
                String filename = S3Object.getKey();
                objectNames.add(filename);
            }
            if (!objectNames.containsAll(putObjs)) {
                return "按时间+文件名前缀获取对象列表错误，和预期列表不符";
            }
            //测试按时间查询列表
            objectNames = new ArrayList<>();
            listObjectsV2Request = new ListObjectsV2Request();
            listObjectsV2Request.withMaxKeys(10)
                    .withStartAfter(startAfter)
                    .withBucketName(bucketName);
            listObjectsV2Result = s3.listObjectsV2(listObjectsV2Request);
            S3ObjectSummarylist = listObjectsV2Result.getObjectSummaries();
            for (S3ObjectSummary S3Object : S3ObjectSummarylist) {
                String filename = S3Object.getKey();
                objectNames.add(filename);
            }
            if (!objectNames.containsAll(putObjs)) {
                return "按时间获取对象列表错误，和预期列表不符";
            }
            message = "获取对象列表功能验证成功";
        } catch (Exception e) {
            message = "获取对象列表失败" + e.toString();
        }
        return message;
    }

    public void uploadObj(String object) throws IOException {
        InputStream input = new ByteArrayInputStream(data.getBytes());
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setHeader("x-hos-meta-message", message);
        PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, object, input, metadata);
        s3.putObject(putObjectRequest);
        input.close();
    }

}
