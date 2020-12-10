package com.mesalab.hos.dao;

import com.mesalab.hos.entity.ResEntity;
import org.apache.hadoop.conf.Configuration;

import java.io.IOException;

/**
 * Created by wk1 on 2020/7/14.
 */

public interface DiskDao {

    Long getHBaseCurrSize(String path) throws Exception;

    Long getHBaseMaxSize(String path) throws Exception;

}
