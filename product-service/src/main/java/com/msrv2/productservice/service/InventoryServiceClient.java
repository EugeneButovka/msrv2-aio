package com.msrv2.productservice.service;

import com.msrv2.productservice.config.FeignConfig;
import com.msrv2.productservice.model.ProductAvailability;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.Optional;

@FeignClient(name = "inventory-service", decode404 = true, configuration = FeignConfig.class)
public interface InventoryServiceClient {

    @GetMapping("api/products/{uniqId}")
    Optional<ProductAvailability> getProductAvailabilityByUniqId(@PathVariable String uniqId);

    @GetMapping("api/products")
    List<ProductAvailability> getProductsAvailabilityByUniqIds(@RequestBody List<String> uniqIds);

    @GetMapping("api/products/sku/{sku}")
    List<ProductAvailability> getProductsAvailabilityBySku(@PathVariable String sku);

}
