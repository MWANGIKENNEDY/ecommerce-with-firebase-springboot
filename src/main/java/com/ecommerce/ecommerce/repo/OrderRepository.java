package com.ecommerce.ecommerce.repo;

import com.ecommerce.ecommerce.model.Order;
import com.ecommerce.ecommerce.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByUser(User user);
}
