package com.example.User_Service.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "cart-service")
public interface CartServiceClient {

    @DeleteMapping("/api/cart")
    void clearCart(@RequestHeader("Authorization") String token);
}
