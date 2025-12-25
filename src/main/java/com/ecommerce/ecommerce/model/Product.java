package com.ecommerce.ecommerce.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Entity
@Getter
@Setter
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String brand;       // ✔ MUST be added
    private Integer inventory;  // ✔ MUST be added
    private BigDecimal price;
    private String description;

    // Category relationship
    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    // Product images
    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL)
    private List<ProductImage> images;

    // Cart items - ignore in JSON to prevent circular references
    @OneToMany(mappedBy = "product")
    @JsonIgnore
    private List<CartItem> cartItems;

    // Order items - ignore in JSON to prevent circular references
    @OneToMany(mappedBy = "product")
    @JsonIgnore
    private List<OrderItem> orderItems;
}

