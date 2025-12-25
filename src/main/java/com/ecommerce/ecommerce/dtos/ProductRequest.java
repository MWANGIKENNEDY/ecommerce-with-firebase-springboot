package com.ecommerce.ecommerce.dtos;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class ProductRequest {
    private String name;
    private String brand;
    private BigDecimal price;
    private int inventory;
    private String description;
    //Do not put an Entity in the DTO!
    private String category;  // Just the name as String!
}
