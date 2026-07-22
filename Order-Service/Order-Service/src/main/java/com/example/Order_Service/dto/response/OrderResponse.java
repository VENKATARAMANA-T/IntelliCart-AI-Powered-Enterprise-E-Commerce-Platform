package com.example.Order_Service.dto.response;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
public class OrderResponse {
    private Long id; private Long customerId; private BigDecimal totalAmount; private String shippingAddress; private String status; private List<OrderItemResponse> items; private LocalDateTime createdAt; private LocalDateTime updatedAt;
    public Long getId() { return id; } public void setId(Long id) { this.id = id; }
    public Long getCustomerId() { return customerId; } public void setCustomerId(Long customerId) { this.customerId = customerId; }
    public BigDecimal getTotalAmount() { return totalAmount; } public void setTotalAmount(BigDecimal totalAmount) { this.totalAmount = totalAmount; }
    public String getShippingAddress() { return shippingAddress; } public void setShippingAddress(String shippingAddress) { this.shippingAddress = shippingAddress; }
    public String getStatus() { return status; } public void setStatus(String status) { this.status = status; }
    public List<OrderItemResponse> getItems() { return items; } public void setItems(List<OrderItemResponse> items) { this.items = items; }
    public LocalDateTime getCreatedAt() { return createdAt; } public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; } public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
    public static Builder builder() { return new Builder(); }
    public static class Builder {
        private Long id; private Long customerId; private BigDecimal totalAmount; private String shippingAddress; private String status; private List<OrderItemResponse> items; private LocalDateTime createdAt; private LocalDateTime updatedAt;
        public Builder id(Long id) { this.id = id; return this; }
        public Builder customerId(Long customerId) { this.customerId = customerId; return this; }
        public Builder totalAmount(BigDecimal totalAmount) { this.totalAmount = totalAmount; return this; }
        public Builder shippingAddress(String shippingAddress) { this.shippingAddress = shippingAddress; return this; }
        public Builder status(String status) { this.status = status; return this; }
        public Builder items(List<OrderItemResponse> items) { this.items = items; return this; }
        public Builder createdAt(LocalDateTime createdAt) { this.createdAt = createdAt; return this; }
        public Builder updatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; return this; }
        public OrderResponse build() { OrderResponse r = new OrderResponse(); r.setId(id); r.setCustomerId(customerId); r.setTotalAmount(totalAmount); r.setShippingAddress(shippingAddress); r.setStatus(status); r.setItems(items); r.setCreatedAt(createdAt); r.setUpdatedAt(updatedAt); return r; }
    }
}
