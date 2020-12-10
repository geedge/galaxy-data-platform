package com.mesalab.hos.entity.upload;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import java.io.Serializable;

/**
 * Created by wk on 2020/4/23.
 */

@XmlAccessorType(XmlAccessType.FIELD)
// XML文件中的根标识
@XmlRootElement(name = "Error")
// 控制JAXB 绑定类中属性和字段的排序

@XmlType(propOrder = {
        "Code",
        "Message",
        "Resource",
        "RequestId"
})
public class UploadResponse implements Serializable {

    private static final long serialVersionUID = 1L;

    private String Code;
    private String Message;
    private String Resource;
    private String RequestId;

    public UploadResponse() {
        super();
    }


    public UploadResponse(String code, String message, String resource, String requestId) {
        super();
        this.Code = code;
        this.Message = message;
        this.Resource = resource;
        this.RequestId = requestId;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public String getCode() {
        return Code;
    }

    public void setCode(String code) {
        Code = code;
    }

    public String getMessage() {
        return Message;
    }

    public void setMessage(String message) {
        Message = message;
    }

    public String getResource() {
        return Resource;
    }

    public void setResource(String resource) {
        Resource = resource;
    }

    public String getRequestId() {
        return RequestId;
    }

    public void setRequestId(String requestId) {
        RequestId = requestId;
    }
}
