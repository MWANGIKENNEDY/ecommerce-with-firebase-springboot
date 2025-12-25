package com.ecommerce.ecommerce.exceptions;

import com.ecommerce.ecommerce.controller.AdminController;
import com.ecommerce.ecommerce.dtos.ApiResponse;
import com.google.firebase.auth.FirebaseAuthException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice(assignableTypes = { AdminController.class,
        com.ecommerce.ecommerce.controller.AdminProductController.class })
public class AdminControllerAdvice {

    @ExceptionHandler(FirebaseAuthException.class)
    public ResponseEntity<ApiResponse> handleFirebaseAuthException(FirebaseAuthException e) {
        ApiResponse errorResponse = new ApiResponse("Firebase Authentication Error: " + e.getMessage(), null);
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiResponse> handleIllegalArgumentException(IllegalArgumentException e) {
        ApiResponse errorResponse = new ApiResponse("Invalid Argument: " + e.getMessage(), null);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

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
