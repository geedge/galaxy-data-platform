package com.mesalab.hos.entity.bucketlist;


import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import java.util.List;

@XmlRootElement(name = "ListAllMyBucketsResult")
@XmlType(propOrder = {"owner", "bucket"})
public class BucketListResponse {
    private BucketOwner owner;
    private List<Bucket> bucket;

    public BucketListResponse() {
    }

    public BucketListResponse(BucketOwner owner, List<Bucket> bucket) {
        this.owner = owner;
        this.bucket = bucket;
    }

    @XmlElement(name = "Owner")
    public BucketOwner getOwner() {
        return owner;
    }

    public void setOwner(BucketOwner owner) {
        this.owner = owner;
    }

    @XmlElementWrapper(name = "Buckets")
    @XmlElement(name = "Bucket")
    public List<Bucket> getBucket() {
        return bucket;
    }

    public void setBucket(List<Bucket> bucket) {
        this.bucket = bucket;
    }
}
