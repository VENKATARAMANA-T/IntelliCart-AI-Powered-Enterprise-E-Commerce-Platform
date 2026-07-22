package com.example.Product_Service.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

/**
 * JPA entity representing a customer review for a product.
 *
 * Stored in PostgreSQL table "product_reviews".
 * Linked to Product via productId (no JPA relationship — kept simple
 * for independent query paths and to avoid lazy loading issues).
 *
 * customerId and customerUsername are captured from the JWT at review
 * creation time — no DB lookup to the User table required.
 */
@Entity
@Table(name = "product_reviews")
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // -----------------------------------------------------------------------
    // Link to product
    // -----------------------------------------------------------------------

    @Column(nullable = false)
    private Long productId;

    // -----------------------------------------------------------------------
    // Author info (captured from JWT at creation — denormalized)
    // -----------------------------------------------------------------------

    @Column(nullable = false)
    private Long customerId;

    @Column(nullable = false)
    private String customerUsername;

    // -----------------------------------------------------------------------
    // Review content
    // -----------------------------------------------------------------------

    /**
     * Star rating from 1 (worst) to 5 (best).
     * Validated at the DTO level with @Min(1) @Max(5).
     */
    @Column(nullable = false)
    private Integer rating;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String description;

    @Column(nullable = false)
    private LocalDateTime reviewDate;

    // -----------------------------------------------------------------------
    // Constructors
    // -----------------------------------------------------------------------

    public Review() {
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

    public static ReviewBuilder builder() {
        return new ReviewBuilder();
    }

    public static class ReviewBuilder {
        private Long productId;
        private Long customerId;
        private String customerUsername;
        private Integer rating;
        private String title;
        private String description;
        private LocalDateTime reviewDate;

        public ReviewBuilder productId(Long productId) { this.productId = productId; return this; }
        public ReviewBuilder customerId(Long customerId) { this.customerId = customerId; return this; }
        public ReviewBuilder customerUsername(String customerUsername) { this.customerUsername = customerUsername; return this; }
        public ReviewBuilder rating(Integer rating) { this.rating = rating; return this; }
        public ReviewBuilder title(String title) { this.title = title; return this; }
        public ReviewBuilder description(String description) { this.description = description; return this; }
        public ReviewBuilder reviewDate(LocalDateTime reviewDate) { this.reviewDate = reviewDate; return this; }

        public Review build() {
            Review review = new Review();
            review.productId = this.productId;
            review.customerId = this.customerId;
            review.customerUsername = this.customerUsername;
            review.rating = this.rating;
            review.title = this.title;
            review.description = this.description;
            review.reviewDate = this.reviewDate;
            return review;
        }
    }
}
