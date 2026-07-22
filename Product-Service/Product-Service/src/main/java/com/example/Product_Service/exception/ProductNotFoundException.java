package com.example.Product_Service.exception;

/**
 * Thrown when a requested product does not exist in the database.
 * Mapped to HTTP 404 Not Found by GlobalExceptionHandler.
 */
public class ProductNotFoundException extends RuntimeException {

    public ProductNotFoundException(String message) {
        super(message);
    }

    public ProductNotFoundException(Long id) {
        super("Product not found with id: " + id);
    }
}
