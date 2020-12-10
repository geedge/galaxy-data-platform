package com.mesalab.hos.controller;

import com.mesalab.hos.config.HbaseProperties;
import com.mesalab.hos.config.HosProperties;
import com.mesalab.hos.entity.DiskResponse;
import com.mesalab.hos.service.DiskService;
import com.mesalab.hos.util.XmlUtil;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.ContentSummary;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.FsStatus;
import org.apache.hadoop.fs.Path;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * Created by wk on 2020/11/30.
 */
@RestController
@Component
public class AdminController {

    @Autowired
    private HosProperties hosProperties;
    @Autowired
    private DiskService diskService;
    @Autowired
    private HbaseProperties hbaseProperties;


    private final org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(this.getClass());

    @GetMapping(value = "/admin/diskspace", produces = MediaType.APPLICATION_XML_VALUE)
    public String getDiskSpace() {

        DiskResponse diskResponse=new DiskResponse();
        try {
            List<String> users = hosProperties.getUsers();
            String path = hbaseProperties.getHbasePath();
            diskResponse = diskService.getHBaseSize(users, path);
        }
        catch (Exception e){
            diskResponse.setHosUsed(-1L);
            diskResponse.setHosCapacity(-1L);
            logger.error(e);
        }
        return XmlUtil.convertToXml(diskResponse);
    }


}
