package com.example.Product_Service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * Product Catalog Service — main entry point.
 *
 * Key annotations:
 *   @EnableFeignClients — activates the AuthServiceClient Feign client
 *                         used for remote JWT validation via Auth-Service.
 *   exclude UserDetailsServiceAutoConfiguration — Product-Service has no local
 *       user store; authentication is fully delegated to Auth-Service.
 */
@SpringBootApplication
@EnableFeignClients
public class ProductServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(ProductServiceApplication.class, args);
    }

}
