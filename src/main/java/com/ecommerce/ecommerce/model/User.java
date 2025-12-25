package com.ecommerce.ecommerce.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;


@Entity
@Table(name = "users")
@Getter
@Setter
public class User {

    // Getters and setters
    @Id
    private String uid; // 🔥 Firebase UID as primary key

    private String name;
    private String email;

    // Extra fields
    private String phoneNumber;
    private String shippingAddress;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Cart> carts;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Order> orders;

    @Enumerated(EnumType.STRING)
    private Role role; // CUSTOMER or ADMIN

}
