package com.example.Product_Service.dto.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * Full product detail response — Amazon-style product page payload.
 *
 * Returned by:
 *   GET /api/products/{id}
 *   POST /api/products       (after creation)
 *   PUT  /api/products/{id}  (after update)
 *
 * Contains all product fields including the complete image gallery,
 * specifications map, seller identity, and audit timestamps.
 */
public class ProductResponse {

    private Long id;
    private String name;
    private String shortDescription;
    private String detailedDescription;
    private List<String> imageUrls;
    private BigDecimal price;
    private BigDecimal discountPercent;
    private String brand;
    private String category;
    private Map<String, String> specifications;
    private Long sellerId;
    private String sellerUsername;
    private Boolean available;
    private Double averageRating;
    private Integer totalReviews;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // -----------------------------------------------------------------------
    // Constructors
    // -----------------------------------------------------------------------

    public ProductResponse() {
    }

    // -----------------------------------------------------------------------
    // Getters & Setters
    // -----------------------------------------------------------------------

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getShortDescription() { return shortDescription; }
    public void setShortDescription(String shortDescription) { this.shortDescription = shortDescription; }

    public String getDetailedDescription() { return detailedDescription; }
    public void setDetailedDescription(String detailedDescription) { this.detailedDescription = detailedDescription; }

    public List<String> getImageUrls() { return imageUrls; }
    public void setImageUrls(List<String> imageUrls) { this.imageUrls = imageUrls; }

    public BigDecimal getPrice() { return price; }
    public void setPrice(BigDecimal price) { this.price = price; }

    public BigDecimal getDiscountPercent() { return discountPercent; }
    public void setDiscountPercent(BigDecimal discountPercent) { this.discountPercent = discountPercent; }

    public String getBrand() { return brand; }
    public void setBrand(String brand) { this.brand = brand; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public Map<String, String> getSpecifications() { return specifications; }
    public void setSpecifications(Map<String, String> specifications) { this.specifications = specifications; }

    public Long getSellerId() { return sellerId; }
    public void setSellerId(Long sellerId) { this.sellerId = sellerId; }

    public String getSellerUsername() { return sellerUsername; }
    public void setSellerUsername(String sellerUsername) { this.sellerUsername = sellerUsername; }

    public Boolean getAvailable() { return available; }
    public void setAvailable(Boolean available) { this.available = available; }

    public Double getAverageRating() { return averageRating; }
    public void setAverageRating(Double averageRating) { this.averageRating = averageRating; }

    public Integer getTotalReviews() { return totalReviews; }
    public void setTotalReviews(Integer totalReviews) { this.totalReviews = totalReviews; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }

    // -----------------------------------------------------------------------
    // Builder
    // -----------------------------------------------------------------------

    public static ProductResponseBuilder builder() {
        return new ProductResponseBuilder();
    }

    public static class ProductResponseBuilder {
        private Long id;
        private String name;
        private String shortDescription;
        private String detailedDescription;
        private List<String> imageUrls;
        private BigDecimal price;
        private BigDecimal discountPercent;
        private String brand;
        private String category;
        private Map<String, String> specifications;
        private Long sellerId;
        private String sellerUsername;
        private Boolean available;
        private Double averageRating;
        private Integer totalReviews;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;

        public ProductResponseBuilder id(Long id) { this.id = id; return this; }
        public ProductResponseBuilder name(String name) { this.name = name; return this; }
        public ProductResponseBuilder shortDescription(String shortDescription) { this.shortDescription = shortDescription; return this; }
        public ProductResponseBuilder detailedDescription(String detailedDescription) { this.detailedDescription = detailedDescription; return this; }
        public ProductResponseBuilder imageUrls(List<String> imageUrls) { this.imageUrls = imageUrls; return this; }
        public ProductResponseBuilder price(BigDecimal price) { this.price = price; return this; }
        public ProductResponseBuilder discountPercent(BigDecimal discountPercent) { this.discountPercent = discountPercent; return this; }
        public ProductResponseBuilder brand(String brand) { this.brand = brand; return this; }
        public ProductResponseBuilder category(String category) { this.category = category; return this; }
        public ProductResponseBuilder specifications(Map<String, String> specifications) { this.specifications = specifications; return this; }
        public ProductResponseBuilder sellerId(Long sellerId) { this.sellerId = sellerId; return this; }
        public ProductResponseBuilder sellerUsername(String sellerUsername) { this.sellerUsername = sellerUsername; return this; }
        public ProductResponseBuilder available(Boolean available) { this.available = available; return this; }
        public ProductResponseBuilder averageRating(Double averageRating) { this.averageRating = averageRating; return this; }
        public ProductResponseBuilder totalReviews(Integer totalReviews) { this.totalReviews = totalReviews; return this; }
        public ProductResponseBuilder createdAt(LocalDateTime createdAt) { this.createdAt = createdAt; return this; }
        public ProductResponseBuilder updatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; return this; }

        public ProductResponse build() {
            ProductResponse r = new ProductResponse();
            r.id = this.id;
            r.name = this.name;
            r.shortDescription = this.shortDescription;
            r.detailedDescription = this.detailedDescription;
            r.imageUrls = this.imageUrls;
            r.price = this.price;
            r.discountPercent = this.discountPercent;
            r.brand = this.brand;
            r.category = this.category;
            r.specifications = this.specifications;
            r.sellerId = this.sellerId;
            r.sellerUsername = this.sellerUsername;
            r.available = this.available;
            r.averageRating = this.averageRating;
            r.totalReviews = this.totalReviews;
            r.createdAt = this.createdAt;
            r.updatedAt = this.updatedAt;
            return r;
        }
    }
}
