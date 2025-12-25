package com.ecommerce.ecommerce.controller;

import com.ecommerce.ecommerce.dtos.ProductRequest;
import com.ecommerce.ecommerce.dtos.ProductResponse;
import com.ecommerce.ecommerce.service.ProductService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/products")
public class AdminProductController {

    private final ProductService productService;

    public AdminProductController(ProductService productService) {
        this.productService = productService;
    }

    // ➕ CREATE PRODUCT
    @PostMapping
    public ResponseEntity<ProductResponse> addProduct(@RequestBody ProductRequest req) {
        ProductResponse created = productService.create(req);
        return ResponseEntity.ok(created);
    }

    // ✏ UPDATE PRODUCT
    @PutMapping("/{id}")
    public ResponseEntity<ProductResponse> updateProduct(
            @PathVariable Long id,
            @RequestBody ProductRequest req) {

        ProductResponse updated = productService.update(id, req);
        return ResponseEntity.ok(updated);
    }

    // ❌ DELETE PRODUCT
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        boolean deleted = productService.delete(id);
        if (!deleted) return ResponseEntity.notFound().build();
        return ResponseEntity.noContent().build();
    }

    // 📋 LIST ALL PRODUCTS
    @GetMapping
    public ResponseEntity<List<ProductResponse>> getAll() {
        return ResponseEntity.ok(productService.findAll());
    }

    // 📌 GET SINGLE PRODUCT
    @GetMapping("/{id}")
    public ResponseEntity<ProductResponse> getOne(@PathVariable Long id) {
        return ResponseEntity.ok(productService.findById(id));
    }
}
