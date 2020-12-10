package com.mesalab.hos.service;

import com.mesalab.hos.entity.FileEntity;
import com.mesalab.hos.entity.MultipartUpload.Part;
import com.mesalab.hos.entity.ResEntity;
import com.mesalab.hos.entity.delete.DeleteObj;

import java.util.List;


/**
 * Created by wk on 2020/4/20.
 */
public interface ObjectService {
    ResEntity putFile(FileEntity fileEntity);

    ResEntity createMultipartUpload(FileEntity fileEntity);

    ResEntity putFilePart(FileEntity fileEntity);

    ResEntity appendFile(FileEntity fileEntity);

    ResEntity getFile(String user, String bucket, String fileName);

    ResEntity getColumn(String user, String bucket, String fileName, String column);

    ResEntity deleteFile(FileEntity fileEntity);

    ResEntity deleteFileList(String bucket, String user, List<DeleteObj> dr);

    ResEntity getObjectList(String username, String bucket, int maxKeys, String prefix, String starttime, String endtime);

    ResEntity abortMultipartUpload(String user,String bucket,String filename);

    ResEntity completeMultipartUpload(String bucket, String user,String filename, List<Part> parts,long timestamp);

    ResEntity getMultipartUploadObjects(String bucket, int maxUploads,String user,String uploadsPrefix);

    ResEntity getObjectMultipartUploadParts(String bucket, int maxUploads,String user,String fileName,String partNumberMarker,String uploadId);

    long getFileSize(String bucket,String filename);
}
