package com.ecommerce.ecommerce.service;

import com.ecommerce.ecommerce.dtos.OrderItemResponse;
import com.ecommerce.ecommerce.dtos.OrderResponse;
import com.ecommerce.ecommerce.dtos.ProductResponse;
import com.ecommerce.ecommerce.model.Cart;
import com.ecommerce.ecommerce.model.CartItem;
import com.ecommerce.ecommerce.model.Order;
import com.ecommerce.ecommerce.model.OrderItem;
import com.ecommerce.ecommerce.model.Product;
import com.ecommerce.ecommerce.model.User;
import com.ecommerce.ecommerce.repo.CartRepository;
import com.ecommerce.ecommerce.repo.OrderRepository;
import com.ecommerce.ecommerce.repo.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final CartRepository cartRepository;
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;

    public OrderService(OrderRepository orderRepository,
            CartRepository cartRepository,
            UserRepository userRepository,
            ModelMapper modelMapper) {
        this.orderRepository = orderRepository;
        this.cartRepository = cartRepository;
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
    }

    @Transactional
    public OrderResponse checkout(String userId) {
        // 1. Get user
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // 2. Get user's cart
        Cart cart = cartRepository.findByUser(user)
                .orElseThrow(() -> new RuntimeException("Cart is empty"));

        // 3. Check if cart has items
        if (cart.getItems() == null || cart.getItems().isEmpty()) {
            throw new RuntimeException("Cannot checkout with an empty cart");
        }

        // 4. Create order
        Order order = new Order();
        order.setUser(user);
        order.setItems(new java.util.ArrayList<>());

        // 5. Calculate total and create order items
        BigDecimal totalAmount = BigDecimal.ZERO;

        for (CartItem cartItem : cart.getItems()) {
            Product product = cartItem.getProduct();
            Integer quantity = cartItem.getQuantity();
            BigDecimal price = product.getPrice();
            BigDecimal itemTotal = price.multiply(BigDecimal.valueOf(quantity));

            // Create order item
            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(order);
            orderItem.setProduct(product);
            orderItem.setQuantity(quantity);
            orderItem.setPrice(price);

            order.getItems().add(orderItem);
            totalAmount = totalAmount.add(itemTotal);
        }

        order.setTotalAmount(totalAmount);

        // 6. Save order
        Order savedOrder = orderRepository.save(order);

        // 7. Clear cart
        cart.getItems().clear();
        cartRepository.save(cart);

        // 8. Convert to response
        return mapToOrderResponse(savedOrder);
    }

    public List<OrderResponse> getUserOrders(String userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        List<Order> orders = orderRepository.findByUser(user);
        return orders.stream()
                .map(this::mapToOrderResponse)
                .collect(Collectors.toList());
    }

    public OrderResponse getOrderById(String userId, Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));
        // Verify order belongs to user
        if (order.getUser() == null || !order.getUser().getUid().equals(userId)) {
            throw new RuntimeException("Order not found");
        }
        return mapToOrderResponse(order);
    }

    private OrderResponse mapToOrderResponse(Order order) {
        OrderResponse response = new OrderResponse();
        response.setId(order.getId());
        response.setTotalAmount(order.getTotalAmount());

        List<OrderItemResponse> itemResponses = order.getItems().stream()
                .map(item -> {
                    OrderItemResponse itemResponse = new OrderItemResponse();
                    itemResponse.setId(item.getId());
                    itemResponse.setQuantity(item.getQuantity());
                    itemResponse.setPrice(item.getPrice());
                    itemResponse.setProduct(modelMapper.map(item.getProduct(), ProductResponse.class));
                    return itemResponse;
                })
                .collect(Collectors.toList());

        response.setItems(itemResponses);
        return response;
    }
}
