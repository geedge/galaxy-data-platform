package com.mesalab.hos.entity.getMultipartUpload;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "Part")
@XmlType(propOrder = {
        "PartNumber",
        "LastModified",
        "ETag",
        "Size"
})
public class Part {
    private int PartNumber;
    private String LastModified;
    private String ETag;
    private int Size;

    public Part() {
    }



    public String getLastModified() {
        return LastModified;
    }

    public void setLastModified(String lastModified) {
        LastModified = lastModified;
    }

    public String getETag() {
        return ETag;
    }

    public void setETag(String ETag) {
        this.ETag = ETag;
    }

    public int getPartNumber() {
        return PartNumber;
    }

    public void setPartNumber(int partNumber) {
        PartNumber = partNumber;
    }

    public int getSize() {
        return Size;
    }

    public void setSize(int size) {
        Size = size;
    }

    public Part(int partNumber, String lastModified, String ETag, int size) {
        PartNumber = partNumber;
        LastModified = lastModified;
        this.ETag = ETag;
        Size = size;
    }
}
