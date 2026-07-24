package com.example.Inventory_Service.dto.response;

public class StockResponse {
    private Long productId;
    private Integer stockQuantity;
    private boolean inStock;

    public StockResponse(Long productId, Integer stockQuantity, boolean inStock) {
        this.productId = productId;
        this.stockQuantity = stockQuantity;
        this.inStock = inStock;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public Integer getStockQuantity() {
        return stockQuantity;
    }

    public void setStockQuantity(Integer stockQuantity) {
        this.stockQuantity = stockQuantity;
    }

    public boolean isInStock() {
        return inStock;
    }

    public void setInStock(boolean inStock) {
        this.inStock = inStock;
    }
}
