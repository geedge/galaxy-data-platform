package com.mesalab.hos.entity.query;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Created by wk on 2020/5/12.
 */
@XmlRootElement(name = "Bucket")

public class Bucket {
    private String CreationDate;
    private String Name;

    public Bucket() {
    }

    public Bucket(String creationDate, String name) {
        CreationDate = creationDate;
        Name = name;
    }

    @XmlElement(name = "CreationDate")
    public String getCreationDate() {
        return CreationDate;
    }

    public void setCreationDate(String creationDate) {
        CreationDate = creationDate;
    }

    @XmlElement(name = "Name")
    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }
}
