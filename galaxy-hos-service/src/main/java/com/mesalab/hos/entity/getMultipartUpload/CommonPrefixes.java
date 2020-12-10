package com.mesalab.hos.entity.getMultipartUpload;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "CommonPrefixes")
public class CommonPrefixes {
    private String  prefix;

    public CommonPrefixes() {
    }

    @XmlElement(name = "Prefix")
    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }
}
