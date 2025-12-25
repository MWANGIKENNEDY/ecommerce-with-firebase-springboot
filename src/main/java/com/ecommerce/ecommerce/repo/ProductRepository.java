package com.ecommerce.ecommerce.repo;

import com.ecommerce.ecommerce.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {
}
