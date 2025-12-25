package com.ecommerce.ecommerce.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderResponse {

    private Long id;
    private BigDecimal totalAmount;
    private List<OrderItemResponse> items;
}
