package com.example.User_Service.feign;

import com.example.User_Service.dto.response.AuthValidationResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "auth-service")
public interface AuthServiceClient {
    @GetMapping("/auth/validate")
    AuthValidationResponse validateToken(@RequestHeader("Authorization") String token);
}
