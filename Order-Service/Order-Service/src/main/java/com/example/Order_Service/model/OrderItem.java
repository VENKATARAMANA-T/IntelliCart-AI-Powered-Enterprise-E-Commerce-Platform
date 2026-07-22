package com.example.Order_Service.model;
import jakarta.persistence.*;
import java.math.BigDecimal;
@Entity
@Table(name = "order_items")
public class OrderItem {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY) @JoinColumn(name = "order_id", nullable = false)
    private Order order;
    @Column(nullable = false)
    private Long productId;
    @Column(nullable = false)
    private Long sellerId;
    @Column(nullable = false)
    private String productName;
    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal productPrice;
    @Column(nullable = false)
    private Integer quantity;
    private String thumbnailUrl;
    public Long getId() { return id; } public void setId(Long id) { this.id = id; }
    public Order getOrder() { return order; } public void setOrder(Order order) { this.order = order; }
    public Long getProductId() { return productId; } public void setProductId(Long productId) { this.productId = productId; }
    public Long getSellerId() { return sellerId; } public void setSellerId(Long sellerId) { this.sellerId = sellerId; }
    public String getProductName() { return productName; } public void setProductName(String productName) { this.productName = productName; }
    public BigDecimal getProductPrice() { return productPrice; } public void setProductPrice(BigDecimal productPrice) { this.productPrice = productPrice; }
    public Integer getQuantity() { return quantity; } public void setQuantity(Integer quantity) { this.quantity = quantity; }
    public String getThumbnailUrl() { return thumbnailUrl; } public void setThumbnailUrl(String thumbnailUrl) { this.thumbnailUrl = thumbnailUrl; }
}
