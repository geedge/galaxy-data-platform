package com.mesalab.hos.entity.getMultipartUpload;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import java.util.List;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "ListMultipartUploadsResult")
@XmlType(propOrder = {
        "Bucket",
        "KeyMarker",
        "UploadIdMarker",
        "NextKeyMarker",
        "NextUploadIdMarker",
        "Delimiter",
        "Prefix",
        "MaxUploads",
        "IsTruncated",
        "Upload",
        "CommonPrefixes",
        "EncodingType"
})
public class ListMultipartUploadsResponse {
    private String  Bucket;
    private String  KeyMarker;
    private String  UploadIdMarker;
    private String  NextKeyMarker;
    private String  Prefix;
    private String  Delimiter;
    private String  NextUploadIdMarker;
    private int  MaxUploads;
    private boolean  IsTruncated;
    private List<Upload> Upload;
    private List<CommonPrefixes> CommonPrefixes;
    private String EncodingType;

    public ListMultipartUploadsResponse() {
    }

    public String getBucket() {
        return Bucket;
    }

    public void setBucket(String bucket) {
        Bucket = bucket;
    }

    public String getKeyMarker() {
        return KeyMarker;
    }

    public void setKeyMarker(String keyMarker) {
        KeyMarker = keyMarker;
    }

    public String getUploadIdMarker() {
        return UploadIdMarker;
    }

    public void setUploadIdMarker(String uploadIdMarker) {
        UploadIdMarker = uploadIdMarker;
    }

    public String getNextKeyMarker() {
        return NextKeyMarker;
    }

    public void setNextKeyMarker(String nextKeyMarker) {
        NextKeyMarker = nextKeyMarker;
    }

    public String getPrefix() {
        return Prefix;
    }

    public void setPrefix(String prefix) {
        Prefix = prefix;
    }

    public String getDelimiter() {
        return Delimiter;
    }

    public void setDelimiter(String delimiter) {
        Delimiter = delimiter;
    }

    public String getNextUploadIdMarker() {
        return NextUploadIdMarker;
    }

    public void setNextUploadIdMarker(String nextUploadIdMarker) {
        NextUploadIdMarker = nextUploadIdMarker;
    }

    public int getMaxUploads() {
        return MaxUploads;
    }

    public void setMaxUploads(int maxUploads) {
        MaxUploads = maxUploads;
    }

    public boolean isTruncated() {
        return IsTruncated;
    }

    public void setTruncated(boolean truncated) {
        IsTruncated = truncated;
    }

    public List<com.mesalab.hos.entity.getMultipartUpload.Upload> getUpload() {
        return Upload;
    }

    public void setUpload(List<com.mesalab.hos.entity.getMultipartUpload.Upload> upload) {
        Upload = upload;
    }

    public List<com.mesalab.hos.entity.getMultipartUpload.CommonPrefixes> getCommonPrefixes() {
        return CommonPrefixes;
    }

    public void setCommonPrefixes(List<com.mesalab.hos.entity.getMultipartUpload.CommonPrefixes> commonPrefixes) {
        CommonPrefixes = commonPrefixes;
    }

    public String getEncodingType() {
        return EncodingType;
    }

    public void setEncodingType(String encodingType) {
        EncodingType = encodingType;
    }
}
