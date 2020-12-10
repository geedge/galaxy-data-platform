package com.mesalab.hos.dao;

import com.mesalab.hos.entity.ColumnEntity;
import com.mesalab.hos.entity.select.Content;

import java.util.List;

/**
 * Created by wk on 2020/4/20.
 */
public interface IndexTimeDao {

    Boolean putFile(String tableName, String rowKey, String filename,String timestamp);

    ColumnEntity getColumnWithTime(String tablename, String rowKey, String column);

    Boolean deleteFile(String tableName, String rowKey);

    Boolean ifExistRowKey(String tableName, String rowKey);

    List<Content> getObjectListWithTimeFilter(String tableName, int maxKeys, String startTime, String endTime, String prefix) throws Exception;

}
