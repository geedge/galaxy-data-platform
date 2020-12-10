package com.mesalab.hos.entity.select;

import com.mesalab.hos.entity.ResEntity;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

/**
 * Created by wk on 2020/5/12.
 */

@XmlRootElement(name = "ListBucketResult")


public class ListObjects extends ResEntity {

    private String Name;
    private String Prefix;
    private String KeyCount;
    private String MaxKeys;
    private Boolean IsTruncated;
    private List<Content> Contents;

    @XmlElement(name = "Name")
    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    @XmlElement(name = "Prefix")
    public String getPrefix() {
        return Prefix;
    }

    public void setPrefix(String prefix) {
        Prefix = prefix;
    }

    @XmlElement(name = "KeyCount")
    public String getKeyCount() {
        return KeyCount;
    }

    public void setKeyCount(String keyCount) {
        KeyCount = keyCount;
    }

    @XmlElement(name = "MaxKeys")
    public String getMaxKeys() {
        return MaxKeys;
    }

    public void setMaxKeys(String maxKeys) {
        MaxKeys = maxKeys;
    }

    @XmlElement(name = "IsTruncated")
    public Boolean getTruncated() {
        return IsTruncated;
    }

    public void setTruncated(Boolean truncated) {
        IsTruncated = truncated;
    }

    @XmlElement(name = "Contents")
    public List<Content> getContents() {
        return Contents;
    }

    public void setContents(List<Content> contents) {
        Contents = contents;
    }
}
