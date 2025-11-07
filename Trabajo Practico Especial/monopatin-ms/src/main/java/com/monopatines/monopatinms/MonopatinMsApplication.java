package com.monopatines.monopatinms;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class MonopatinMsApplication {

	public static void main(String[] args) {
		SpringApplication.run(MonopatinMsApplication.class, args);
	}

}
