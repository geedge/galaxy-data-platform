package com.mesalab.authcenter;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@EnableDiscoveryClient
@SpringBootApplication
public class GalaxyAuthCenter {

    public static void main(String[] args) {
        SpringApplication.run(GalaxyAuthCenter.class);
    }

}
