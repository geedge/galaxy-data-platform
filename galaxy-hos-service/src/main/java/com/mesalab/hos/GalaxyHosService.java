package com.mesalab.hos;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@EnableDiscoveryClient
@SpringBootApplication
public class GalaxyHosService {

    public static void main(String[] args) {
        SpringApplication.run(GalaxyHosService.class);
    }
}
