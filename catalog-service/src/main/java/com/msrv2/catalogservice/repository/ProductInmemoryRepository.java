package com.msrv2.catalogservice.repository;

import com.msrv2.catalogservice.exception.ProductNotFoundException;
import com.msrv2.catalogservice.model.Product;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Repository
public class ProductInmemoryRepository {
    private final List<Product> products;
    ProductRepository productRepository;

    public ProductInmemoryRepository(ProductRepository productRepository) {
        products = new ArrayList<>(productRepository.findAll());
        this.productRepository = productRepository;
    }

    public List<Product> findBySku(String sku) {
        return products.stream()
                       .filter(p -> p.getSku().equals(sku))
                       .collect(Collectors.toList());
    }

    public Product findByUniqId(String uniqId) {
        return products.stream()
                       .filter(p -> p.getUniqId().equals(uniqId))
                       .findFirst()
                       .orElseThrow(() -> new ProductNotFoundException(uniqId));
    }
}
