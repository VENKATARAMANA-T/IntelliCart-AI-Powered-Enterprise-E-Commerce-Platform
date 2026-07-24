package com.example.Product_Service.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * JPA entity representing a product in the catalog.
 *
 * Stored in PostgreSQL table "products".
 * Owner (seller) is permanently set at creation — never updated.
 * Image URLs stored as an @ElementCollection (separate join table).
 * Specifications (key-value pairs) stored as an @ElementCollection map.
 *
 * averageRating and totalReviews are recalculated by ReviewServiceImpl
 * every time a review is added — denormalized for fast display.
 */
@Entity
@Table(name = "products")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // -----------------------------------------------------------------------
    // Core product information
    // -----------------------------------------------------------------------

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String shortDescription;

    @Column(columnDefinition = "TEXT")
    private String detailedDescription;

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal price;

    @Column(precision = 5, scale = 2)
    private BigDecimal discountPercent;

    private String brand;

    private String category;

    // -----------------------------------------------------------------------
    // Images — stored in a separate join table "product_image_urls"
    // -----------------------------------------------------------------------

    @ElementCollection
    @CollectionTable(name = "product_image_urls", joinColumns = @JoinColumn(name = "product_id"))
    @Column(name = "image_url", columnDefinition = "TEXT")
    private List<String> imageUrls = new ArrayList<>();

    // -----------------------------------------------------------------------
    // Specifications — key-value pairs (e.g. {"Color": "Red", "Weight": "2kg"})
    // Stored in a separate join table "product_specifications"
    // -----------------------------------------------------------------------

    @ElementCollection
    @CollectionTable(name = "product_specifications", joinColumns = @JoinColumn(name = "product_id"))
    @MapKeyColumn(name = "spec_key")
    @Column(name = "spec_value", columnDefinition = "TEXT")
    private Map<String, String> specifications = new HashMap<>();

    // -----------------------------------------------------------------------
    // Seller / Ownership — set at creation, NEVER modified
    // -----------------------------------------------------------------------

    @Column(nullable = false, updatable = false)
    private Long sellerId;

    @Column(nullable = false, updatable = false)
    private String sellerUsername;

    // -----------------------------------------------------------------------
    // Review aggregates — denormalized; updated by ReviewServiceImpl
    // -----------------------------------------------------------------------

    @Column(nullable = false)
    private Double averageRating = 0.0;

    @Column(nullable = false)
    private Integer totalReviews = 0;

    // -----------------------------------------------------------------------
    // Audit timestamps
    // -----------------------------------------------------------------------

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    // -----------------------------------------------------------------------
    // Constructors
    // -----------------------------------------------------------------------

    public Product() {
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

    public BigDecimal getPrice() { return price; }
    public void setPrice(BigDecimal price) { this.price = price; }

    public BigDecimal getDiscountPercent() { return discountPercent; }
    public void setDiscountPercent(BigDecimal discountPercent) { this.discountPercent = discountPercent; }

    public String getBrand() { return brand; }
    public void setBrand(String brand) { this.brand = brand; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public List<String> getImageUrls() { return imageUrls; }
    public void setImageUrls(List<String> imageUrls) { this.imageUrls = imageUrls; }

    public Map<String, String> getSpecifications() { return specifications; }
    public void setSpecifications(Map<String, String> specifications) { this.specifications = specifications; }

    public Long getSellerId() { return sellerId; }
    public void setSellerId(Long sellerId) { this.sellerId = sellerId; }

    public String getSellerUsername() { return sellerUsername; }
    public void setSellerUsername(String sellerUsername) { this.sellerUsername = sellerUsername; }

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

    public static ProductBuilder builder() {
        return new ProductBuilder();
    }

    public static class ProductBuilder {
        private String name;
        private String shortDescription;
        private String detailedDescription;
        private BigDecimal price;
        private BigDecimal discountPercent;
        private String brand;
        private String category;
        private List<String> imageUrls = new ArrayList<>();
        private Map<String, String> specifications = new HashMap<>();
        private Long sellerId;
        private String sellerUsername;

        public ProductBuilder name(String name) { this.name = name; return this; }
        public ProductBuilder shortDescription(String shortDescription) { this.shortDescription = shortDescription; return this; }
        public ProductBuilder detailedDescription(String detailedDescription) { this.detailedDescription = detailedDescription; return this; }
        public ProductBuilder price(BigDecimal price) { this.price = price; return this; }
        public ProductBuilder discountPercent(BigDecimal discountPercent) { this.discountPercent = discountPercent; return this; }
        public ProductBuilder brand(String brand) { this.brand = brand; return this; }
        public ProductBuilder category(String category) { this.category = category; return this; }
        public ProductBuilder imageUrls(List<String> imageUrls) { this.imageUrls = imageUrls; return this; }
        public ProductBuilder specifications(Map<String, String> specifications) { this.specifications = specifications; return this; }
        public ProductBuilder sellerId(Long sellerId) { this.sellerId = sellerId; return this; }
        public ProductBuilder sellerUsername(String sellerUsername) { this.sellerUsername = sellerUsername; return this; }

        public Product build() {
            Product product = new Product();
            product.name = this.name;
            product.shortDescription = this.shortDescription;
            product.detailedDescription = this.detailedDescription;
            product.price = this.price;
            product.discountPercent = this.discountPercent;
            product.brand = this.brand;
            product.category = this.category;
            product.imageUrls = this.imageUrls;
            product.specifications = this.specifications;
            product.sellerId = this.sellerId;
            product.sellerUsername = this.sellerUsername;
            product.averageRating = 0.0;
            product.totalReviews = 0;
            return product;
        }
    }
}
