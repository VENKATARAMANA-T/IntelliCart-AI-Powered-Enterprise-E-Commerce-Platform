package com.example.Product_Service.service;

import com.example.Product_Service.dto.request.CreateProductRequest;
import com.example.Product_Service.dto.request.UpdateProductRequest;
import com.example.Product_Service.dto.response.ProductResponse;
import com.example.Product_Service.dto.response.ProductSummaryResponse;

import java.util.List;

/**
 * Product catalog business logic contract.
 *
 * All ownership and authorization checks beyond Spring Security role enforcement
 * (e.g. verifying that the requesting seller owns the product) are performed here.
 */
public interface ProductService {

    /**
     * Create a new product for the authenticated seller.
     * sellerId and sellerUsername are sourced from the validated JWT —
     * they are NOT accepted from the request body.
     */
    ProductResponse createProduct(CreateProductRequest request, Long sellerId, String sellerUsername);

    /**
     * Browse all available products. Accessible to authenticated users of any role.
     */
    List<ProductSummaryResponse> getAllProducts();

    /**
     * View full product details for a single product.
     */
    ProductResponse getProductById(Long id);

    /**
     * Update a product. Only the owner (seller with matching sellerId) may update.
     * Throws UnauthorizedException if another seller attempts to update.
     */
    ProductResponse updateProduct(Long id, UpdateProductRequest request, Long sellerId);

    /**
     * Delete a product. Only the owner (seller with matching sellerId) may delete.
     * Throws UnauthorizedException if another seller attempts to delete.
     */
    void deleteProduct(Long id, Long sellerId);

    /**
     * Retrieve all products owned by the requesting seller (all availability statuses).
     */
    List<ProductSummaryResponse> getMyProducts(Long sellerId);
}
