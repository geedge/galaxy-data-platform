package com.mesalab.hos.service.impl;

import com.mesalab.hos.config.HbaseProperties;
import com.mesalab.hos.dao.*;
import com.mesalab.hos.entity.FileEntity;
import com.mesalab.hos.entity.MultipartUpload.Part;
import com.mesalab.hos.entity.ResEntity;
import com.mesalab.hos.entity.ColumnEntity;
import com.mesalab.hos.entity.delete.DeleteError;
import com.mesalab.hos.entity.delete.DeleteObj;
import com.mesalab.hos.entity.delete.DeleteResponse;
import com.mesalab.hos.entity.getMultipartUpload.*;
import com.mesalab.hos.entity.partEntity;
import com.mesalab.hos.service.ObjectService;
import com.mesalab.hos.util.PublicUtil;
import com.mesalab.hos.util.ResponseMessageUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;
import org.springframework.util.StringUtils;

import java.util.*;

import static com.mesalab.hos.util.PublicUtil.getRowKey;

@Service
@ConditionalOnProperty(value = "hos.simple", havingValue = "0")
public class ObjectSimpleServiceImpl implements ObjectService {
    private final org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(this.getClass());

    @Autowired
    private ObjectDao objectDao;
    @Autowired
    private PartFileDao partFileDao;
    @Autowired
    HbaseProperties hbaseProperties;

    @Override
    public ResEntity putFile(FileEntity fileEntity) {
        return objectDao.putFile(fileEntity);
    }

    @Override
    public ResEntity putFilePart(FileEntity fileEntity) {
        ResEntity resEntity = new ResEntity();
        resEntity.setCode("");
        resEntity.setIsSuccess(false);
        try {
            Boolean isExit = partFileDao.ifExistRowKey(fileEntity.getUser() + ":" + hbaseProperties.getPartfile_index_table_prefix() + fileEntity.getBucket(), getRowKey(fileEntity.getFileName()));
            if (isExit) {
                String partFilename = fileEntity.getFileName() + "|" + fileEntity.getPartNumber();
                partFileDao.appendColumn(fileEntity.getUser() + ":" + hbaseProperties.getPartfile_index_table_prefix() + fileEntity.getBucket(), getRowKey(fileEntity.getFileName()), "partname", fileEntity.getPartNumber() + ",");
                fileEntity.setFileName(partFilename);
                resEntity = objectDao.putFile(fileEntity);
            } else {
                logger.error("NoSuchUpload ! not create multipart upload");
                resEntity.setCode("NoSuchUpload");
            }
        } catch (Exception e) {
            resEntity.setCode("InternalError");
            logger.error(e.toString());
        }
        return resEntity;
    }

    @Override
    public ResEntity createMultipartUpload(FileEntity fileEntity) {
        ResEntity resEntity = new ResEntity();
        resEntity.setCode("");
        resEntity.setIsSuccess(false);
        try {
            Boolean isPartFileExit = partFileDao.ifExistRowKey(fileEntity.getUser() + ":" + hbaseProperties.getPartfile_index_table_prefix() + fileEntity.getBucket(), getRowKey(fileEntity.getFileName()));
            Boolean isDataFileExit = objectDao.ifExistRowKey(fileEntity.getUser() + ":" + fileEntity.getBucket(), getRowKey(fileEntity.getFileName()));
            if (isPartFileExit || isDataFileExit) {
                resEntity.setCode("InvalidObjectState");
            } else {
                resEntity = partFileDao.putFile(fileEntity);
            }
        } catch (Exception e) {
            resEntity.setCode("InternalError");
            logger.error(e.toString());
        }
        return resEntity;
    }

    @Override
    public ResEntity appendFile(FileEntity fileEntity) {
        ResEntity resEntity = new ResEntity();
        resEntity.setCode("");
        resEntity.setIsSuccess(false);
        String tableName = fileEntity.getUser() + ":" + fileEntity.getBucket();
        String partfileIndexTableName = fileEntity.getUser() + ":" + hbaseProperties.getPartfile_index_table_prefix() + fileEntity.getBucket();
        String fileName = fileEntity.getFileName();
        String partName = fileEntity.getPartName();
        fileEntity.setPartName(partName + ",");

        try {
            if (objectDao.ifExistRowKey(partfileIndexTableName, getRowKey(fileName))) {
                resEntity.setCode("ObjectNotAppendable");
            } else {
                if (objectDao.ifExistRowKey(tableName, getRowKey(fileName))) {
                    resEntity = objectDao.getAllFileMeta(tableName, getRowKey(fileName));
                    FileEntity fileMeta = (FileEntity) resEntity.getReturnEntity();
                    if (fileMeta.getIsParent() < 1) {
                        resEntity.setIsSuccess(false);
                        resEntity.setCode("ObjectNotAppendable");
                        return resEntity;
                    }
                    else{
                        fileEntity.setFileName(fileName + "|" + partName);
                        resEntity = putFile(fileEntity);
                        resEntity = objectDao.appendColumn(tableName, getRowKey(fileName), "partname", fileEntity.getPartName());
                    }
                }
                else{
                    fileEntity.setFileName(fileName + "|" + partName);
                    resEntity = putFile(fileEntity);
                    fileEntity.setFileName(fileName);
                    fileEntity.setFileSize(0);
                    fileEntity.setIsParent(1);
                    resEntity = objectDao.putAppendFileMeta(fileEntity);
                }
            }
        } catch (Exception e) {
            resEntity.setCode("InternalError");
            logger.error(e.toString());
        }
        return resEntity;
    }

    @Override
    public ResEntity getFile(String user, String bucket, String fileName) {
        return objectDao.getFile(user + ":" + bucket, fileName);
    }

    @Override
    public ResEntity getColumn(String user, String bucket, String fileName, String column) {
        return objectDao.getColumn(user + ":" + bucket, getRowKey(fileName), column);
    }

    @Override
    public ResEntity deleteFile(FileEntity fileEntity) {
        ResEntity resEntity = new ResEntity();
        resEntity.setCode("");
        resEntity.setIsSuccess(false);
        try {
            if (objectDao.ifMultiFile(fileEntity.getUser() + ":" + fileEntity.getBucket(), getRowKey(fileEntity.getFileName()))) {
                resEntity = objectDao.deleteMultipartUploadFile(fileEntity.getUser() + ":" + fileEntity.getBucket(), fileEntity.getFileName());
            } else {
                resEntity = objectDao.deleteFile(fileEntity.getUser() + ":" + fileEntity.getBucket(), getRowKey(fileEntity.getFileName()));
            }
        } catch (Exception e) {
            resEntity.setCode("InternalError");
            logger.error(e.toString());
        }
        return resEntity;
    }

    @Override
    public ResEntity deleteFileList(String bucket, String user, List<DeleteObj> dr) {
        ResEntity resEntity = new ResEntity();
        resEntity.setCode("");
        resEntity.setIsSuccess(false);
        List<DeleteError> errors = new ArrayList<>();
        try {
            for (DeleteObj deleteObj : dr) {
                FileEntity fileEntity = new FileEntity();
                fileEntity.setBucket(bucket);
                fileEntity.setUser(user);
                fileEntity.setFileName(deleteObj.getKey());
                resEntity = deleteFile(fileEntity);
                if (!resEntity.getIsSuccess()) {
                    DeleteError deleteError = new DeleteError();
                    deleteError.setKey(deleteObj.getKey());
                    deleteError.setCode(resEntity.getCode());
                    deleteError.setMessage(ResponseMessageUtil.getResponseMessage(resEntity.getCode()));
                    errors.add(deleteError);
                }
            }
            if (errors.size() > 0) {
                DeleteResponse deleteResponse = new DeleteResponse();
                deleteResponse.setErrors(errors);
                resEntity.setReturnEntity(deleteResponse);
            } else {
                resEntity.setIsSuccess(true);
            }
        } catch (Exception e) {
            resEntity.setCode("InternalError");
            logger.error(e.toString());
        }
        return resEntity;
    }

    @Override
    public ResEntity getObjectList(String user, String bucket, int maxKeys, String prefix, String starttime, String endtime) {
        return null;
    }

    @Override
    public ResEntity abortMultipartUpload(String user, String bucket, String filename) {
        ResEntity resEntity = new ResEntity();
        resEntity.setCode("");
        resEntity.setIsSuccess(false);
        try {
            ResEntity ce = partFileDao.getFile(user + ":" + hbaseProperties.getPartfile_index_table_prefix() + bucket, getRowKey(filename));
            if (ce.getIsSuccess()) {
                partEntity pe = (partEntity) ce.getReturnEntity();
                if (!"".equals(pe.getFileName()) && pe.getFileName() != null) {
                    String partFile = pe.getPartName();
                    String[] partArray = partFile.split(",");
                    for (String part : partArray) {
                        if (!"".equals(part)) {
                            resEntity = objectDao.deleteFile(user + ":" + bucket, getRowKey(filename + "|" + part));
                            if (!resEntity.getIsSuccess()) {
                                break;
                            }
                        }
                    }
                    resEntity = partFileDao.deleteFile(user + ":" + hbaseProperties.getPartfile_index_table_prefix() + bucket, getRowKey(filename));
                }
            } else {
                resEntity.setCode("NoSuchKey");
                logger.error("interrupt Multipart delete ! get partfile error");
            }
            resEntity.setIsSuccess(true);
        } catch (Exception e) {
            resEntity.setCode("InternalError");
            logger.error(e.toString());
        }
        return resEntity;
    }

    @Override
    public ResEntity completeMultipartUpload(String bucket, String user, String filename, List<Part> parts, long timestamp) {
        ResEntity resEntity = new ResEntity();
        resEntity.setCode("");
        resEntity.setIsSuccess(false);
        long fileSize = 0;
        int i = 1;
        try {
            ResEntity ce = partFileDao.getFile(user + ":" + hbaseProperties.getPartfile_index_table_prefix() + bucket, getRowKey(filename));
            if (ce.getIsSuccess()) {
                partEntity pEntity = (partEntity) ce.getReturnEntity();
                String partFile = pEntity.getPartName();
                String[] partArray = partFile.split(",");
                Map partNames = new HashMap<>();
                for (String part : partArray) {
                    if (!"".equals(part)) {
                        partNames.put(getRowKey(filename + "|" + part), "");
                    }
                }
                if (partNames.size() != parts.size()) {
                    logger.error("parts size not equal exist partfile size");
                    resEntity.setCode("InvalidPart");
                } else {
                    StringBuilder partFileName = new StringBuilder();
                    for (Part part : parts) {
                        String key = part.getETag();
                        String rowKey = getRowKey(filename + "|" + i);
                        if (key.equals(rowKey) && partNames.containsKey(key)) {
                            ResEntity res = objectDao.getColumn(user + ":" + bucket, key, "filesize");
                            if (!res.getIsSuccess()) {
                                logger.error("get part key error");
                                resEntity.setCode("InvalidPart");
                                return resEntity;
                            }
                            partFileName.append(i).append(",");
                            ColumnEntity columnEntity = (ColumnEntity) res.getReturnEntity();
                            long partSize = Long.parseLong(columnEntity.getColumn());
                            fileSize = fileSize + partSize;
                        } else {
                            logger.error("partkey not equal partfile");
                            resEntity.setCode("InvalidPart");
                            return resEntity;
                        }
                        i++;
                    }
                    FileEntity fileEntity = new FileEntity();
                    fileEntity.setFileName(pEntity.getFileName());
                    fileEntity.setUser(user);
                    fileEntity.setBucket(bucket);
                    fileEntity.setMessage(pEntity.getMessage());
                    fileEntity.setTimestamp(timestamp);
                    fileEntity.setFileSize(fileSize);
                    fileEntity.setIsParent(1);
                    fileEntity.setFileType(pEntity.getFileType());
                    fileEntity.setPartName(partFileName.toString());
                    fileEntity.setIndexFileNameKey("");
                    fileEntity.setIndexTimeKey("");
                    objectDao.putAllFileMeta(fileEntity);
                    partFileDao.deleteFile(user + ":" + hbaseProperties.getPartfile_index_table_prefix() + bucket, pEntity.getUploadId());
                    resEntity.setIsSuccess(true);
                }
            }
        } catch (Exception e) {
            resEntity.setCode("InternalError");
            logger.error(e.toString());
        }
        return resEntity;
    }

    @Override
    public ResEntity getMultipartUploadObjects(String bucket, int maxUploads, String user, String uploadsPrefix) {
        ResEntity resEntity = new ResEntity();
        resEntity.setCode("");
        resEntity.setIsSuccess(false);
        try {
            ListMultipartUploadsResponse listMultipartUploadsResponse = new ListMultipartUploadsResponse();
            listMultipartUploadsResponse.setTruncated(false);
            listMultipartUploadsResponse.setBucket(bucket);
            listMultipartUploadsResponse.setMaxUploads(maxUploads);
            if (!"".equals(uploadsPrefix)) {
                resEntity = partFileDao.getMultipartUploadObjectsByPrefix(user + ":" + hbaseProperties.getPartfile_index_table_prefix() + bucket, resEntity, uploadsPrefix);
                listMultipartUploadsResponse.setPrefix(uploadsPrefix);
            } else {
                resEntity = partFileDao.getMultipartUploadObjects(user + ":" + hbaseProperties.getPartfile_index_table_prefix() + bucket, resEntity);
            }
            Upload upload;
            List<Upload> maxUploadsList = new ArrayList<>();
            List<Upload> allUploadList = (ArrayList<Upload>) resEntity.getReturnEntity();
            if (allUploadList.size() > 0) {
                allUploadList.sort(new Comparator<Upload>() {
                    @Override
                    public int compare(Upload o1, Upload o2) {
                        return Long.valueOf(o1.getInitiated()).compareTo(Long.valueOf(o2.getInitiated()));
                    }
                });
                if (maxUploads < allUploadList.size()) {
                    listMultipartUploadsResponse.setTruncated(true);
                    for (int i = 0; i < maxUploads; i++) {
                        upload = allUploadList.get(i);
                        upload.setStorageClass("STANDARD");
                        upload.setInitiator(new Initiator(user, DigestUtils.md5DigestAsHex(user.getBytes())));
                        upload.setOwner(new Owner(user, DigestUtils.md5DigestAsHex(user.getBytes())));
                        long timestamp = Long.parseLong(upload.getInitiated());
                        upload.setInitiated(PublicUtil.getUTCTime(timestamp));
                        maxUploadsList.add(i, upload);
                    }
                    listMultipartUploadsResponse.setUpload(maxUploadsList);
                    Upload nextUpload = allUploadList.get(maxUploads);
                    String nextKeyMarker = nextUpload.getKey();
                    String nextUploadIdMarker = nextUpload.getUploadId();
                    listMultipartUploadsResponse.setNextKeyMarker(nextKeyMarker);
                    listMultipartUploadsResponse.setNextUploadIdMarker(nextUploadIdMarker);
                } else {
                    listMultipartUploadsResponse.setTruncated(false);
                    for (int i = 0; i < allUploadList.size(); i++) {
                        upload = allUploadList.get(i);
                        upload.setStorageClass("STANDARD");
                        upload.setInitiator(new Initiator(user, DigestUtils.md5DigestAsHex(user.getBytes())));
                        upload.setOwner(new Owner(user, DigestUtils.md5DigestAsHex(user.getBytes())));
                        long timestamp = Long.parseLong(upload.getInitiated());
                        upload.setInitiated(PublicUtil.getUTCTime(timestamp));
                        allUploadList.set(i, upload);
                    }
                    listMultipartUploadsResponse.setUpload(allUploadList);
                }
            }
            resEntity.setReturnEntity(listMultipartUploadsResponse);
            resEntity.setIsSuccess(true);
        } catch (Exception e) {
            resEntity.setCode("InternalError");
            logger.error(e.toString());
        }
        return resEntity;
    }

    @Override
    public ResEntity getObjectMultipartUploadParts(String bucket, int maxUploads, String user, String fileName, String partNumberMarker, String uploadId) {
        ResEntity resEntity = new ResEntity();
        resEntity.setCode("");
        resEntity.setIsSuccess(false);
        try {
            ListPartsResponse listPartsResponse = new ListPartsResponse();
            listPartsResponse.setBucket(bucket);
            listPartsResponse.setKey(fileName);
            listPartsResponse.setInitiator(new Initiator(user, DigestUtils.md5DigestAsHex(user.getBytes())));
            listPartsResponse.setOwner(new Owner(user, DigestUtils.md5DigestAsHex(user.getBytes())));
            listPartsResponse.setStorageClass("STANDARD");
            listPartsResponse.setTruncated(false);
            listPartsResponse.setMaxParts(maxUploads);
            if (partFileDao.ifExistRowKey(user + ":" + hbaseProperties.getPartfile_index_table_prefix() + bucket, PublicUtil.getRowKey(fileName)) && partFileDao.ifExistRowKey(user + ":" + hbaseProperties.getPartfile_index_table_prefix() + bucket, uploadId)) {
                resEntity.setReturnEntity(listPartsResponse);
                resEntity = partFileDao.getObjectParts(user, bucket, fileName, resEntity);
                listPartsResponse = (ListPartsResponse) resEntity.getReturnEntity();
                List<com.mesalab.hos.entity.getMultipartUpload.Part> partList = listPartsResponse.getPart();
                partList.sort(new Comparator<com.mesalab.hos.entity.getMultipartUpload.Part>() {
                    @Override
                    public int compare(com.mesalab.hos.entity.getMultipartUpload.Part o1, com.mesalab.hos.entity.getMultipartUpload.Part o2) {
                        return o1.getPartNumber() - (o2.getPartNumber());
                    }
                });
                int objectCount = partList.size();
                List<com.mesalab.hos.entity.getMultipartUpload.Part> maxPartsList = new ArrayList<>();
                if (!"".equals(partNumberMarker)) {
                    int intPartNumberMarker = Integer.parseInt(partNumberMarker);
                    if (intPartNumberMarker >= 0) {
                        listPartsResponse.setPartNumberMarker(partNumberMarker);
                        if (maxUploads + intPartNumberMarker < objectCount) {
                            listPartsResponse.setTruncated(true);
                            for (int i = intPartNumberMarker; i < maxUploads + intPartNumberMarker; i++) {
                                com.mesalab.hos.entity.getMultipartUpload.Part part = partList.get(i);
                                long timestamp = Long.parseLong(part.getLastModified());
                                part.setLastModified(PublicUtil.getUTCTime(timestamp));
                                maxPartsList.add(part);
                            }
                            listPartsResponse.setPart(maxPartsList);
                            com.mesalab.hos.entity.getMultipartUpload.Part nextPart = partList.get(maxUploads + intPartNumberMarker);
                            int partNumber = nextPart.getPartNumber();
                            listPartsResponse.setNextPartNumberMarker(partNumber + "");
                        } else {
                            listPartsResponse.setTruncated(false);
                            for (int i = intPartNumberMarker; i < objectCount; i++) {
                                com.mesalab.hos.entity.getMultipartUpload.Part part = partList.get(i);
                                long timestamp = Long.parseLong(part.getLastModified());
                                part.setLastModified(PublicUtil.getUTCTime(timestamp));
                                maxPartsList.add(part);
                            }
                            listPartsResponse.setPart(maxPartsList);
                        }
                        resEntity.setReturnEntity(listPartsResponse);
                    } else {
                        resEntity.setCode("InvalidArgument");
                        return resEntity;
                    }
                } else {
                    if (maxUploads < objectCount) {
                        listPartsResponse.setTruncated(true);
                        for (int i = 0; i < maxUploads; i++) {
                            com.mesalab.hos.entity.getMultipartUpload.Part part = partList.get(i);
                            long timestamp = Long.parseLong(part.getLastModified());
                            part.setLastModified(PublicUtil.getUTCTime(timestamp));
                            maxPartsList.add(i, part);
                        }
                        listPartsResponse.setPart(maxPartsList);
                        com.mesalab.hos.entity.getMultipartUpload.Part nextPart = partList.get(maxUploads);
                        int nextPartNumber = nextPart.getPartNumber();
                        listPartsResponse.setNextPartNumberMarker(nextPartNumber + "");
                    } else {
                        listPartsResponse.setTruncated(false);
                        for (int i = 0; i < objectCount; i++) {
                            com.mesalab.hos.entity.getMultipartUpload.Part part = partList.get(i);
                            long timestamp = Long.parseLong(part.getLastModified());
                            part.setLastModified(PublicUtil.getUTCTime(timestamp));
                            maxPartsList.add(i, part);
                        }
                        listPartsResponse.setPart(maxPartsList);
                    }
                    resEntity.setReturnEntity(listPartsResponse);
                }
                resEntity.setIsSuccess(true);
            } else {
                resEntity.setCode("NoSuchUpload");
                return resEntity;
            }
        } catch (NumberFormatException e) {
            resEntity.setCode("InvalidArgument");
            logger.error(e.toString());
        } catch (Exception e) {
            resEntity.setCode("InternalError");
            logger.error(e.toString());
        }
        return resEntity;
    }

    @Override
    public long getFileSize(String bucket, String filename) {
        return 0;
    }
}
