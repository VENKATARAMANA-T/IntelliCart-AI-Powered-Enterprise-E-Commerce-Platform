package com.example.Product_Service.repository;

import com.example.Product_Service.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Spring Data JPA repository for Product entities.
 * All queries are derived from method names — no custom JPQL needed.
 */
@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    /**
     * Find all products owned by a specific seller.
     * Used by the "my products" endpoint — returns all statuses.
     */
    List<Product> findBySellerId(Long sellerId);

    /**
     * Find all products currently available for customers to browse.
     * Used by the public product listing endpoint.
     */
    List<Product> findByAvailableTrue();

    /**
     * Find available products in a specific category.
     * Used for category-based browsing.
     */
    List<Product> findByCategoryAndAvailableTrue(String category);

    /**
     * Search available products by name (case-insensitive contains).
     * Used for basic keyword search.
     */
    List<Product> findByNameContainingIgnoreCaseAndAvailableTrue(String keyword);
}
