package com.example.Order_Service.dto.response;
import java.math.BigDecimal;
import java.time.LocalDateTime;
public class SellerOrderItemResponse {
    private Long orderId; private Long productId; private String productName; private BigDecimal productPrice; private Integer quantity; private BigDecimal itemTotal; private String orderStatus; private LocalDateTime orderDate;
    public Long getOrderId() { return orderId; } public void setOrderId(Long orderId) { this.orderId = orderId; }
    public Long getProductId() { return productId; } public void setProductId(Long productId) { this.productId = productId; }
    public String getProductName() { return productName; } public void setProductName(String productName) { this.productName = productName; }
    public BigDecimal getProductPrice() { return productPrice; } public void setProductPrice(BigDecimal productPrice) { this.productPrice = productPrice; }
    public Integer getQuantity() { return quantity; } public void setQuantity(Integer quantity) { this.quantity = quantity; }
    public BigDecimal getItemTotal() { return itemTotal; } public void setItemTotal(BigDecimal itemTotal) { this.itemTotal = itemTotal; }
    public String getOrderStatus() { return orderStatus; } public void setOrderStatus(String orderStatus) { this.orderStatus = orderStatus; }
    public LocalDateTime getOrderDate() { return orderDate; } public void setOrderDate(LocalDateTime orderDate) { this.orderDate = orderDate; }
    public static Builder builder() { return new Builder(); }
    public static class Builder {
        private Long orderId; private Long productId; private String productName; private BigDecimal productPrice; private Integer quantity; private BigDecimal itemTotal; private String orderStatus; private LocalDateTime orderDate;
        public Builder orderId(Long orderId) { this.orderId = orderId; return this; }
        public Builder productId(Long productId) { this.productId = productId; return this; }
        public Builder productName(String productName) { this.productName = productName; return this; }
        public Builder productPrice(BigDecimal productPrice) { this.productPrice = productPrice; return this; }
        public Builder quantity(Integer quantity) { this.quantity = quantity; return this; }
        public Builder itemTotal(BigDecimal itemTotal) { this.itemTotal = itemTotal; return this; }
        public Builder orderStatus(String orderStatus) { this.orderStatus = orderStatus; return this; }
        public Builder orderDate(LocalDateTime orderDate) { this.orderDate = orderDate; return this; }
        public SellerOrderItemResponse build() { SellerOrderItemResponse r = new SellerOrderItemResponse(); r.setOrderId(orderId); r.setProductId(productId); r.setProductName(productName); r.setProductPrice(productPrice); r.setQuantity(quantity); r.setItemTotal(itemTotal); r.setOrderStatus(orderStatus); r.setOrderDate(orderDate); return r; }
    }
}
