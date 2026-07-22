package com.example.Order_Service.feign;
import com.example.Order_Service.config.FeignConfig;
import com.example.Order_Service.dto.response.CartResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
@FeignClient(name = "cart-service", configuration = FeignConfig.class)
public interface CartServiceClient {
    @GetMapping("/api/cart")
    CartResponse getCart();
    @DeleteMapping("/api/cart")
    void clearCart();
}
