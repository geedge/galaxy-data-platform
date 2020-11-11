package com.mesalab.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@EnableDiscoveryClient
@SpringBootApplication
public class GalaxyBusinessApi {

    public static void main(String[] args) {
        SpringApplication.run(GalaxyBusinessApi.class);
    }

}
