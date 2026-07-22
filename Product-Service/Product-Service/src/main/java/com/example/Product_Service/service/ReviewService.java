package com.example.Product_Service.service;

import com.example.Product_Service.dto.request.CreateReviewRequest;
import com.example.Product_Service.dto.response.ReviewResponse;

import java.util.List;

/**
 * Review business logic contract.
 *
 * Reviews are persisted in PostgreSQL (product_reviews table).
 * After a review is added, the associated Product's averageRating
 * and totalReviews fields are recalculated and saved.
 */
public interface ReviewService {

    /**
     * Add a review for a product.
     * customerId and customerUsername come from the validated JWT —
     * they are NOT trusted from the request body.
     *
     * Throws ProductNotFoundException if the product does not exist.
     */
    ReviewResponse addReview(Long productId, CreateReviewRequest request,
                             Long customerId, String customerUsername);

    /**
     * Retrieve all reviews for a product, ordered newest first.
     * Throws ProductNotFoundException if the product does not exist.
     */
    List<ReviewResponse> getReviewsByProductId(Long productId);

    /**
     * Edit an existing review.
     */
    ReviewResponse editReview(Long productId, Long reviewId, CreateReviewRequest request, Long customerId);

    /**
     * Delete an existing review.
     */
    void deleteReview(Long productId, Long reviewId, Long customerId);
}
