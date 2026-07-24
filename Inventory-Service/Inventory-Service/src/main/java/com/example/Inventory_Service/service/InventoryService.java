package com.example.Inventory_Service.service;

import com.example.Inventory_Service.dto.response.StockResponse;
import com.example.Inventory_Service.exception.InsufficientStockException;
import com.example.Inventory_Service.model.InventoryItem;
import com.example.Inventory_Service.repository.InventoryRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class InventoryService {

    private final InventoryRepository inventoryRepository;

    public InventoryService(InventoryRepository inventoryRepository) {
        this.inventoryRepository = inventoryRepository;
    }

    public StockResponse getInventoryByProductId(Long productId) {
        InventoryItem item = inventoryRepository.findByProductId(productId)
                .orElseGet(() -> {
                    InventoryItem newItem = new InventoryItem();
                    newItem.setProductId(productId);
                    newItem.setStockQuantity(0);
                    return inventoryRepository.save(newItem);
                });
        return new StockResponse(item.getProductId(), item.getStockQuantity(), item.getStockQuantity() > 0);
    }

    @Transactional
    public StockResponse deductStock(Long productId, Integer quantity) {
        InventoryItem item = inventoryRepository.findByProductId(productId)
                .orElseThrow(() -> new InsufficientStockException("Product " + productId + " not found in inventory"));

        if (item.getStockQuantity() < quantity) {
            throw new InsufficientStockException("Insufficient stock for product " + productId + ". Available: " + item.getStockQuantity());
        }

        item.setStockQuantity(item.getStockQuantity() - quantity);
        inventoryRepository.save(item);

        return new StockResponse(item.getProductId(), item.getStockQuantity(), item.getStockQuantity() > 0);
    }

    @Transactional
    public StockResponse initStock(Long productId, String productName, String category, String seller, Integer quantity) {
        InventoryItem item = inventoryRepository.findByProductId(productId)
                .orElseGet(() -> {
                    InventoryItem newItem = new InventoryItem();
                    newItem.setProductId(productId);
                    return newItem;
                });

        item.setProductName(productName);
        item.setCategory(category);
        item.setSeller(seller);
        item.setStockQuantity(quantity);
        inventoryRepository.save(item);

        return new StockResponse(item.getProductId(), item.getStockQuantity(), item.getStockQuantity() > 0);
    }

    @Transactional
    public StockResponse addStock(Long productId, Integer quantity) {
        InventoryItem item = inventoryRepository.findByProductId(productId)
                .orElseGet(() -> {
                    InventoryItem newItem = new InventoryItem();
                    newItem.setProductId(productId);
                    newItem.setStockQuantity(0);
                    return newItem;
                });

        item.setStockQuantity(item.getStockQuantity() + quantity);
        inventoryRepository.save(item);

        return new StockResponse(item.getProductId(), item.getStockQuantity(), item.getStockQuantity() > 0);
    }
}
