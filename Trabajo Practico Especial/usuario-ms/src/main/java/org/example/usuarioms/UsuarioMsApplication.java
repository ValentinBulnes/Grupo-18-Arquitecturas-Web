package org.example.usuarioms;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients
@SpringBootApplication
public class UsuarioMsApplication {

	public static void main(String[] args) {
		SpringApplication.run(UsuarioMsApplication.class, args);
	}

} 
