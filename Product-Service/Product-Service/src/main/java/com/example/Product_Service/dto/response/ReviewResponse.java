package com.example.Product_Service.dto.response;

import java.time.LocalDateTime;

/**
 * Response payload for a single review.
 * Returned by GET and POST /api/products/{productId}/reviews.
 */
public class ReviewResponse {

    private Long id;
    private Long productId;
    private Long customerId;
    private String customerUsername;
    private Integer rating;
    private String title;
    private String description;
    private LocalDateTime reviewDate;

    // -----------------------------------------------------------------------
    // Constructors
    // -----------------------------------------------------------------------

    public ReviewResponse() {
    }

    // -----------------------------------------------------------------------
    // Getters & Setters
    // -----------------------------------------------------------------------

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getProductId() { return productId; }
    public void setProductId(Long productId) { this.productId = productId; }

    public Long getCustomerId() { return customerId; }
    public void setCustomerId(Long customerId) { this.customerId = customerId; }

    public String getCustomerUsername() { return customerUsername; }
    public void setCustomerUsername(String customerUsername) { this.customerUsername = customerUsername; }

    public Integer getRating() { return rating; }
    public void setRating(Integer rating) { this.rating = rating; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public LocalDateTime getReviewDate() { return reviewDate; }
    public void setReviewDate(LocalDateTime reviewDate) { this.reviewDate = reviewDate; }

    // -----------------------------------------------------------------------
    // Builder
    // -----------------------------------------------------------------------

    public static ReviewResponseBuilder builder() {
        return new ReviewResponseBuilder();
    }

    public static class ReviewResponseBuilder {
        private Long id;
        private Long productId;
        private Long customerId;
        private String customerUsername;
        private Integer rating;
        private String title;
        private String description;
        private LocalDateTime reviewDate;

        public ReviewResponseBuilder id(Long id) { this.id = id; return this; }
        public ReviewResponseBuilder productId(Long productId) { this.productId = productId; return this; }
        public ReviewResponseBuilder customerId(Long customerId) { this.customerId = customerId; return this; }
        public ReviewResponseBuilder customerUsername(String customerUsername) { this.customerUsername = customerUsername; return this; }
        public ReviewResponseBuilder rating(Integer rating) { this.rating = rating; return this; }
        public ReviewResponseBuilder title(String title) { this.title = title; return this; }
        public ReviewResponseBuilder description(String description) { this.description = description; return this; }
        public ReviewResponseBuilder reviewDate(LocalDateTime reviewDate) { this.reviewDate = reviewDate; return this; }

        public ReviewResponse build() {
            ReviewResponse r = new ReviewResponse();
            r.id = this.id;
            r.productId = this.productId;
            r.customerId = this.customerId;
            r.customerUsername = this.customerUsername;
            r.rating = this.rating;
            r.title = this.title;
            r.description = this.description;
            r.reviewDate = this.reviewDate;
            return r;
        }
    }
}
