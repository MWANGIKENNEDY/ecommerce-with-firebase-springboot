package com.ecommerce.ecommerce.service;

import com.ecommerce.ecommerce.dtos.ProductRequest;
import com.ecommerce.ecommerce.dtos.ProductResponse;
import com.ecommerce.ecommerce.model.Product;
import com.ecommerce.ecommerce.model.Category;
import com.ecommerce.ecommerce.repo.CategoryRepository;
import com.ecommerce.ecommerce.repo.ProductRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepository repo;
    private final CategoryRepository categoryRepository;
    private final ModelMapper modelMapper;

    public ProductServiceImpl(ProductRepository repo,
                              CategoryRepository categoryRepository,
                              ModelMapper modelMapper) {

        this.repo = repo;
        this.categoryRepository = categoryRepository;
        this.modelMapper = modelMapper;
    }

    // ---------------- CREATE ----------------
    @Override
    public ProductResponse create(ProductRequest req) {

        // 1. Find or create category
        Category category = categoryRepository.findByName(req.getCategory())
                .orElseGet(() -> {
                    Category newCat = new Category();
                    newCat.setName(req.getCategory());
                    return categoryRepository.save(newCat);
                });

        // 2. Map DTO → Entity
        Product product = modelMapper.map(req, Product.class);

        // 3. Assign category
        product.setCategory(category);

        // 4. Save product
        Product saved = repo.save(product);

        // 5. Convert entity → response DTO
        return modelMapper.map(saved, ProductResponse.class);
    }

    // ---------------- GET ALL ----------------
    @Override
    public List<ProductResponse> findAll() {
        return repo.findAll()
                .stream()
                .map(product -> modelMapper.map(product, ProductResponse.class))
                .toList();
    }

    // ---------------- GET BY ID ----------------
    @Override
    public ProductResponse findById(Long id) {
        Product product = repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        return modelMapper.map(product, ProductResponse.class);
    }

    // ---------------- UPDATE ----------------
    @Override
    public ProductResponse update(Long id, ProductRequest req) {

        // 1. Find existing product
        Product product = repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        // 2. Find or create category
        Category category = categoryRepository.findByName(req.getCategory())
                .orElseGet(() -> {
                    Category newCat = new Category();
                    newCat.setName(req.getCategory());
                    return categoryRepository.save(newCat);
                });

        // 3. Update fields
        product.setName(req.getName());
        product.setBrand(req.getBrand());
        product.setInventory(req.getInventory());
        product.setPrice(req.getPrice());
        product.setDescription(req.getDescription());
        product.setCategory(category);

        // 4. Save
        Product updated = repo.save(product);

        // 5. Convert entity → DTO
        return modelMapper.map(updated, ProductResponse.class);
    }

    // ---------------- DELETE ----------------
    @Override
    public boolean delete(Long id) {
        if (!repo.existsById(id)) return false;
        repo.deleteById(id);
        return true;
    }
}
