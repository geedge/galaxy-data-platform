package com.mesalab.hos.dao.Impl;

import com.mesalab.hos.dao.DiskDao;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.ContentSummary;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.FsStatus;
import org.apache.hadoop.fs.Path;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;


/**
 * Created by wk on 2020/4/20.
 */
@Component
@ConditionalOnProperty(value = "hbase.standone", havingValue = "1")
public class DiskDaoClusterImpl implements DiskDao {

    private final org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(this.getClass());
    @Autowired
    @Qualifier("hadoopConfiguration")
    private Configuration hadoopConfiguration;
    @Override
    public Long getHBaseCurrSize(String path) throws Exception {

        Long used;
        try (FileSystem fs = FileSystem.get(hadoopConfiguration)) {
            Path filenamePath = new Path(path);
            ContentSummary in = fs.getContentSummary(filenamePath);
            used=in.getSpaceConsumed();
        } catch (IOException e) {
            logger.error(e);
            throw e;
        }
        return used;
    }

    @Override
    public Long getHBaseMaxSize(String path) throws Exception {
        Long capacity ;
        try (FileSystem fs = FileSystem.get(hadoopConfiguration)) {
            FsStatus fsStatus = fs.getStatus();
            capacity = fsStatus.getCapacity(); //Configured Capacity

        } catch (IOException e) {
            logger.error(e);
            throw e;
        }
        return capacity;
    }



}
