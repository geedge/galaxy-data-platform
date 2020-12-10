package com.mesalab.hos.entity;

/**
 * Created by wk on 2020/4/20.
 */
public class FileEntity {

    private String fileName;
    private String message;
    private long fileSize;
    private byte[] file;
    private String user;
    private String bucket;
    private String fileType;
    private long timestamp;
    private int partNumber;
    private String uploadId;
    private int isParent;
    private String partName;
    private String indexFileNameKey;
    private String indexTimeKey;

    public FileEntity() {
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public long getFileSize() {
        return fileSize;
    }

    public void setFileSize(long fileSize) {
        this.fileSize = fileSize;
    }

    public byte[] getFile() {
        return file;
    }

    public void setFile(byte[] file) {
        this.file = file;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getBucket() {
        return bucket;
    }

    public void setBucket(String bucket) {
        this.bucket = bucket;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }


    public int getPartNumber() {
        return partNumber;
    }

    public void setPartNumber(int partNumber) {
        this.partNumber = partNumber;
    }

    public String getUploadId() {
        return uploadId;
    }

    public void setUploadId(String uploadId) {
        this.uploadId = uploadId;
    }

    public int getIsParent() {
        return isParent;
    }

    public void setIsParent(int isParent) {
        this.isParent = isParent;
    }

    public String getPartName() {
        return partName;
    }

    public void setPartName(String partName) {
        this.partName = partName;
    }

    public String getIndexFileNameKey() {
        return indexFileNameKey;
    }

    public void setIndexFileNameKey(String indexFileNameKey) {
        this.indexFileNameKey = indexFileNameKey;
    }

    public String getIndexTimeKey() {
        return indexTimeKey;
    }

    public void setIndexTimeKey(String indexTimeKey) {
        this.indexTimeKey = indexTimeKey;
    }
}
