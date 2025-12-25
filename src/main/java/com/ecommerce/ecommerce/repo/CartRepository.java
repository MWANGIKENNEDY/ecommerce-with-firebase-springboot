package com.ecommerce.ecommerce.repo;

import com.ecommerce.ecommerce.model.Cart;
import com.ecommerce.ecommerce.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CartRepository extends JpaRepository<Cart, Long> {
    Optional<Cart> findByUser(User user);
}
