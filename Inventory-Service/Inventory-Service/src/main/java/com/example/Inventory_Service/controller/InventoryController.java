package com.example.Inventory_Service.controller;

import com.example.Inventory_Service.dto.request.StockRequest;
import com.example.Inventory_Service.dto.response.StockResponse;
import com.example.Inventory_Service.service.InventoryService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/inventory")
public class InventoryController {

    private final InventoryService inventoryService;

    public InventoryController(InventoryService inventoryService) {
        this.inventoryService = inventoryService;
    }

    @GetMapping("/{productId}")
    public ResponseEntity<StockResponse> getStock(@PathVariable Long productId) {
        return ResponseEntity.ok(inventoryService.getInventoryByProductId(productId));
    }

    @PostMapping("/{productId}/init")
    public ResponseEntity<StockResponse> initStock(@PathVariable Long productId, @RequestBody com.example.Inventory_Service.dto.request.InitStockRequest request) {
        return ResponseEntity.ok(inventoryService.initStock(productId, request.getProductName(), request.getCategory(), request.getSeller(), request.getQuantity()));
    }

    @PostMapping("/{productId}/deduct")
    public ResponseEntity<StockResponse> deductStock(@PathVariable Long productId, @RequestBody StockRequest request) {
        return ResponseEntity.ok(inventoryService.deductStock(productId, request.getQuantity()));
    }

    @PostMapping("/{productId}/add")
    public ResponseEntity<StockResponse> addStock(@PathVariable Long productId, @RequestBody StockRequest request) {
        return ResponseEntity.ok(inventoryService.addStock(productId, request.getQuantity()));
    }
}
