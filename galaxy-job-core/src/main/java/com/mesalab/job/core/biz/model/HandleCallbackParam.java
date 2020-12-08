package com.mesalab.job.core.biz.model;

import java.io.Serializable;


public class HandleCallbackParam implements Serializable {
    private static final long serialVersionUID = 42L;

    private long logId;
    private long logDateTim;

    private com.mesalab.job.core.biz.model.ReturnT<String> executeResult;

    public HandleCallbackParam() {
    }

    public HandleCallbackParam(long logId, long logDateTim, com.mesalab.job.core.biz.model.ReturnT<String> executeResult) {
        this.logId = logId;
        this.logDateTim = logDateTim;
        this.executeResult = executeResult;
    }

    public long getLogId() {
        return logId;
    }

    public void setLogId(long logId) {
        this.logId = logId;
    }

    public long getLogDateTim() {
        return logDateTim;
    }

    public void setLogDateTim(long logDateTim) {
        this.logDateTim = logDateTim;
    }

    public com.mesalab.job.core.biz.model.ReturnT<String> getExecuteResult() {
        return executeResult;
    }

    public void setExecuteResult(com.mesalab.job.core.biz.model.ReturnT<String> executeResult) {
        this.executeResult = executeResult;
    }

    @Override
    public String toString() {
        return "HandleCallbackParam{" +
                "logId=" + logId +
                ", logDateTim=" + logDateTim +
                ", executeResult=" + executeResult +
                '}';
    }

}
