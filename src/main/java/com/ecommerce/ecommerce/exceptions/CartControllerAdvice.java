package com.ecommerce.ecommerce.exceptions;

import com.ecommerce.ecommerce.controller.CartController;
import com.ecommerce.ecommerce.dtos.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice(assignableTypes = CartController.class)
public class CartControllerAdvice {

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ApiResponse> handleRuntimeException(RuntimeException e) {
        // Check if it's a "not found" type of exception
        if (e.getMessage() != null && e.getMessage().contains("not found")) {
            ApiResponse errorResponse = new ApiResponse(e.getMessage(), null);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
        }
        // Otherwise treat as internal server error
        ApiResponse errorResponse = new ApiResponse("Error: " + e.getMessage(), null);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse> handleGenericException(Exception e) {
        ApiResponse errorResponse = new ApiResponse("Internal Server Error: " + e.getMessage(), null);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
    }
}
