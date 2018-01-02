package com.playtika.carproxy;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.feign.EnableFeignClients;

@EnableFeignClients
@SpringBootApplication
public class CarProxyApplication {

	public static void main(String[] args) {
		SpringApplication.run(CarProxyApplication.class, args);
	}
}
