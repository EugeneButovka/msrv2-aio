package com.msrv2.inventoryservice.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

import com.msrv2.inventoryservice.model.Product;
import com.msrv2.inventoryservice.repository.ProductAvailabilityInmemoryRepository;

@Service
@Transactional
@Slf4j
public class ProductService {
    @Autowired
    ProductAvailabilityInmemoryRepository productAvailabilityInmemoryRepository;

    public Product getProductByUniqId(String uniqId) {
        return productAvailabilityInmemoryRepository.findByUniqId(uniqId);
    }

    public List<Product> getProductsByUniqIdList(List<String> uniqIds) {
        return productAvailabilityInmemoryRepository.findByUniqIdList(uniqIds);
    }

    public List<Product> getProductsBySku(String sku) {
        return productAvailabilityInmemoryRepository.findBySku(sku);
    }
}
