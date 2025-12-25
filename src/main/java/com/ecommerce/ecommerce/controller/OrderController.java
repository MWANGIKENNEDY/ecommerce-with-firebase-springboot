package com.ecommerce.ecommerce.controller;

import com.ecommerce.ecommerce.dtos.ApiResponse;
import com.ecommerce.ecommerce.dtos.OrderResponse;
import com.ecommerce.ecommerce.service.OrderService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    // Checkout - Create order from cart
    @PostMapping("/checkout")
    public ResponseEntity<ApiResponse> checkout(Authentication authentication) {
        String userId = authentication.getName(); // UID from SecurityContext
        OrderResponse order = orderService.checkout(userId);
        return ResponseEntity.ok(new ApiResponse("Order created successfully", order));
    }

    // Get all orders for current user
    @GetMapping
    public ResponseEntity<List<OrderResponse>> getUserOrders(Authentication authentication) {
        String userId = authentication.getName();
        List<OrderResponse> orders = orderService.getUserOrders(userId);
        return ResponseEntity.ok(orders);
    }

    // Get single order by ID
    @GetMapping("/{orderId}")
    public ResponseEntity<OrderResponse> getOrderById(
            @PathVariable Long orderId,
            Authentication authentication) {
        String userId = authentication.getName();
        OrderResponse order = orderService.getOrderById(userId, orderId);
        return ResponseEntity.ok(order);
    }
}
