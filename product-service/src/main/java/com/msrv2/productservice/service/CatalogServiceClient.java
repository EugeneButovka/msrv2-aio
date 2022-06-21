package com.msrv2.productservice.service;

import com.msrv2.productservice.config.FeignConfig;
import com.msrv2.productservice.model.Product;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;
import java.util.Optional;

@FeignClient(name = "catalog-service", decode404 = true, configuration = FeignConfig.class)
public interface CatalogServiceClient {

    @GetMapping("api/products/{uniqId}")
    Optional<Product> getProductByUniqId(@PathVariable String uniqId);

    @GetMapping("api/products/sku/{sku}")
    List<Product> getProductsBySku(@PathVariable String sku);
}
