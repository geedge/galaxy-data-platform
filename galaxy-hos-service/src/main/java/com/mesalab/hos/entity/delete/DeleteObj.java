package com.mesalab.hos.entity.delete;

import javax.xml.bind.annotation.XmlElement;

/**
 * Created by wk on 2020/5/11.
 */

public class DeleteObj {

    private String key;

    public DeleteObj(String key) {
        this.key = key;
    }

    public DeleteObj() {
    }

    @XmlElement(name = "Key")
    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
