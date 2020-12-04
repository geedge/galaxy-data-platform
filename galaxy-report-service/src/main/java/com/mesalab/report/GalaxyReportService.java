package com.mesalab.report;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@EnableDiscoveryClient
@SpringBootApplication
public class GalaxyReportService {

    public static void main(String[] args) {
        SpringApplication.run(GalaxyReportService.class);
    }
}
