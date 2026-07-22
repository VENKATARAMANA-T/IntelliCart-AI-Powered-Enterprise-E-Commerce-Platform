package com.example.Order_Service.feign;
import com.example.Order_Service.config.FeignConfig;
import com.example.Order_Service.dto.response.ProductResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
@FeignClient(name = "product-service", configuration = FeignConfig.class)
public interface ProductServiceClient {
    @GetMapping("/api/products/{id}")
    ProductResponse getProductById(@PathVariable("id") Long id);
}
