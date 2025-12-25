package com.ecommerce.ecommerce.controller;

import com.ecommerce.ecommerce.dtos.ApiResponse;
import com.ecommerce.ecommerce.dtos.CartItemRequest;
import com.ecommerce.ecommerce.dtos.CartResponse;
import com.ecommerce.ecommerce.service.CartService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cart")
public class CartController {

    private final CartService cartService;

    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    // Get current user's cart
    @GetMapping
    public ResponseEntity<CartResponse> getCart(Authentication authentication) {
        String userId = authentication.getName(); // UID from SecurityContext
        CartResponse cart = cartService.getCart(userId);
        return ResponseEntity.ok(cart);
    }

    // Add item to cart
    @PostMapping("/items")
    public ResponseEntity<ApiResponse> addItemToCart(
            @Valid @RequestBody CartItemRequest request,
            Authentication authentication) {
        String userId = authentication.getName();
        CartResponse cart = cartService.addItemToCart(userId, request);
        return ResponseEntity.ok(new ApiResponse("Item added to cart successfully", cart));
    }

    // Remove item from cart
    @DeleteMapping("/items/{cartItemId}")
    public ResponseEntity<ApiResponse> removeItemFromCart(
            @PathVariable Long cartItemId,
            Authentication authentication) {
        String userId = authentication.getName();
        CartResponse cart = cartService.removeItemFromCart(userId, cartItemId);
        return ResponseEntity.ok(new ApiResponse("Item removed from cart successfully", cart));
    }
}
