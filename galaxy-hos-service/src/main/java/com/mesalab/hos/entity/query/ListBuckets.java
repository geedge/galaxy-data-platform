package com.mesalab.hos.entity.query;

import com.mesalab.hos.entity.ResEntity;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

/**
 * Created by wk on 2020/5/12.
 */

@XmlRootElement(name = "ListBucketsOutput")

public class ListBuckets extends ResEntity {

    private List<Bucket> Buckets;
    private BucketOwner Owner;


    @XmlElementWrapper(name = "Buckets")
    @XmlElement(name = "Bucket")
    public List<Bucket> getBuckets() {
        return Buckets;
    }

    public void setBuckets(List<Bucket> buckets) {
        Buckets = buckets;
    }


    @XmlElement(name = "Owner")
    public BucketOwner getOwner() {
        return Owner;
    }

    public void setOwner(BucketOwner owner) {
        Owner = owner;
    }
}
