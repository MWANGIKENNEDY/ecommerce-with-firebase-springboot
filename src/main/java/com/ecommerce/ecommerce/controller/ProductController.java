package com.ecommerce.ecommerce.controller;

import com.ecommerce.ecommerce.dtos.ProductResponse;
import com.ecommerce.ecommerce.model.Product;
import com.ecommerce.ecommerce.repo.ProductRepository;
import com.ecommerce.ecommerce.service.ProductService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
public class ProductController {


    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    // List all products (public)

    @GetMapping
    public ResponseEntity<List<ProductResponse>> getAll() {
        return ResponseEntity.ok(productService.findAll());
    }

    // 📌 GET SINGLE PRODUCT
    @GetMapping("/{id}")
    public ResponseEntity<ProductResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(productService.findById(id));
    }
}
