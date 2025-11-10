package com.example.viajems.feignClients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.example.viajems.DTO.MonopatinDTO;

@FeignClient(name = "monopatines-ms", url = "http://localhost:27018")
public interface MonopatinFeignClient {

    @GetMapping("/monopatines/{id}")
    MonopatinDTO getMonopatin(@PathVariable("id") String id);
}
