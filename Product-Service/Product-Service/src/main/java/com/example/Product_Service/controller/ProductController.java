package com.example.Product_Service.controller;

import com.example.Product_Service.dto.request.CreateProductRequest;
import com.example.Product_Service.dto.request.UpdateProductRequest;
import com.example.Product_Service.dto.response.ProductResponse;
import com.example.Product_Service.dto.response.ProductSummaryResponse;
import com.example.Product_Service.security.AuthenticatedUser;
import com.example.Product_Service.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Product Catalog REST Controller.
 *
 * Endpoints:
 *   POST   /api/products        — Create product          [SELLER only]
 *   GET    /api/products        — Browse all available    [Authenticated]
 *   GET    /api/products/my     — Seller's own products   [SELLER only]
 *   GET    /api/products/{id}   — Full product detail     [Authenticated]
 *   PUT    /api/products/{id}   — Update product          [SELLER only + owner]
 *   DELETE /api/products/{id}   — Delete product          [SELLER only + owner]
 *
 * Principal (AuthenticatedUser) is populated by JwtAuthenticationFilter
 * from the remote JWT validation response — no DB lookup required here.
 *
 * Ownership (sellerId match) is enforced in ProductServiceImpl, not here.
 * This keeps the controller thin — no business logic beyond delegation.
 */
@RestController
@RequestMapping("/api/products")
public class ProductController {

    @Autowired
    private ProductService productService;

    // -----------------------------------------------------------------------
    // CREATE
    // -----------------------------------------------------------------------

    /**
     * Create a new product listing.
     * Only authenticated sellers may create products.
     * sellerId and sellerUsername are extracted from the JWT — NOT from the request body.
     */
    @PostMapping
    @PreAuthorize("hasRole('SELLER')")
    public ResponseEntity<ProductResponse> createProduct(
            @Valid @RequestBody CreateProductRequest request,
            Authentication authentication) {

        AuthenticatedUser seller = (AuthenticatedUser) authentication.getPrincipal();
        ProductResponse response = productService.createProduct(
                request, seller.getUserId(), seller.getUsername());
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // -----------------------------------------------------------------------
    // READ — List
    // -----------------------------------------------------------------------

    /**
     * Browse all available products.
     * Accessible to any authenticated user (CUSTOMER or SELLER).
     */
    @GetMapping
    public ResponseEntity<List<ProductSummaryResponse>> getAllProducts() {
        return ResponseEntity.ok(productService.getAllProducts());
    }

    /**
     * Retrieve all products belonging to the authenticated seller.
     * Returns all availability statuses (including unavailable products).
     */
    @GetMapping("/my")
    @PreAuthorize("hasRole('SELLER')")
    public ResponseEntity<List<ProductSummaryResponse>> getMyProducts(Authentication authentication) {
        AuthenticatedUser seller = (AuthenticatedUser) authentication.getPrincipal();
        return ResponseEntity.ok(productService.getMyProducts(seller.getUserId()));
    }

    // -----------------------------------------------------------------------
    // READ — Single
    // -----------------------------------------------------------------------

    /**
     * Get full product details — Amazon-style product page payload.
     * Accessible to any authenticated user.
     */
    @GetMapping("/{id}")
    public ResponseEntity<ProductResponse> getProductById(@PathVariable Long id) {
        return ResponseEntity.ok(productService.getProductById(id));
    }

    // -----------------------------------------------------------------------
    // UPDATE
    // -----------------------------------------------------------------------

    /**
     * Update an existing product (patch-style — only non-null fields applied).
     * Only the product owner (matching sellerId) may update.
     * Attempting to update another seller's product returns 403.
     */
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('SELLER')")
    public ResponseEntity<ProductResponse> updateProduct(
            @PathVariable Long id,
            @Valid @RequestBody UpdateProductRequest request,
            Authentication authentication) {

        AuthenticatedUser seller = (AuthenticatedUser) authentication.getPrincipal();
        ProductResponse response = productService.updateProduct(id, request, seller.getUserId());
        return ResponseEntity.ok(response);
    }

    // -----------------------------------------------------------------------
    // DELETE
    // -----------------------------------------------------------------------

    /**
     * Delete a product permanently.
     * Only the product owner (matching sellerId) may delete.
     * Attempting to delete another seller's product returns 403.
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('SELLER')")
    public ResponseEntity<Void> deleteProduct(
            @PathVariable Long id,
            Authentication authentication) {

        AuthenticatedUser seller = (AuthenticatedUser) authentication.getPrincipal();
        productService.deleteProduct(id, seller.getUserId());
        return ResponseEntity.noContent().build();
    }
}
