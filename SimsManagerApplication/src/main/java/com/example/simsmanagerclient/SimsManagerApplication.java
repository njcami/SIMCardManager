package com.example.simsmanagerclient;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@EnableDiscoveryClient
@EnableConfigurationProperties
@SpringBootApplication
public class SimsManagerApplication {

	public static void main(String[] args) {
		SpringApplication.run(SimsManagerApplication.class, args);
	}
}