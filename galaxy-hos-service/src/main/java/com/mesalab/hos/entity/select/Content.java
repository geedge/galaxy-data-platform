package com.mesalab.hos.entity.select;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Created by wk on 2020/5/12.
 */
@XmlRootElement(name = "Contents")

public class Content {

    private String Key;
    private String LastModified;
    private String ETag;
    private String Size;
    private String StorageClass;

    public Content(String key, String lastModified, String ETag, String size, String storageClass) {
        Key = key;
        LastModified = lastModified;
        this.ETag = ETag;
        Size = size;
        StorageClass = storageClass;
    }

    public Content() {
    }

    @XmlElement(name = "Key")
    public String getKey() {
        return Key;
    }

    public void setKey(String key) {
        Key = key;
    }

    @XmlElement(name = "LastModified")
    public String getLastModified() {
        return LastModified;
    }

    public void setLastModified(String lastModified) {
        LastModified = lastModified;
    }

    @XmlElement(name = "ETag")
    public String getETag() {
        return ETag;
    }

    public void setETag(String ETag) {
        this.ETag = ETag;
    }

    @XmlElement(name = "Size")
    public String getSize() {
        return Size;
    }

    public void setSize(String size) {
        Size = size;
    }

    @XmlElement(name = "StorageClass")
    public String getStorageClass() {
        return StorageClass;
    }

    public void setStorageClass(String storageClass) {
        StorageClass = storageClass;
    }

}
