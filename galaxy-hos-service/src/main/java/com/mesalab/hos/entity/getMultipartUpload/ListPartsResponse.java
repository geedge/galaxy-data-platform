package com.mesalab.hos.entity.getMultipartUpload;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import java.util.List;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "ListPartsResult")
@XmlType(propOrder = {
        "Bucket",
        "Key",
        "UploadId",
        "Initiator",
        "Owner",
        "StorageClass",
        "PartNumberMarker",
        "NextPartNumberMarker",
        "MaxParts",
        "IsTruncated",
        "Part"
})
public class ListPartsResponse {
    private String  Bucket;
    private String  Key;
    private String  UploadId;
    private Initiator  Initiator;
    private Owner Owner;
    private String  StorageClass;
    private String  PartNumberMarker;
    private String  NextPartNumberMarker;
    private int  MaxParts;
    private boolean  IsTruncated;
    private List<Part> Part;

    public ListPartsResponse() {
    }

    public String getBucket() {
        return Bucket;
    }

    public void setBucket(String bucket) {
        Bucket = bucket;
    }

    public String getKey() {
        return Key;
    }

    public void setKey(String key) {
        Key = key;
    }

    public String getUploadId() {
        return UploadId;
    }

    public void setUploadId(String uploadId) {
        UploadId = uploadId;
    }

    public Initiator getInitiator() {
        return Initiator;
    }

    public void setInitiator(Initiator initiator) {
        Initiator = initiator;
    }

    public Owner getOwner() {
        return Owner;
    }

    public void setOwner(Owner owner) {
        Owner = owner;
    }

    public String getStorageClass() {
        return StorageClass;
    }

    public void setStorageClass(String storageClass) {
        StorageClass = storageClass;
    }


    public List<Part> getPart() {
        return Part;
    }

    public void setPart(List<Part> part) {
        Part = part;
    }

    public int getMaxParts() {
        return MaxParts;
    }

    public void setMaxParts(int maxParts) {
        MaxParts = maxParts;
    }

    public boolean isTruncated() {
        return IsTruncated;
    }

    public void setTruncated(boolean truncated) {
        IsTruncated = truncated;
    }

    public String getPartNumberMarker() {
        return PartNumberMarker;
    }

    public void setPartNumberMarker(String partNumberMarker) {
        PartNumberMarker = partNumberMarker;
    }

    public String getNextPartNumberMarker() {
        return NextPartNumberMarker;
    }

    public void setNextPartNumberMarker(String nextPartNumberMarker) {
        NextPartNumberMarker = nextPartNumberMarker;
    }

    public ListPartsResponse(String bucket, String key, String uploadId, com.mesalab.hos.entity.getMultipartUpload.Initiator initiator, com.mesalab.hos.entity.getMultipartUpload.Owner owner, String storageClass, String partNumberMarker, String nextPartNumberMarker, int maxParts, boolean isTruncated, List<com.mesalab.hos.entity.getMultipartUpload.Part> part) {
        Bucket = bucket;
        Key = key;
        UploadId = uploadId;
        Initiator = initiator;
        Owner = owner;
        StorageClass = storageClass;
        PartNumberMarker = partNumberMarker;
        NextPartNumberMarker = nextPartNumberMarker;
        MaxParts = maxParts;
        IsTruncated = isTruncated;
        Part = part;
    }
}
