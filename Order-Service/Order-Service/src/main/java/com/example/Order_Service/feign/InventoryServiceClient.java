package com.example.Order_Service.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "Inventory-Service")
public interface InventoryServiceClient {

    @PostMapping("/api/inventory/{productId}/deduct")
    void deductStock(@PathVariable("productId") Long productId, @RequestBody StockRequest request);

    @PostMapping("/api/inventory/{productId}/add")
    void addStock(@PathVariable("productId") Long productId, @RequestBody StockRequest request);

    class StockRequest {
        private Integer quantity;

        public StockRequest() {}

        public StockRequest(Integer quantity) {
            this.quantity = quantity;
        }

        public Integer getQuantity() {
            return quantity;
        }

        public void setQuantity(Integer quantity) {
            this.quantity = quantity;
        }
    }
}
