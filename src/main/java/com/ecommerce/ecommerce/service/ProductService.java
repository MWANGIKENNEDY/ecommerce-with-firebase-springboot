package com.ecommerce.ecommerce.service;

import com.ecommerce.ecommerce.dtos.ProductRequest;
import com.ecommerce.ecommerce.dtos.ProductResponse;

import java.util.List;

public interface ProductService {

    ProductResponse create(ProductRequest req);

    List<ProductResponse> findAll();

    ProductResponse findById(Long id);

    ProductResponse update(Long id, ProductRequest req);

    boolean delete(Long id);
}
