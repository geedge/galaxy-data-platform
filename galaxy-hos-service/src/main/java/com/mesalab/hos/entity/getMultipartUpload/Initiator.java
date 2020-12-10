package com.mesalab.hos.entity.getMultipartUpload;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "Initiator")
public class Initiator {
    private String  displayName;
    private String  id;

    public Initiator() {
    }

    public Initiator(String displayName, String id) {
        this.displayName = displayName;
        this.id = id;
    }

    @XmlElement(name = "DisplayName")
    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    @XmlElement(name = "ID")
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
