package com.example.administracionms.feign;

import com.example.administracionms.dto.MonopatinDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@FeignClient(name = "monopatin-ms", url = "http://localhost:8006")
public interface MonopatinFeignClient {
    @GetMapping("/monopatines/")
    List<MonopatinDTO> getAll();
}
