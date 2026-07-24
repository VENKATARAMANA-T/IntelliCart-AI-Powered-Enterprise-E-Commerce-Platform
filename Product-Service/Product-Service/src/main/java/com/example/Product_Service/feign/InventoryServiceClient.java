package com.example.Product_Service.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Map;

@FeignClient(name = "Inventory-Service")
public interface InventoryServiceClient {

    @PostMapping("/api/inventory/{productId}/init")
    void initStock(@PathVariable("productId") Long productId, @RequestBody InitStockRequest request);

    @GetMapping("/api/inventory/{productId}")
    Map<String, Object> getStock(@PathVariable("productId") Long productId);

    class InitStockRequest {
        private String productName;
        private String category;
        private String seller;
        private Integer quantity;

        public InitStockRequest(String productName, String category, String seller, Integer quantity) {
            this.productName = productName;
            this.category = category;
            this.seller = seller;
            this.quantity = quantity;
        }

        public String getProductName() { return productName; }
        public String getCategory() { return category; }
        public String getSeller() { return seller; }
        public Integer getQuantity() { return quantity; }
    }
}
