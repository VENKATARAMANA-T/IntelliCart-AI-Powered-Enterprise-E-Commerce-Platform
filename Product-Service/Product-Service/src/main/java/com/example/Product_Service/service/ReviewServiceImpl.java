package com.example.Product_Service.service;

import com.example.Product_Service.dto.request.CreateReviewRequest;
import com.example.Product_Service.dto.response.ReviewResponse;
import com.example.Product_Service.exception.ProductNotFoundException;
import com.example.Product_Service.exception.UnauthorizedException;
import com.example.Product_Service.model.Product;
import com.example.Product_Service.model.Review;
import com.example.Product_Service.repository.ProductRepository;
import com.example.Product_Service.repository.ReviewRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Review business logic implementation.
 *
 * When a review is added:
 *   1. Verify the product exists.
 *   2. Persist the review to product_reviews table (PostgreSQL).
 *   3. Recalculate averageRating using a JPQL AVG query (accurate, DB-level).
 *   4. Update totalReviews count.
 *   5. Save the updated product aggregates.
 *
 * Customer identity (customerId, customerUsername) is captured from the
 * JWT SecurityContext — never from the request body.
 */
@Service
@Transactional
public class ReviewServiceImpl implements ReviewService {

    private static final Logger log = LoggerFactory.getLogger(ReviewServiceImpl.class);

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private ProductRepository productRepository;

    // =========================================================================
    // ADD REVIEW
    // =========================================================================

    @Override
    public ReviewResponse addReview(Long productId, CreateReviewRequest request,
                                    Long customerId, String customerUsername) {
        // 1. Verify product exists
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException(productId));

        // 2. Persist review
        Review review = Review.builder()
                .productId(productId)
                .customerId(customerId)
                .customerUsername(customerUsername)
                .rating(request.getRating())
                .title(request.getTitle())
                .description(request.getDescription())
                .reviewDate(LocalDateTime.now())
                .build();

        Review saved = reviewRepository.save(review);
        log.info("Review added: id={}, productId={}, customer={}, rating={}",
                saved.getId(), productId, customerUsername, request.getRating());

        // 3. Recalculate aggregates on the product (DB-level JPQL average — accurate)
        updateProductRatingAggregates(product, productId);

        return toReviewResponse(saved);
    }

    // =========================================================================
    // GET REVIEWS
    // =========================================================================

    @Override
    @Transactional(readOnly = true)
    public List<ReviewResponse> getReviewsByProductId(Long productId) {
        // Verify product exists before returning its reviews
        if (!productRepository.existsById(productId)) {
            throw new ProductNotFoundException(productId);
        }

        return reviewRepository.findByProductIdOrderByReviewDateDesc(productId)
                .stream()
                .map(this::toReviewResponse)
                .collect(Collectors.toList());
    }

    // =========================================================================
    // EDIT & DELETE REVIEWS
    // =========================================================================

    @Override
    public ReviewResponse editReview(Long productId, Long reviewId, CreateReviewRequest request, Long customerId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException(productId));

        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new IllegalArgumentException("Review not found with id: " + reviewId));

        if (!review.getProductId().equals(productId)) {
            throw new IllegalArgumentException("Review does not belong to this product");
        }

        if (!review.getCustomerId().equals(customerId)) {
            throw new UnauthorizedException("You can only edit your own review");
        }

        review.setRating(request.getRating());
        review.setTitle(request.getTitle());
        review.setDescription(request.getDescription());
        review.setReviewDate(LocalDateTime.now());

        Review saved = reviewRepository.save(review);
        log.info("Review edited: id={}, productId={}, customer={}", saved.getId(), productId, customerId);

        updateProductRatingAggregates(product, productId);

        return toReviewResponse(saved);
    }

    @Override
    public void deleteReview(Long productId, Long reviewId, Long customerId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException(productId));

        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new IllegalArgumentException("Review not found with id: " + reviewId));

        if (!review.getProductId().equals(productId)) {
            throw new IllegalArgumentException("Review does not belong to this product");
        }

        if (!review.getCustomerId().equals(customerId)) {
            throw new UnauthorizedException("You can only delete your own review");
        }

        reviewRepository.delete(review);
        log.info("Review deleted: id={}, productId={}, customer={}", reviewId, productId, customerId);

        updateProductRatingAggregates(product, productId);
    }

    // =========================================================================
    // Private helpers
    // =========================================================================

    /**
     * Recalculates and saves averageRating and totalReviews on the Product entity.
     * Uses JPQL AVG query for accuracy — avoids loading all reviews into memory.
     */
    private void updateProductRatingAggregates(Product product, Long productId) {
        Double avgRating = reviewRepository.findAverageRatingByProductId(productId);
        long count = reviewRepository.countByProductId(productId);

        product.setAverageRating(avgRating != null ? Math.round(avgRating * 10.0) / 10.0 : 0.0);
        product.setTotalReviews((int) count);
        productRepository.save(product);

        log.debug("Product {} rating updated: avg={}, count={}", productId, product.getAverageRating(), count);
    }

    /**
     * Map Review entity → ReviewResponse DTO.
     */
    private ReviewResponse toReviewResponse(Review review) {
        return ReviewResponse.builder()
                .id(review.getId())
                .productId(review.getProductId())
                .customerId(review.getCustomerId())
                .customerUsername(review.getCustomerUsername())
                .rating(review.getRating())
                .title(review.getTitle())
                .description(review.getDescription())
                .reviewDate(review.getReviewDate())
                .build();
    }
}
