package com.ecommerce.ecommerce.model;

import jakarta.persistence.*;

@Entity
public class ProductImage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String imageUrl;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    // getters and setters
}
