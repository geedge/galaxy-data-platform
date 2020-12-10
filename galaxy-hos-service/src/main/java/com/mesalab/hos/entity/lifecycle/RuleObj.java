package com.mesalab.hos.entity.lifecycle;

import javax.xml.bind.annotation.XmlElement;


public class RuleObj {
    private String id;
    private String prefix;
    private String status;
    private ExpirationObj expirationObj;

    public RuleObj() {
    }

    @XmlElement(name = "ID")
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @XmlElement(name = "Prefix")
    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    @XmlElement(name = "Status")
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @XmlElement(name = "Expiration")
    public ExpirationObj getExpirationObj() {
        return expirationObj;
    }

    public void setExpirationObj(ExpirationObj expirationObj) {
        this.expirationObj = expirationObj;
    }

    @Override
    public String toString() {
        return "RuleObj{" +
                "id='" + id + '\'' +
                ", prefix='" + prefix + '\'' +
                ", status='" + status + '\'' +
                ", expirationObj=" + expirationObj +
                '}';
    }
}
