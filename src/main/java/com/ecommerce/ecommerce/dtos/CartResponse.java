package com.ecommerce.ecommerce.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CartResponse {

    private Long id;
    private List<CartItemResponse> items;
}
