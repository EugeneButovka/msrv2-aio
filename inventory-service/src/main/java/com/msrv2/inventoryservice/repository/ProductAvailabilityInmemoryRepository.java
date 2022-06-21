package com.msrv2.inventoryservice.repository;

import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Random;
import java.util.stream.Collectors;

import com.msrv2.inventoryservice.model.Product;

@Repository
public class ProductAvailabilityInmemoryRepository {
    private final List<Product> products;

    public ProductAvailabilityInmemoryRepository(ProductRepository productRepository) {
        List<Product> productsFromDb = new ArrayList<>(productRepository.findAll());

        Random rnd = new Random();
        products = productsFromDb.stream()
                                 .map(product -> new Product(product.getUniqId(), product.getSku(), rnd.nextBoolean()))
                                 .collect(Collectors.toList());
    }

    public Product findByUniqId(String uniqId) {
        return products.stream().filter(p -> p.getUniqId().equals(uniqId)).findFirst().orElseThrow(
                NoSuchElementException::new);
    }

    public List<Product> findByUniqIdList(List<String> uniqIds) {
        return products.stream().filter(p -> uniqIds.contains(p.getUniqId())).collect(Collectors.toList());
    }

    public List<Product> findBySku(String sku) {
        return products.stream().filter(p -> sku.equals(p.getSku())).collect(Collectors.toList());
    }
}
