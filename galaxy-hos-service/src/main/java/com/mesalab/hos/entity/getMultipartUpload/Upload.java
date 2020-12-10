package com.mesalab.hos.entity.getMultipartUpload;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "Upload")
@XmlType(propOrder = {
        "Initiated",
        "Initiator",
        "Key",
        "Owner",
        "StorageClass",
        "UploadId"
})
public class Upload{
    private String  Initiated;
    private Initiator  Initiator;
    private String  Key;
    private Owner Owner;
    private String  StorageClass;
    private String  UploadId;

    public Upload() {
    }

    public String getInitiated() {
        return Initiated;
    }

    public void setInitiated(String initiated) {
        Initiated = initiated;
    }

    public com.mesalab.hos.entity.getMultipartUpload.Initiator getInitiator() {
        return Initiator;
    }

    public void setInitiator(com.mesalab.hos.entity.getMultipartUpload.Initiator initiator) {
        Initiator = initiator;
    }

    public String getKey() {
        return Key;
    }

    public void setKey(String key) {
        Key = key;
    }

    public com.mesalab.hos.entity.getMultipartUpload.Owner getOwner() {
        return Owner;
    }

    public void setOwner(com.mesalab.hos.entity.getMultipartUpload.Owner owner) {
        Owner = owner;
    }

    public String getStorageClass() {
        return StorageClass;
    }

    public void setStorageClass(String storageClass) {
        StorageClass = storageClass;
    }

    public String getUploadId() {
        return UploadId;
    }

    public void setUploadId(String uploadId) {
        UploadId = uploadId;
    }

    public void setUploadId() {
    }
}
