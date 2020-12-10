package com.mesalab.hos.service;

import com.mesalab.hos.entity.ResEntity;

/**
 * Created by wk on 2020/4/20.
 */

public interface BucketService {

    ResEntity changeTTL(int day, String user, String bucket);

    ResEntity truncateData(String user, String bucket);

    ResEntity getBucketList(String user);

    ResEntity createBucket(String user, String bucket);

    ResEntity deleteBucket(String user, String bucket);

}
