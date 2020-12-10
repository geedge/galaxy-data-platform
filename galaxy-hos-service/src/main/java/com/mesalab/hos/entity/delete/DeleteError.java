package com.mesalab.hos.entity.delete;

import javax.xml.bind.annotation.XmlElement;

/**
 * Created by wk on 2020/5/11.
 */
//@XmlRootElement(name = "Error")
public class DeleteError {

    private String Key;

    private String Code;

    private String Message;


    public DeleteError(String key, String code, String message) {
        Key = key;
        Code = code;
        Message = message;
    }

    public DeleteError() {
    }

    @XmlElement(name = "Key")
    public String getKey() {
        return Key;
    }

    public void setKey(String key) {
        Key = key;
    }

    @XmlElement(name = "Code")
    public String getCode() {
        return Code;
    }

    public void setCode(String code) {
        Code = code;
    }

    @XmlElement(name = "Message")
    public String getMessage() {
        return Message;
    }

    public void setMessage(String message) {
        Message = message;
    }
}
