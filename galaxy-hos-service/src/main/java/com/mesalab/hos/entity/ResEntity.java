package com.mesalab.hos.entity;

/**
 * Created by wk on 2020/5/12.
 */
public class ResEntity {

    private String code;

    private Boolean isSuccess;

    private Object returnEntity;


    public ResEntity() {
    }

    public ResEntity(String code, Boolean isSuccess, Object returnEntity) {
        this.code = code;
        this.isSuccess = isSuccess;
        this.returnEntity = returnEntity;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Boolean getIsSuccess() {
        return isSuccess;
    }

    public void setIsSuccess(Boolean isSuccess) {
        this.isSuccess = isSuccess;
    }

    public Object getReturnEntity() {
        return returnEntity;
    }

    public void setReturnEntity(Object returnEntity) {
        this.returnEntity = returnEntity;
    }
}
