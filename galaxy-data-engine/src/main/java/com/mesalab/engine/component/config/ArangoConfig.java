package com.mesalab.engine.component.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @description: for http
 * @author: zhq
 * @create: 2020-07-07
 **/
@Data
@Component
public class ArangoConfig {

    @Value("${arango.query.url}")
    private String queryurl;

    @Value("${arango.maxrows}")
    private String maxrows;

}
