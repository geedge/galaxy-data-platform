package com.mesalab.report.service;

import com.mesalab.report.bean.HttpResult;
import com.mesalab.report.bean.JobEntity;
import org.apache.hadoop.yarn.webapp.hamlet.HamletSpec;

import java.util.Map;

/**
 * Created by wk1 on 2020/1/2.
 */
public interface HbaseService {


    Boolean put(JobEntity jobEntity) throws Exception;

}
