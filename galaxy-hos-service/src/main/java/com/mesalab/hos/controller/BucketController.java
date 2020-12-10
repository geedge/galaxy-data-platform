package com.mesalab.hos.controller;

import com.mesalab.hos.entity.ErrorResponse;
import com.mesalab.hos.entity.ResEntity;
import com.mesalab.hos.entity.lifecycle.LifecycleRequest;
import com.mesalab.hos.service.BucketService;
import com.mesalab.hos.util.ResponseMessageUtil;
import com.mesalab.hos.util.XmlUtil;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;

import static com.mesalab.hos.util.XmlUtil.convertXmlStrToObject;

@RestController
@Component
@RequestMapping("/hos")
public class BucketController {
    private final org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(this.getClass());

    @Autowired
    private BucketService bucketService;

    @PutMapping(value = "/{bucket}", produces = MediaType.APPLICATION_XML_VALUE)
    public String createOrChangeTTL(@PathVariable("bucket") String bucket, String lifecycle, HttpServletRequest request, HttpServletResponse response, @RequestAttribute("username") String username, @RequestAttribute("requestId") String requestId, @RequestAttribute("hostId") String hostId) {
        ResEntity resEntity;
        ErrorResponse errorResponse = null;
        if (lifecycle != null) {
            try (InputStream inputStream = request.getInputStream()) {
                String xmlBody = IOUtils.toString(inputStream, "UTF-8");
                LifecycleRequest lr = (LifecycleRequest) convertXmlStrToObject(LifecycleRequest.class, xmlBody);
                if (lr != null) {
                    resEntity = bucketService.changeTTL(lr.getRuleObj().getExpirationObj().getDays(), username, bucket);
                    if (!resEntity.getIsSuccess()) {
                        errorResponse = ResponseMessageUtil.getResponseMessage(resEntity.getCode(), hostId, requestId, bucket, "", response);
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
            resEntity = bucketService.createBucket(username, bucket);
            if (resEntity.getIsSuccess()) {
                response.setHeader("Location", "/" + bucket);
            } else {
                errorResponse = ResponseMessageUtil.getResponseMessage(resEntity.getCode(), hostId, requestId, bucket, "", response);
            }
        }
        if (errorResponse == null) {
            return "";
        } else {
            return XmlUtil.convertToXml(errorResponse);
        }
    }

    @DeleteMapping(value = "/{bucket}", produces = MediaType.APPLICATION_XML_VALUE)
    public String truncateOrDelete(@PathVariable("bucket") String bucket, String truncate, HttpServletResponse response, @RequestAttribute("username") String username, @RequestAttribute("requestId") String requestId, @RequestAttribute("hostId") String hostId) {
        ResEntity resEntity;
        ErrorResponse errorResponse = null;
        try {
            if (truncate != null) {
                resEntity = bucketService.truncateData(username, bucket);
                if (!resEntity.getIsSuccess()) {
                    errorResponse = ResponseMessageUtil.getResponseMessage(resEntity.getCode(), hostId, requestId, bucket, "", response);
                }
            }else {
                resEntity = bucketService.deleteBucket(username, bucket);
                if (!resEntity.getIsSuccess()) {
                    errorResponse = ResponseMessageUtil.getResponseMessage(resEntity.getCode(), hostId, requestId, bucket, "", response);
                } else {
                    response.setStatus(204);
                }
            }
        } catch (Exception e) {
            logger.error(requestId + " " + e.toString());
            errorResponse = ResponseMessageUtil.getResponseMessage("InternalError", hostId, requestId, bucket, "", response);
        }
        if (errorResponse == null) {
            return "";
        } else {
            return XmlUtil.convertToXml(errorResponse);
        }
    }

    @GetMapping(value = "/", produces = MediaType.APPLICATION_XML_VALUE)
    public String getBucketList(HttpServletResponse response, @RequestAttribute("username") String username, @RequestAttribute("requestId") String requestId, @RequestAttribute("hostId") String hostId) {
        ResEntity resEntity;
        ErrorResponse errorResponse;
        try {
            resEntity = bucketService.getBucketList(username);
            if (resEntity.getIsSuccess()) {
                return XmlUtil.convertToXml(resEntity.getReturnEntity());
            } else {
                errorResponse = ResponseMessageUtil.getResponseMessage(resEntity.getCode(), hostId, requestId, "", "", response);
            }
        } catch (Exception e) {
            logger.error(requestId + " " + e.toString());
            errorResponse = ResponseMessageUtil.getResponseMessage("InternalError", hostId, requestId, "", "", response);
        }
        return XmlUtil.convertToXml(errorResponse);
    }
}
