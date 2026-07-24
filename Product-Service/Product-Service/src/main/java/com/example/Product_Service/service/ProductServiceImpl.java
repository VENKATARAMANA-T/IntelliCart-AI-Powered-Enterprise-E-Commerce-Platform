package com.example.Product_Service.service;

import com.example.Product_Service.dto.request.CreateProductRequest;
import com.example.Product_Service.dto.request.UpdateProductRequest;
import com.example.Product_Service.dto.response.ProductResponse;
import com.example.Product_Service.dto.response.ProductSummaryResponse;
import com.example.Product_Service.exception.ProductNotFoundException;
import com.example.Product_Service.exception.UnauthorizedException;
import com.example.Product_Service.model.Product;
import com.example.Product_Service.repository.ProductRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.example.Product_Service.feign.InventoryServiceClient;
import java.util.Map;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Core product catalog business logic.
 *
 * Authorization layers:
 *   1. HTTP-level  — all /api/** require authentication (SecurityConfig)
 *   2. Role-level  — @PreAuthorize on controller methods (SELLER / CUSTOMER / any auth)
 *   3. Ownership   — this class verifies sellerId matches for update/delete
 *
 * @Transactional ensures JPA @ElementCollection operations (imageUrls, specs)
 * are flushed atomically within a single DB transaction.
 */
@Service
@Transactional
public class ProductServiceImpl implements ProductService {

    private static final Logger log = LoggerFactory.getLogger(ProductServiceImpl.class);

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private InventoryServiceClient inventoryServiceClient;

    // =========================================================================
    // CREATE
    // =========================================================================

    @Override
    public ProductResponse createProduct(CreateProductRequest request,
                                         Long sellerId,
                                         String sellerUsername) {
        Product product = Product.builder()
                .name(request.getName())
                .shortDescription(request.getShortDescription())
                .detailedDescription(request.getDetailedDescription())
                .price(request.getPrice())
                .discountPercent(request.getDiscountPercent())
                .brand(request.getBrand())
                .category(request.getCategory())
                .imageUrls(request.getImageUrls() != null ? request.getImageUrls() : List.of())
                .specifications(request.getSpecifications() != null ? request.getSpecifications() : java.util.Map.of())
                .sellerId(sellerId)
                .sellerUsername(sellerUsername)
                .build();

        Product saved = productRepository.save(product);
        log.info("Product created: id={}, name='{}', seller={}", saved.getId(), saved.getName(), sellerUsername);
        
        try {
            inventoryServiceClient.initStock(saved.getId(), new InventoryServiceClient.InitStockRequest(
                saved.getName(), saved.getCategory(), saved.getSellerUsername(), request.getStockCount()
            ));
        } catch (Exception e) {
            log.error("Failed to initialize stock for product {}", saved.getId(), e);
        }

        return toProductResponse(saved);
    }

    // =========================================================================
    // READ
    // =========================================================================

    @Override
    @Transactional(readOnly = true)
    public List<ProductSummaryResponse> getAllProducts() {
        return productRepository.findAll()
                .stream()
                .map(this::toProductSummaryResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public ProductResponse getProductById(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException(id));
        return toProductResponse(product);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductSummaryResponse> getMyProducts(Long sellerId) {
        return productRepository.findBySellerId(sellerId)
                .stream()
                .map(this::toProductSummaryResponse)
                .collect(Collectors.toList());
    }

    // =========================================================================
    // UPDATE
    // =========================================================================

    @Override
    public ProductResponse updateProduct(Long id, UpdateProductRequest request, Long sellerId) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException(id));

        // Ownership check — reject if requesting seller is not the product owner
        if (!product.getSellerId().equals(sellerId)) {
            log.warn("Unauthorized update attempt on product {} by seller {}", id, sellerId);
            throw new UnauthorizedException(
                    "You are not authorized to update this product. "
                    + "Only the product owner (seller) can modify it.");
        }

        // Apply patch-style updates — only set fields that are non-null in the request
        if (request.getName() != null) {
            product.setName(request.getName());
        }
        if (request.getShortDescription() != null) {
            product.setShortDescription(request.getShortDescription());
        }
        if (request.getDetailedDescription() != null) {
            product.setDetailedDescription(request.getDetailedDescription());
        }
        if (request.getPrice() != null) {
            product.setPrice(request.getPrice());
        }
        if (request.getDiscountPercent() != null) {
            product.setDiscountPercent(request.getDiscountPercent());
        }
        if (request.getBrand() != null) {
            product.setBrand(request.getBrand());
        }
        if (request.getCategory() != null) {
            product.setCategory(request.getCategory());
        }
        if (request.getImageUrls() != null) {
            product.setImageUrls(request.getImageUrls());
        }
        if (request.getSpecifications() != null) {
            product.setSpecifications(request.getSpecifications());
        }

        Product updated = productRepository.save(product);
        log.info("Product updated: id={}, seller={}", id, sellerId);
        return toProductResponse(updated);
    }

    // =========================================================================
    // DELETE
    // =========================================================================

    @Override
    public void deleteProduct(Long id, Long sellerId) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException(id));

        // Ownership check — reject if requesting seller is not the product owner
        if (!product.getSellerId().equals(sellerId)) {
            log.warn("Unauthorized delete attempt on product {} by seller {}", id, sellerId);
            throw new UnauthorizedException(
                    "You are not authorized to delete this product. "
                    + "Only the product owner (seller) can delete it.");
        }

        productRepository.delete(product);
        log.info("Product deleted: id={}, seller={}", id, sellerId);
    }

    // =========================================================================
    // Mapping helpers
    // =========================================================================

    /**
     * Map Product entity → full ProductResponse (for single-product detail view).
     */
    public ProductResponse toProductResponse(Product product) {
        return ProductResponse.builder()
                .id(product.getId())
                .name(product.getName())
                .shortDescription(product.getShortDescription())
                .detailedDescription(product.getDetailedDescription())
                .imageUrls(product.getImageUrls())
                .price(product.getPrice())
                .discountPercent(product.getDiscountPercent())
                .brand(product.getBrand())
                .category(product.getCategory())
                .specifications(product.getSpecifications())
                .sellerId(product.getSellerId())
                .sellerUsername(product.getSellerUsername())
                .stockCount(fetchStock(product.getId()))
                .averageRating(product.getAverageRating())
                .totalReviews(product.getTotalReviews())
                .createdAt(product.getCreatedAt())
                .updatedAt(product.getUpdatedAt())
                .build();
    }

    /**
     * Map Product entity → lightweight ProductSummaryResponse (for list views).
     * Uses only the first image URL as the thumbnail.
     */
    private ProductSummaryResponse toProductSummaryResponse(Product product) {
        String thumbnail = (product.getImageUrls() != null && !product.getImageUrls().isEmpty())
                ? product.getImageUrls().get(0)
                : null;

        return ProductSummaryResponse.builder()
                .id(product.getId())
                .name(product.getName())
                .brand(product.getBrand())
                .category(product.getCategory())
                .price(product.getPrice())
                .discountPercent(product.getDiscountPercent())
                .averageRating(product.getAverageRating())
                .totalReviews(product.getTotalReviews())
                .stockCount(fetchStock(product.getId()))
                .thumbnailUrl(thumbnail)
                .sellerUsername(product.getSellerUsername())
                .build();
    }

    private Integer fetchStock(Long productId) {
        try {
            Map<String, Object> response = inventoryServiceClient.getStock(productId);
            return (Integer) response.get("stockQuantity");
        } catch (Exception e) {
            log.warn("Could not fetch stock for product {}", productId);
            return 0;
        }
    }
}
