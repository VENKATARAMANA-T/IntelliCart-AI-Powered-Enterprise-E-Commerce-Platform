package com.example.Product_Service.dto.response;

import java.math.BigDecimal;

/**
 * Lightweight product projection for list views.
 * Contains only the fields needed to render a product card —
 * avoids fetching heavy fields like detailedDescription or all image URLs.
 */
public class ProductSummaryResponse {

    private Long id;
    private String name;
    private String brand;
    private String category;
    private BigDecimal price;
    private BigDecimal discountPercent;
    private Double averageRating;
    private Integer totalReviews;
    private Integer stockCount;
    private String thumbnailUrl;       // First image URL only (or null)
    private String sellerUsername;

    // -----------------------------------------------------------------------
    // Constructors
    // -----------------------------------------------------------------------

    public ProductSummaryResponse() {
    }

    // -----------------------------------------------------------------------
    // Getters & Setters
    // -----------------------------------------------------------------------

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getBrand() { return brand; }
    public void setBrand(String brand) { this.brand = brand; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public BigDecimal getPrice() { return price; }
    public void setPrice(BigDecimal price) { this.price = price; }

    public BigDecimal getDiscountPercent() { return discountPercent; }
    public void setDiscountPercent(BigDecimal discountPercent) { this.discountPercent = discountPercent; }

    public Double getAverageRating() { return averageRating; }
    public void setAverageRating(Double averageRating) { this.averageRating = averageRating; }

    public Integer getTotalReviews() { return totalReviews; }
    public void setTotalReviews(Integer totalReviews) { this.totalReviews = totalReviews; }

    public Integer getStockCount() { return stockCount; }
    public void setStockCount(Integer stockCount) { this.stockCount = stockCount; }

    public String getThumbnailUrl() { return thumbnailUrl; }
    public void setThumbnailUrl(String thumbnailUrl) { this.thumbnailUrl = thumbnailUrl; }

    public String getSellerUsername() { return sellerUsername; }
    public void setSellerUsername(String sellerUsername) { this.sellerUsername = sellerUsername; }

    // -----------------------------------------------------------------------
    // Builder
    // -----------------------------------------------------------------------

    public static ProductSummaryResponseBuilder builder() {
        return new ProductSummaryResponseBuilder();
    }

    public static class ProductSummaryResponseBuilder {
        private Long id;
        private String name;
        private String brand;
        private String category;
        private BigDecimal price;
        private BigDecimal discountPercent;
        private Double averageRating;
        private Integer totalReviews;
        private Integer stockCount;
        private String thumbnailUrl;
        private String sellerUsername;

        public ProductSummaryResponseBuilder id(Long id) { this.id = id; return this; }
        public ProductSummaryResponseBuilder name(String name) { this.name = name; return this; }
        public ProductSummaryResponseBuilder brand(String brand) { this.brand = brand; return this; }
        public ProductSummaryResponseBuilder category(String category) { this.category = category; return this; }
        public ProductSummaryResponseBuilder price(BigDecimal price) { this.price = price; return this; }
        public ProductSummaryResponseBuilder discountPercent(BigDecimal discountPercent) { this.discountPercent = discountPercent; return this; }
        public ProductSummaryResponseBuilder averageRating(Double averageRating) { this.averageRating = averageRating; return this; }
        public ProductSummaryResponseBuilder totalReviews(Integer totalReviews) { this.totalReviews = totalReviews; return this; }
        public ProductSummaryResponseBuilder stockCount(Integer stockCount) { this.stockCount = stockCount; return this; }
        public ProductSummaryResponseBuilder thumbnailUrl(String thumbnailUrl) { this.thumbnailUrl = thumbnailUrl; return this; }
        public ProductSummaryResponseBuilder sellerUsername(String sellerUsername) { this.sellerUsername = sellerUsername; return this; }

        public ProductSummaryResponse build() {
            ProductSummaryResponse r = new ProductSummaryResponse();
            r.id = this.id;
            r.name = this.name;
            r.brand = this.brand;
            r.category = this.category;
            r.price = this.price;
            r.discountPercent = this.discountPercent;
            r.averageRating = this.averageRating;
            r.totalReviews = this.totalReviews;
            r.stockCount = this.stockCount;
            r.thumbnailUrl = this.thumbnailUrl;
            r.sellerUsername = this.sellerUsername;
            return r;
        }
    }
}
