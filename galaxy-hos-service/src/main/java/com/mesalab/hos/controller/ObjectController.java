package com.mesalab.hos.controller;

import com.mesalab.hos.config.HosProperties;
import com.mesalab.hos.config.LogChartMetricsFilter;
import com.mesalab.hos.entity.ErrorResponse;
import com.mesalab.hos.entity.FileEntity;
import com.mesalab.hos.entity.MultipartUpload.CompleteMultipartUpload;
import com.mesalab.hos.entity.MultipartUpload.CompleteMultipartUploadResult;
import com.mesalab.hos.entity.MultipartUpload.InitiateMultipartUploadResult;
import com.mesalab.hos.entity.MultipartUpload.Part;
import com.mesalab.hos.entity.ResEntity;
import com.mesalab.hos.entity.ColumnEntity;
import com.mesalab.hos.entity.delete.DeleteObj;
import com.mesalab.hos.entity.delete.DeleteRequest;
import com.mesalab.hos.entity.delete.DeleteResponse;
import com.mesalab.hos.service.ObjectService;
import com.mesalab.hos.util.ResponseMessageUtil;
import com.mesalab.hos.util.XmlUtil;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;

import static com.mesalab.hos.util.PublicUtil.getRowKey;
import static com.mesalab.hos.util.XmlUtil.convertXmlStrToObject;


/**
 * Created by wk on 2020/4/16.
 */
@RestController
@Component
@RequestMapping("/hos")
public class ObjectController {

    private final org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(this.getClass());
    @Autowired
    private ObjectService objectService;
    @Autowired
    private HosProperties hosProperties;

    //上传追加接口
    @PutMapping(value = "/{bucket}/{filename}", produces = MediaType.APPLICATION_XML_VALUE)
    public String uploadOrAppend(@PathVariable("bucket") String bucket, @PathVariable("filename") String fileName, String append, @RequestHeader(name = "x-hos-position", defaultValue = "0") int position, @RequestHeader(name = "Content-Type", defaultValue = "application/octet-stream") String filetype, @RequestHeader(name = "x-hos-meta-message", defaultValue = "") String message, @RequestHeader(name = "x-hos-upload-type", defaultValue = "update") String uploadType, HttpServletRequest request, HttpServletResponse response, @RequestAttribute("username") String username, @RequestAttribute("requestId") String requestId, @RequestAttribute("hostId") String hostId, @RequestParam(value = "partNumber", defaultValue = "0") int PartNumber, @RequestParam(value = "uploadId", defaultValue = "") String UploadId, @RequestHeader(name = "x-hos-time-lastmodified", defaultValue = "0") long lastModiFiedTime) {
        ResEntity resEntity = null;
        ErrorResponse errorResponse = null;
        String responseMessage="";
        byte[] byteFile;
        long timestamp;
        if(lastModiFiedTime==0){
            timestamp = System.currentTimeMillis() / 1000 * 1000;
        }
        else{
            timestamp = lastModiFiedTime;
        }
        try (InputStream inputStream = request.getInputStream()) {
            byteFile = IOUtils.toByteArray(inputStream);
            FileEntity fileEntity = new FileEntity();
            fileEntity.setFileName(fileName);
            fileEntity.setMessage(message);
            fileEntity.setFile(byteFile);
            fileEntity.setUser(username);
            fileEntity.setBucket(bucket);
            fileEntity.setFileType(filetype);
            fileEntity.setTimestamp(timestamp);
            fileEntity.setIsParent(0);
            fileEntity.setIndexFileNameKey("");
            fileEntity.setIndexTimeKey("");
            fileEntity.setPartName("");
            fileEntity.setFileSize(byteFile.length);
            if(PartNumber > hosProperties.getMaxPartNumber()){
                errorResponse = ResponseMessageUtil.getResponseMessage("InvalidRange", hostId, requestId, bucket, fileName, response);
            } else if (PartNumber > 0 && !"".equals(UploadId)) {
                fileEntity.setPartNumber(PartNumber);
                fileEntity.setUploadId(UploadId);
                resEntity = objectService.putFilePart(fileEntity);
                if (resEntity.getIsSuccess()) {
                    String Etag = (String) resEntity.getReturnEntity();
                    response.setHeader("ETag", Etag);
                } else {
                    errorResponse = ResponseMessageUtil.getResponseMessage("InternalError", hostId, requestId, bucket, fileName, response);
                }
            } else {
                if (append == null && uploadType.equals("update")) {
                    logger.info("put object key="+fileName+" filesize="+byteFile.length+" bucket="+bucket);
                    resEntity = objectService.putFile(fileEntity);
                } else {
                    logger.info("append object key="+fileName+" position="+position+" filesize="+byteFile.length+" bucket="+bucket);
                    if(position<=0){
                        errorResponse = ResponseMessageUtil.getResponseMessage("PositionNotEqualToLength", hostId, requestId, bucket, fileName, response);
                    }else if(position > hosProperties.getMaxPosition()){
                        errorResponse = ResponseMessageUtil.getResponseMessage("InvalidRange", hostId, requestId, bucket, fileName, response);
                    }else {
                        fileEntity.setPartName(position+"");
                        fileEntity.setIsParent(1);
                        resEntity = objectService.appendFile(fileEntity);
                    }
                }
                if (resEntity != null && !resEntity.getIsSuccess()) {
                    errorResponse = ResponseMessageUtil.getResponseMessage(resEntity.getCode(), hostId, requestId, bucket, fileName, response);
                }
            }

            try {
                if (fileEntity.getFileSize() < 1024 * 1024) {
                    HosProperties.smallFileCount++;
                } else if (fileEntity.getFileSize() >= 1024 * 1024 && fileEntity.getFileSize() <= 10 * 1024 * 1024) {
                    HosProperties.middleFileCount++;
                } else {
                    HosProperties.bigFileCount++;
                }
                HosProperties.sumPacket = HosProperties.sumPacket + fileEntity.getFileSize();
            }
            catch (Exception e){
                logger.error(requestId + " " + e.toString()+"monitor error");
            }
        } catch (Exception e) {
            logger.error(requestId + " " + e.toString());
            errorResponse = ResponseMessageUtil.getResponseMessage("InternalError", hostId, requestId, bucket, fileName, response);
        }
        try {
            if (errorResponse == null) {
                HosProperties.successCount++;
            } else {
                responseMessage =XmlUtil.convertToXml(errorResponse);
                HosProperties.errorCount++;
            }

        }
        catch (Exception e){
            logger.error(requestId + " " + e.toString()+"monitor error");
        }
        return responseMessage;

    }


    @PostMapping(value = "/{bucket}/{filename}", produces = MediaType.APPLICATION_XML_VALUE)
    public String MultipartUpload(@PathVariable("bucket") String bucket, @PathVariable("filename") String fileName, String uploads, HttpServletRequest request, HttpServletResponse response, @RequestAttribute("username") String username, @RequestAttribute("requestId") String requestId, @RequestAttribute("hostId") String hostId, @RequestHeader(name = "Content-Type", defaultValue = "application/octet-stream") String fileType, @RequestHeader(name = "x-hos-meta-message", defaultValue = "") String message, @RequestParam(value = "uploadId", defaultValue = "") String uploadId) {
        ErrorResponse errorResponse;
        long timestamp = System.currentTimeMillis() / 1000 * 1000;
        if (uploads != null) {
            try {
                FileEntity fileEntity = new FileEntity();
                fileEntity.setFileName(fileName);
                fileEntity.setMessage(message);
                fileEntity.setUser(username);
                fileEntity.setBucket(bucket);
                fileEntity.setFileType(fileType);
                fileEntity.setTimestamp(timestamp);
                fileEntity.setIsParent(1);
                fileEntity.setIndexFileNameKey("");
                fileEntity.setIndexTimeKey("");
                uploadId = getRowKey(fileName);
                ResEntity resEntity = objectService.createMultipartUpload(fileEntity);
                if (resEntity.getIsSuccess()) {
                    InitiateMultipartUploadResult initiateMultipartUploadResult = new InitiateMultipartUploadResult(bucket, fileName, uploadId);
                    return XmlUtil.convertToXml(initiateMultipartUploadResult);
                } else {
                    errorResponse = ResponseMessageUtil.getResponseMessage(resEntity.getCode(), hostId, requestId, bucket, fileName, response);
                }
            } catch (Exception e) {
                logger.error(requestId + " " + e.toString());
                errorResponse = ResponseMessageUtil.getResponseMessage("InternalError", hostId, requestId, bucket, "", response);
            }
        } else if (!"".equals(uploadId)) {
            String xmlBody;
            try (InputStream inputStream = request.getInputStream()) {
                xmlBody = IOUtils.toString(inputStream, StandardCharsets.UTF_8);
                CompleteMultipartUpload cmu = (CompleteMultipartUpload) convertXmlStrToObject(CompleteMultipartUpload.class, xmlBody);
                if (cmu != null) {
                    List<Part> completeListObjects = cmu.getParts();
                    ResEntity resEntity = objectService.completeMultipartUpload(bucket, username, fileName, completeListObjects, timestamp);
                    if (!resEntity.getIsSuccess()) {
                        errorResponse = ResponseMessageUtil.getResponseMessage(resEntity.getCode(), hostId, requestId, bucket, "", response);
                    } else {
                        CompleteMultipartUploadResult output = new CompleteMultipartUploadResult();
                        output.setLocation("http://" + hostId + ":" + hosProperties.getServer_port() + "/hos/" + bucket + "/" + fileName);
                        output.setKey(fileName);
                        output.setBucket(bucket);
                        output.setETag('"' + getRowKey(fileName) + '"');
                        return XmlUtil.convertToXml(output);
                    }
                } else {
                    errorResponse = ResponseMessageUtil.getResponseMessage("MalformedXML", hostId, requestId, bucket, "", response);
                }
            } catch (IOException e) {
                logger.error(requestId + " " + e.toString());
                errorResponse = ResponseMessageUtil.getResponseMessage("MalformedXML", hostId, requestId, bucket, "", response);
            } catch (Exception e) {
                logger.error(requestId + " " + e.toString());
                errorResponse = ResponseMessageUtil.getResponseMessage("InternalError", hostId, requestId, bucket, "", response);
            }
        } else {
            response.setStatus(405);
            return "";
        }
        return XmlUtil.convertToXml(errorResponse);
    }

    //下载文件信息接口
    @GetMapping(value = "/{bucket}/{filename}", produces = MediaType.APPLICATION_XML_VALUE)
    public String downloadObj(@PathVariable("bucket") String bucket, @PathVariable("filename") String fileName, String metaData, @RequestParam(name = "uploadId", defaultValue = "") String uploadId, @RequestParam(name = "max-parts", defaultValue = "1000") int maxParts, @RequestParam(name = "part-number-marker", defaultValue = "") String partNumberMarker, HttpServletResponse response, @RequestAttribute("username") String username, @RequestAttribute("requestId") String requestId, @RequestAttribute("hostId") String hostId) {
        ResEntity resEntity;
        ErrorResponse errorResponse = null;
        response.setHeader("Content-Disposition", "attachment;fileName=" + fileName);// 设置文件名
        if (!"".equals(uploadId)) {
            if (maxParts > hosProperties.getMaxResultLimit()) {
                errorResponse = ResponseMessageUtil.getResponseMessage("InventoryFull", hostId, requestId, bucket, "", response);
            } else if (maxParts < 0) {
                errorResponse = ResponseMessageUtil.getResponseMessage("InvalidArgument", hostId, requestId, bucket, "", response);
            } else {
                resEntity = objectService.getObjectMultipartUploadParts(bucket, maxParts, username, fileName, partNumberMarker, uploadId);
                if (resEntity.getIsSuccess()) {
                    return XmlUtil.convertToXml(resEntity.getReturnEntity());
                } else {
                    errorResponse = ResponseMessageUtil.getResponseMessage(resEntity.getCode(), hostId, requestId, bucket, "", response);
                }
            }
        } else {
            if (metaData == null) {
                resEntity = objectService.getFile(username, bucket, fileName);
                FileEntity fileEntity = (FileEntity) resEntity.getReturnEntity();
                if (resEntity.getIsSuccess()) {
                    try (OutputStream outputStream = response.getOutputStream()) {
                        response.setContentLength(fileEntity.getFile().length);
                        response.setHeader(hosProperties.getMetaHeader(), fileEntity.getMessage());// 设置文件名
                        response.setContentType(fileEntity.getFileType());
                        IOUtils.write(fileEntity.getFile(), outputStream);
                    } catch (Exception e) {
                        logger.error(requestId + " " + e.toString());
                        errorResponse = ResponseMessageUtil.getResponseMessage("InternalError", hostId, requestId, bucket, fileName, response);
                    }
                } else {
                    errorResponse = ResponseMessageUtil.getResponseMessage(resEntity.getCode(), hostId, requestId, bucket, fileName, response);
                }
            } else {
                resEntity = objectService.getColumn(username, bucket, getRowKey(fileName), "message");
                if (resEntity.getIsSuccess()) {
                    ColumnEntity ce = (ColumnEntity) resEntity.getReturnEntity();
                    response.setHeader(hosProperties.getMetaHeader(), ce.getColumn());
                } else {
                    errorResponse = ResponseMessageUtil.getResponseMessage(resEntity.getCode(), hostId, requestId, bucket, fileName, response);
                }
            }
        }
        if (errorResponse == null) {
            return "";
        } else {
            return XmlUtil.convertToXml(errorResponse);
        }
    }

    @DeleteMapping(value = "/{bucket}/{filename}", produces = MediaType.APPLICATION_XML_VALUE)
    public String deleteObj(@PathVariable("bucket") String bucket, @PathVariable("filename") String fileName, @RequestParam(value = "uploadId", defaultValue = "") String uploadId, HttpServletResponse response, @RequestAttribute("username") String username, @RequestAttribute("requestId") String requestId, @RequestAttribute("hostId") String hostId) {
        ResEntity resEntity;
        ErrorResponse errorResponse = null;
        FileEntity fileEntity = new FileEntity();
        fileEntity.setFileName(fileName);
        fileEntity.setBucket(bucket);
        fileEntity.setUser(username);
        try {
            if ("".equals(uploadId)) {
                resEntity = objectService.deleteFile(fileEntity);
                if (!resEntity.getIsSuccess()) {
                    errorResponse = ResponseMessageUtil.getResponseMessage(resEntity.getCode(), hostId, requestId, bucket, fileName, response);
                } else {
                    response.setStatus(204);
                }
            } else {
                resEntity = objectService.abortMultipartUpload(username, bucket, fileName);
                if (!resEntity.getIsSuccess()) {
                    errorResponse = ResponseMessageUtil.getResponseMessage("InternalError", hostId, requestId, bucket, fileName, response);
                }else {
                    response.setStatus(204);
                }
            }
        } catch (Exception e) {
            logger.error(requestId + " " + e.toString());
            errorResponse = ResponseMessageUtil.getResponseMessage("InternalError", hostId, requestId, bucket, fileName, response);
        }
        if (errorResponse == null) {
            return "";
        } else {
            return XmlUtil.convertToXml(errorResponse);
        }
    }

    @PostMapping(value = "/{bucket}", produces = MediaType.APPLICATION_XML_VALUE)
    public String deleteMultipleObj(@PathVariable("bucket") String bucket, String delete, HttpServletRequest request, HttpServletResponse response, @RequestAttribute("username") String username, @RequestAttribute("requestId") String requestId, @RequestAttribute("hostId") String hostId) {
        ResEntity resEntity;
        ErrorResponse errorResponse;
        if (delete != null) {
            String xmlBody;
            try (InputStream inputStream = request.getInputStream()) {
                xmlBody = IOUtils.toString(inputStream, "UTF-8");
                DeleteRequest deleteRequest = (DeleteRequest) convertXmlStrToObject(DeleteRequest.class, xmlBody);
                if (deleteRequest != null) {
                    List<DeleteObj> deleteListObjects = deleteRequest.getObjects();
                    if (deleteListObjects.size() > hosProperties.getDeleteMultipleNumber()) {
                        errorResponse = ResponseMessageUtil.getResponseMessage("InventoryFull", hostId, requestId, bucket, "", response);
                    } else {
                        resEntity = objectService.deleteFileList(bucket, username, deleteListObjects);
                        if (!resEntity.getIsSuccess()) {
                            return XmlUtil.convertToXml(resEntity.getReturnEntity());
                        } else {
                            DeleteResponse deleteResponse = new DeleteResponse();
                            return XmlUtil.convertToXml(deleteResponse);
                        }
                    }
                } else {
                    errorResponse = ResponseMessageUtil.getResponseMessage("MalformedXML", hostId, requestId, bucket, "", response);
                }
            } catch (IOException e) {
                logger.error(requestId + " " + e.toString());
                errorResponse = ResponseMessageUtil.getResponseMessage("MalformedXML", hostId, requestId, bucket, "", response);
            } catch (Exception e) {
                logger.error(requestId + " " + e.toString());
                errorResponse = ResponseMessageUtil.getResponseMessage("InternalError", hostId, requestId, bucket, "", response);
            }
        } else {
            response.setStatus(405);
            return "";
        }
        return XmlUtil.convertToXml(errorResponse);
    }


    @GetMapping(value = "/{bucket}/", produces = MediaType.APPLICATION_XML_VALUE)
    public String getObjectList(HttpServletResponse response, String uploads, @RequestParam(name = "max-uploads", defaultValue = "1000") int maxUploads, @RequestParam(name = "delimiter", defaultValue = "") String delimiter, @RequestParam(name = "key-marker", defaultValue = "") String keyMarker, @RequestParam(name = "prefix", defaultValue = "") String uploadsPrefix, @RequestParam(name = "upload-id-marker", defaultValue = "") String uploadIdMarker, @PathVariable("bucket") String bucket, @RequestParam(name = "max-keys", defaultValue = "1000") int maxKeys, @RequestParam(name = "prefix", defaultValue = "") String prefix, @RequestParam(name = "start-after", defaultValue = "0_0") String startAfter, @RequestAttribute("username") String username, @RequestAttribute("requestId") String requestId, @RequestAttribute("hostId") String hostId) {

        ResEntity resEntity;
        ErrorResponse errorResponse;
        try {
            if (uploads != null) {
                if (maxUploads > hosProperties.getMaxResultLimit()) {
                    errorResponse = ResponseMessageUtil.getResponseMessage("InventoryFull", hostId, requestId, bucket, "", response);
                } else if (maxUploads < 0) {
                    errorResponse = ResponseMessageUtil.getResponseMessage("InvalidArgument", hostId, requestId, bucket, "", response);
                } else {
                    resEntity = objectService.getMultipartUploadObjects(bucket, maxUploads, username, uploadsPrefix);
                    if (resEntity.getIsSuccess()) {
                        return XmlUtil.convertToXml(resEntity.getReturnEntity());
                    } else {
                        errorResponse = ResponseMessageUtil.getResponseMessage(resEntity.getCode(), hostId, requestId, bucket, "", response);
                    }
                }
            } else {
                if (maxKeys < hosProperties.getMaxResultLimit()) {
                    String[] timeRange = startAfter.split("_");
                    String startTime = timeRange[0];
                    String endTime = timeRange[1];
                    resEntity = objectService.getObjectList(username, bucket, maxKeys, prefix, startTime, endTime);
                    if (resEntity.getIsSuccess()) {
                        return XmlUtil.convertToXml(resEntity.getReturnEntity());
                    } else {
                        errorResponse = ResponseMessageUtil.getResponseMessage(resEntity.getCode(), hostId, requestId, bucket, "", response);
                    }
                } else {
                    errorResponse = ResponseMessageUtil.getResponseMessage("InlineDataTooLarge", hostId, requestId, bucket, "", response);
                }
            }
        } catch (Exception e) {
            logger.error(requestId + " " + e.toString());
            errorResponse = ResponseMessageUtil.getResponseMessage("InternalError", hostId, requestId, bucket, "", response);
        }
        return XmlUtil.convertToXml(errorResponse);
    }


}
