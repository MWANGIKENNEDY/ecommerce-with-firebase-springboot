package com.ecommerce.ecommerce.service;

import com.ecommerce.ecommerce.dtos.CartItemRequest;
import com.ecommerce.ecommerce.dtos.CartItemResponse;
import com.ecommerce.ecommerce.dtos.CartResponse;
import com.ecommerce.ecommerce.dtos.ProductResponse;
import com.ecommerce.ecommerce.model.Cart;
import com.ecommerce.ecommerce.model.CartItem;
import com.ecommerce.ecommerce.model.Product;
import com.ecommerce.ecommerce.model.User;
import com.ecommerce.ecommerce.repo.CartRepository;
import com.ecommerce.ecommerce.repo.ProductRepository;
import com.ecommerce.ecommerce.repo.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CartService {

    private final CartRepository cartRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final ModelMapper modelMapper;

    public CartService(CartRepository cartRepository,
            UserRepository userRepository,
            ProductRepository productRepository,
            ModelMapper modelMapper) {
        this.cartRepository = cartRepository;
        this.userRepository = userRepository;
        this.productRepository = productRepository;
        this.modelMapper = modelMapper;
    }

    @Transactional
    public CartResponse addItemToCart(String userId, CartItemRequest request) {
        // 1. Get or create user's cart
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Cart cart = cartRepository.findByUser(user)
                .orElseGet(() -> {
                    Cart newCart = new Cart();
                    newCart.setUser(user);
                    return cartRepository.save(newCart);
                });

        // 2. Check if product exists
        Product product = productRepository.findById(request.getProductId())
                .orElseThrow(() -> new RuntimeException("Product not found"));

        // 3. Check if item already exists in cart
        Optional<CartItem> existingItem = cart.getItems().stream()
                .filter(item -> item.getProduct().getId().equals(request.getProductId()))
                .findFirst();

        if (existingItem.isPresent()) {
            // Update quantity
            CartItem item = existingItem.get();
            item.setQuantity(item.getQuantity() + request.getQuantity());
        } else {
            // Create new cart item
            CartItem newItem = new CartItem();
            newItem.setCart(cart);
            newItem.setProduct(product);
            newItem.setQuantity(request.getQuantity());
            cart.getItems().add(newItem);
        }

        // 4. Save cart
        Cart savedCart = cartRepository.save(cart);

        // 5. Convert to response
        return mapToCartResponse(savedCart);
    }

    @Transactional
    public CartResponse removeItemFromCart(String userId, Long cartItemId) {
        // 1. Get user's cart
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Cart cart = cartRepository.findByUser(user)
                .orElseThrow(() -> new RuntimeException("Cart not found"));

        // 2. Find and remove item
        CartItem itemToRemove = cart.getItems().stream()
                .filter(item -> item.getId().equals(cartItemId))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Cart item not found"));

        cart.getItems().remove(itemToRemove);
        Cart savedCart = cartRepository.save(cart);

        // 3. Convert to response
        return mapToCartResponse(savedCart);
    }

    public CartResponse getCart(String userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Cart cart = cartRepository.findByUser(user)
                .orElseGet(() -> {
                    Cart newCart = new Cart();
                    newCart.setUser(user);
                    return cartRepository.save(newCart);
                });

        return mapToCartResponse(cart);
    }

    private CartResponse mapToCartResponse(Cart cart) {
        CartResponse response = new CartResponse();
        response.setId(cart.getId());

        List<CartItemResponse> itemResponses = cart.getItems().stream()
                .map(item -> {
                    CartItemResponse itemResponse = new CartItemResponse();
                    itemResponse.setId(item.getId());
                    itemResponse.setQuantity(item.getQuantity());
                    itemResponse.setProduct(modelMapper.map(item.getProduct(), ProductResponse.class));
                    return itemResponse;
                })
                .collect(Collectors.toList());

        response.setItems(itemResponses);
        return response;
    }
}
