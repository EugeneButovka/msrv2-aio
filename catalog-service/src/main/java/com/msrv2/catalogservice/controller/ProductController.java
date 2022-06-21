package com.msrv2.catalogservice.controller;

import com.msrv2.catalogservice.model.Product;
import com.msrv2.catalogservice.service.ProductService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
@Slf4j
public class ProductController {
    @Autowired
    ProductService productService;

    @GetMapping("/products/{uniq_id}")
    public Product getProductByUniqId(@PathVariable("uniq_id") String uniqId) {
        log.info("get product request by id");
        return productService.getProductByUniqId(uniqId);
    }

    @GetMapping("/products/sku/{sku}")
    public List<Product> getProductsBySku(@PathVariable("sku") String sku) {
        log.info("get products request by sku");
        return productService.getProductListBySku(sku);
    }


}
