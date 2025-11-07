package com.monopatines.monopatinms.feignClients;
import com.monopatines.monopatinms.DTO.ParadaDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name= "parada-ms", url = "http://localhost:8005")
public interface ParadaFeignClient {

    @GetMapping("/paradas/{id}")
    ParadaDTO findById(@PathVariable Long id);

}
