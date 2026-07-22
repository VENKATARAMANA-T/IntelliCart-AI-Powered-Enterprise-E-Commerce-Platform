package com.example.Product_Service.controller;

import com.example.Product_Service.dto.request.CreateReviewRequest;
import com.example.Product_Service.dto.response.ReviewResponse;
import com.example.Product_Service.security.AuthenticatedUser;
import com.example.Product_Service.service.ReviewService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Review REST Controller — nested under /api/products/{productId}/reviews.
 *
 * Endpoints:
 *   GET  /api/products/{productId}/reviews  — Get all reviews     [Authenticated]
 *   POST /api/products/{productId}/reviews  — Submit a review     [CUSTOMER only]
 *
 * Design rationale for CUSTOMER-only review submission:
 *   - Sellers should not review their own or competitors' products
 *   - Reviews are a trust signal from real buyers — role separation enforces this
 *
 * customerId and customerUsername are sourced from the JWT principal (AuthenticatedUser)
 * set by JwtAuthenticationFilter — never from the request body.
 */
@RestController
@RequestMapping("/api/products/{productId}/reviews")
public class ReviewController {

    @Autowired
    private ReviewService reviewService;

    /**
     * Retrieve all reviews for a product, ordered newest first.
     * Any authenticated user (CUSTOMER or SELLER) may view reviews.
     */
    @GetMapping
    public ResponseEntity<List<ReviewResponse>> getReviews(@PathVariable Long productId) {
        return ResponseEntity.ok(reviewService.getReviewsByProductId(productId));
    }

    /**
     * Submit a new review for a product.
     * Only authenticated customers may submit reviews.
     * customerId and customerUsername are extracted from the JWT — not the request body.
     */
    @PostMapping
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<ReviewResponse> addReview(
            @PathVariable Long productId,
            @Valid @RequestBody CreateReviewRequest request,
            Authentication authentication) {

        AuthenticatedUser customer = (AuthenticatedUser) authentication.getPrincipal();
        ReviewResponse response = reviewService.addReview(
                productId, request, customer.getUserId(), customer.getUsername());
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * Edit an existing review.
     */
    @PutMapping("/{reviewId}")
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<ReviewResponse> editReview(
            @PathVariable Long productId,
            @PathVariable Long reviewId,
            @Valid @RequestBody CreateReviewRequest request,
            Authentication authentication) {
        
        AuthenticatedUser customer = (AuthenticatedUser) authentication.getPrincipal();
        ReviewResponse response = reviewService.editReview(productId, reviewId, request, customer.getUserId());
        return ResponseEntity.ok(response);
    }

    /**
     * Delete an existing review.
     */
    @DeleteMapping("/{reviewId}")
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<Void> deleteReview(
            @PathVariable Long productId,
            @PathVariable Long reviewId,
            Authentication authentication) {
        
        AuthenticatedUser customer = (AuthenticatedUser) authentication.getPrincipal();
        reviewService.deleteReview(productId, reviewId, customer.getUserId());
        return ResponseEntity.noContent().build();
    }
}
