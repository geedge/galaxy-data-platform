package com.mesalab.hos.entity.MultipartUpload;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import java.io.Serializable;

/**
 * Created by wk1 on 2020/7/20.
 */

@XmlAccessorType(XmlAccessType.FIELD)

// XML文件中的根标识
@XmlRootElement(name = "CompleteMultipartUploadResult")
// 控制JAXB 绑定类中属性和字段的排序

@XmlType(propOrder = {
        "Location",
        "Bucket",
        "Key",
        "ETag"
})
public class CompleteMultipartUploadResult implements Serializable {

    private static final long serialVersionUID = 1L;

     private String Location;
     private String Bucket;
     private String Key;
     private String ETag;

    public CompleteMultipartUploadResult(String location, String bucket, String key, String ETag) {
        Location = location;
        Bucket = bucket;
        Key = key;
        this.ETag = ETag;
    }

    public CompleteMultipartUploadResult() {
       super();
    }

    public String getLocation() {
        return Location;
    }

    public void setLocation(String location) {
        Location = location;
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

    public String getETag() {
        return ETag;
    }

    public void setETag(String ETag) {
        this.ETag = ETag;
    }
}
