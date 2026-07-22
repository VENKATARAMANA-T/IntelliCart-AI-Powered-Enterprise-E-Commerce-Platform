package com.example.Product_Service.repository;

import com.example.Product_Service.model.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Spring Data JPA repository for Review entities.
 * Reviews are stored in PostgreSQL (product_reviews table).
 */
@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {

    /**
     * Retrieve all reviews for a given product, ordered newest first.
     */
    List<Review> findByProductIdOrderByReviewDateDesc(Long productId);

    /**
     * Count the total number of reviews for a product.
     * Used when recalculating totalReviews on the Product entity.
     */
    long countByProductId(Long productId);

    /**
     * Calculate the average star rating for a product.
     * Returns null if no reviews exist yet (handled in service layer).
     */
    @Query("SELECT AVG(r.rating) FROM Review r WHERE r.productId = :productId")
    Double findAverageRatingByProductId(@Param("productId") Long productId);
}
