package com.mesalab.hos.dao;

import com.mesalab.hos.entity.FileEntity;
import com.mesalab.hos.entity.ResEntity;

/**
 * Created by wk on 2020/4/20.
 */
public interface PartFileDao {

    ResEntity putFile(FileEntity fileEntity);

    ResEntity appendColumn(String tableName, String fileName, String columnName, String columnValue);

    ResEntity getFile(String tableName, String fileName);

    ResEntity deleteFile(String tableName, String filename);

    Boolean ifExistRowKey(String tableName, String rowKey);

    ResEntity getMultipartUploadObjects(String tableName,ResEntity resEntity);

    ResEntity getMultipartUploadObjectsByPrefix(String tableName,ResEntity resEntity,String prefix);

    ResEntity getObjectParts(String user,String bucket,String object,ResEntity resEntity);

}
