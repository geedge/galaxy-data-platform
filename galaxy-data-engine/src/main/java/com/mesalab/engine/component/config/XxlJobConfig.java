package com.mesalab.engine.component.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @Date: 2020-08-19 14:34
 * @Author : liuyongqiang
 * @ClassName : JobAdminConfig
 * @Description : JobAdminConfig
 */
@Data
@Component
public class XxlJobConfig {

    @Value("$xxl.job.admin.url")
    private String url;

    @Value("$xxl.job.admin.username")
    private String userName;

    @Value("$xxl.job.admin.password")
    private String password;
}
