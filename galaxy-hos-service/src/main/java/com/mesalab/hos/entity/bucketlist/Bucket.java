package com.mesalab.hos.entity.bucketlist;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


@XmlRootElement
@XmlType(propOrder = {"name", "creationDate"})
public class Bucket {
    private String creationDate;
    private String name;

    public Bucket() {
    }

    public Bucket(String creationDate, String name) {
        this.creationDate = creationDate;
        this.name = name;
    }

    @XmlElement(name = "CreationDate")
    public String getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(String creationDate) {
        this.creationDate = creationDate;
    }

    @XmlElement(name = "Name")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
