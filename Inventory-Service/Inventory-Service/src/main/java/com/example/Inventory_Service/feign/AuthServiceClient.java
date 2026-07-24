package com.example.Inventory_Service.feign;

import com.example.Inventory_Service.dto.response.AuthValidationResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "Auth-Service")
public interface AuthServiceClient {

    @GetMapping("/auth/validate")
    AuthValidationResponse validateToken(@RequestHeader("Authorization") String authHeader);
}
