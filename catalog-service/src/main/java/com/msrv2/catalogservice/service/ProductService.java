package com.msrv2.catalogservice.service;

import com.msrv2.catalogservice.model.Product;
import com.msrv2.catalogservice.repository.ProductInmemoryRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@Slf4j
public class ProductService {
    @Autowired
    ProductInmemoryRepository productInmemoryRepository;

    public List<Product> getProductListBySku(String sku) {
        return productInmemoryRepository.findBySku(sku);
    }

    public Product getProductByUniqId(String uniqId) {
        return productInmemoryRepository.findByUniqId(uniqId);
    }
}
