package com.ecommerce.ecommerce.dtos;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductResponse {

    @NotNull
    private Long id;  

    @NotEmpty
    private String name;

    @NotEmpty
    private String description;

    @NotNull
    private BigDecimal price;

    @NotNull
    private Integer inventory;

    @NotEmpty
    private String brand;

    @NotNull
    private CategoryResponse category;

    private List<ImageResponse> images;
}
