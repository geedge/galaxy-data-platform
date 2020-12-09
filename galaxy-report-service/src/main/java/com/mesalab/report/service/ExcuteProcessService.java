package com.mesalab.report.service;

import com.mesalab.report.bean.JobEntity;

import java.util.List;

/**
 * Created by wk1 on 2020/1/8.
 */
public interface ExcuteProcessService {

    void updateResultMessage(JobEntity job);
    void reSet(JobEntity jobEntity);
    void updateProcessMessage(JobEntity job);
    void killQuery(JobEntity jobEntity);

}
