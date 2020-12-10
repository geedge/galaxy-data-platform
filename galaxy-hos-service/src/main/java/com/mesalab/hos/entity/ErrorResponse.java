package com.mesalab.hos.entity;

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
        "RequestId",
        "Resource",
        "HostId"
})
public class ErrorResponse implements Serializable {

    private static final long serialVersionUID = 2L;

    private String Code;
    private String Message;
    private String RequestId;
    private String Resource;
    private String HostId;

    public ErrorResponse() {
        super();
    }


    public ErrorResponse(String code, String message, String requestId, String resource, String hostId) {
        super();
        this.Code = code;
        this.Message = message;
        this.RequestId = requestId;
        this.Resource = resource;
        this.HostId = hostId;
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

    public String getRequestId() {
        return RequestId;
    }

    public void setRequestId(String requestId) {
        RequestId = requestId;
    }

    public String getResource() {
        return Resource;
    }

    public void setResource(String resource) {
        Resource = resource;
    }

    public String getHostId() {
        return HostId;
    }

    public void setHostId(String hostId) {
        HostId = hostId;
    }


}
