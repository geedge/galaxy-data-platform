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
@XmlRootElement(name = "InitiateMultipartUploadResult")
// 控制JAXB 绑定类中属性和字段的排序

@XmlType(propOrder = {
        "Bucket",
        "Key",
        "UploadId"
})
public class InitiateMultipartUploadResult implements Serializable {

    private static final long serialVersionUID = 1L;

     private String Bucket;
     private String Key;
     private String UploadId;

    public InitiateMultipartUploadResult(String bucket, String key, String uploadId) {
        Bucket = bucket;
        Key = key;
        UploadId = uploadId;
    }
    public InitiateMultipartUploadResult() {
       super();
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
}
