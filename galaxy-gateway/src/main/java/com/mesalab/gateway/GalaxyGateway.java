package com.mesalab.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@EnableDiscoveryClient
@SpringBootApplication
public class GalaxyGateway {


    public static void main(String[] args) {
        SpringApplication.run(GalaxyGateway.class);
    }
}
