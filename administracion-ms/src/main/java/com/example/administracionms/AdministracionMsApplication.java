package com.example.administracionms;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class AdministracionMsApplication {

    public static void main(String[] args) {
        SpringApplication.run(AdministracionMsApplication.class, args);
    }

}
