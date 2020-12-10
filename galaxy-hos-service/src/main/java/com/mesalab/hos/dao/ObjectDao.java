package com.mesalab.hos.dao;

import com.mesalab.hos.entity.FileEntity;
import com.mesalab.hos.entity.ResEntity;

/**
 * Created by wk on 2020/4/20.
 */
public interface ObjectDao {

    ResEntity putFile(FileEntity fileEntity);

    ResEntity putAppendFileMeta(FileEntity fileEntity);

    ResEntity putAllFileMeta(FileEntity fileEntity);

    ResEntity appendColumn(String tableName, String rowKey,String column,String columnData);

    ResEntity appendIndexKey(FileEntity fileEntity);

    ResEntity putColumn(String tableName, String rowKey,String column,String columnData);

    ResEntity getFile(String tableName, String fileName);

    ResEntity getAllFileMeta(String tableName, String fileName);

    ResEntity getColumn(String tableName, String fileName, String column);

    ResEntity deleteFile(String tableName, String filename);

    ResEntity deleteMultipartUploadFile(String tableName, String filename);

    Boolean ifExistRowKey(String tableName, String rowKey);

    Boolean ifMultiFile(String tableName, String rowKey);
}
