package com.mesalab.hos.entity.query;

import javax.xml.bind.annotation.XmlElement;

/**
 * Created by wk on 2020/5/12.
 */
public class BucketOwner {

    private String DisplayName;
    private String ID;

    public BucketOwner(String displayName, String ID) {
        DisplayName = displayName;
        this.ID = ID;
    }

    public BucketOwner() {

    }

    @XmlElement(name = "DisplayName")
    public String getDisplayName() {
        return DisplayName;
    }

    public void setDisplayName(String displayName) {
        DisplayName = displayName;
    }

    @XmlElement(name = "ID")
    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }
}
