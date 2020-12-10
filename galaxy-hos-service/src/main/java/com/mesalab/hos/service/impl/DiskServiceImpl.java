package com.mesalab.hos.service.impl;

import com.mesalab.hos.config.HbaseProperties;
import com.mesalab.hos.dao.BucketDao;
import com.mesalab.hos.dao.DiskDao;
import com.mesalab.hos.entity.DiskResponse;
import com.mesalab.hos.entity.ResEntity;
import com.mesalab.hos.service.BucketService;
import com.mesalab.hos.service.DiskService;
import org.apache.hadoop.conf.Configuration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by wk1 on 2020/7/14.
 */
@Service
public class DiskServiceImpl implements DiskService {
    private final org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(this.getClass());

    @Autowired
    private DiskDao diskDao;


    public DiskResponse getHBaseSize(List<String> users, String path) {
        DiskResponse diskResponse = new DiskResponse();
        try {
            diskResponse.setHosCapacity(diskDao.getHBaseMaxSize(path));
            Long used = 0L;
            for (String user : users) {
                used += diskDao.getHBaseCurrSize(path + "/data/" + user);
                used += diskDao.getHBaseCurrSize(path + "/mobdir/data/" + user);
            }
            diskResponse.setHosUsed(used);
        }catch (Exception e){
            diskResponse.setHosUsed(-1L);
            diskResponse.setHosCapacity(-1L);
            logger.error(e);
        }
        return diskResponse;
    }

}