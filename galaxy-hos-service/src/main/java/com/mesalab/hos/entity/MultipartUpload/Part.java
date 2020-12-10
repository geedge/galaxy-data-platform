package com.mesalab.hos.entity.MultipartUpload;

import javax.xml.bind.annotation.XmlElement;

/**
 * Created by wk on 2020/5/11.
 */

public class Part {

    private String ETag;
    private int PartNumber;

    public Part(String ETag, int partNumber) {
        this.ETag = ETag;
        PartNumber = partNumber;
    }

    public Part() {
    }

    @XmlElement(name = "ETag")
    public String getETag() {
        return ETag;
    }

    public void setETag(String ETag) {
        this.ETag = ETag;
    }

    @XmlElement(name = "PartNumber")
    public int getPartNumber() {
        return PartNumber;
    }

    public void setPartNumber(int partNumber) {
        PartNumber = partNumber;
    }
}
