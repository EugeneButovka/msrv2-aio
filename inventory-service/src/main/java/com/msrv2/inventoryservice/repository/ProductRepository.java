package com.msrv2.inventoryservice.repository;

import com.msrv2.inventoryservice.model.Product;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ProductRepository extends MongoRepository<Product, String> {

}
