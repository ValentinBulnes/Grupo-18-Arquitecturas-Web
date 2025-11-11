package com.example.viajems;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;


@SpringBootApplication
@EnableFeignClients
public class ViajeMsApplication {

    public static void main(String[] args) {
        SpringApplication.run(ViajeMsApplication.class, args);
    }

}
