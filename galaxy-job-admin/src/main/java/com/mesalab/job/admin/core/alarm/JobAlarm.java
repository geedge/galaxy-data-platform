package com.mesalab.job.admin.core.alarm;

import com.mesalab.job.admin.core.model.XxlJobInfo;
import com.mesalab.job.admin.core.model.XxlJobLog;


public interface JobAlarm {

    /**
     * job alarm
     *
     * @param info
     * @param jobLog
     * @return
     */
    boolean doAlarm(XxlJobInfo info, XxlJobLog jobLog);

}
