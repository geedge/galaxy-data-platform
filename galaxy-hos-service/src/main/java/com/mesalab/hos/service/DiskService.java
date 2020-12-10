package com.mesalab.hos.service;

import com.mesalab.hos.entity.DiskResponse;
import com.mesalab.hos.entity.ResEntity;
import org.apache.hadoop.conf.Configuration;

import java.util.List;

/**
 * Created by wk on 2020/4/20.
 */

public interface DiskService {


     DiskResponse getHBaseSize(List<String> users, String path) ;

    }
