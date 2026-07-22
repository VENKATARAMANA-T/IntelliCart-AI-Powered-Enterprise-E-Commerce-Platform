package com.example.Product_Service.exception;

/**
 * Thrown when a requested review does not exist in the database.
 * Mapped to HTTP 404 Not Found by GlobalExceptionHandler.
 */
public class ReviewNotFoundException extends RuntimeException {

    public ReviewNotFoundException(String message) {
        super(message);
    }

    public ReviewNotFoundException(Long id) {
        super("Review not found with id: " + id);
    }
}
