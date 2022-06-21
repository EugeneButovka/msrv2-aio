package com.msrv2.catalogservice.repository;

import com.msrv2.catalogservice.model.Product;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ProductRepository extends MongoRepository<Product, String> {

}
