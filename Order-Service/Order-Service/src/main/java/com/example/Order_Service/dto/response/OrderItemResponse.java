package com.example.Order_Service.dto.response;
import java.math.BigDecimal;
public class OrderItemResponse {
    private Long id; private Long productId; private Long sellerId; private String productName; private BigDecimal productPrice; private Integer quantity; private BigDecimal itemTotal; private String thumbnailUrl;
    public Long getId() { return id; } public void setId(Long id) { this.id = id; }
    public Long getProductId() { return productId; } public void setProductId(Long productId) { this.productId = productId; }
    public Long getSellerId() { return sellerId; } public void setSellerId(Long sellerId) { this.sellerId = sellerId; }
    public String getProductName() { return productName; } public void setProductName(String productName) { this.productName = productName; }
    public BigDecimal getProductPrice() { return productPrice; } public void setProductPrice(BigDecimal productPrice) { this.productPrice = productPrice; }
    public Integer getQuantity() { return quantity; } public void setQuantity(Integer quantity) { this.quantity = quantity; }
    public BigDecimal getItemTotal() { return itemTotal; } public void setItemTotal(BigDecimal itemTotal) { this.itemTotal = itemTotal; }
    public String getThumbnailUrl() { return thumbnailUrl; } public void setThumbnailUrl(String thumbnailUrl) { this.thumbnailUrl = thumbnailUrl; }
    public static Builder builder() { return new Builder(); }
    public static class Builder {
        private Long id; private Long productId; private Long sellerId; private String productName; private BigDecimal productPrice; private Integer quantity; private BigDecimal itemTotal; private String thumbnailUrl;
        public Builder id(Long id) { this.id = id; return this; }
        public Builder productId(Long productId) { this.productId = productId; return this; }
        public Builder sellerId(Long sellerId) { this.sellerId = sellerId; return this; }
        public Builder productName(String productName) { this.productName = productName; return this; }
        public Builder productPrice(BigDecimal productPrice) { this.productPrice = productPrice; return this; }
        public Builder quantity(Integer quantity) { this.quantity = quantity; return this; }
        public Builder itemTotal(BigDecimal itemTotal) { this.itemTotal = itemTotal; return this; }
        public Builder thumbnailUrl(String thumbnailUrl) { this.thumbnailUrl = thumbnailUrl; return this; }
        public OrderItemResponse build() { OrderItemResponse r = new OrderItemResponse(); r.setId(id); r.setProductId(productId); r.setSellerId(sellerId); r.setProductName(productName); r.setProductPrice(productPrice); r.setQuantity(quantity); r.setItemTotal(itemTotal); r.setThumbnailUrl(thumbnailUrl); return r; }
    }
}
