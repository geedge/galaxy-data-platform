package com.mesalab.hos.entity;

/**
 * Created by wk1 on 2020/7/13.
 */
public class ColumnEntity {

    private String column;

    private Long timestamp;

    public String getColumn() {
        return column;
    }

    public void setColumn(String column) {
        this.column = column;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }
}
