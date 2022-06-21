package com.msrv2.inventoryservice.controller;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.NoSuchElementException;

import com.msrv2.inventoryservice.model.Product;
import com.msrv2.inventoryservice.service.ProductService;

@RestController
@RequestMapping("/api")
@Slf4j
public class ProductAvailabilityController {
    @Autowired
    ProductService productService;

    @GetMapping(value = "/products/{uniq_id}")
    @HystrixCommand(ignoreExceptions = {NoSuchElementException.class})
    public Product getAvailableProductByUniqId(@PathVariable(name = "uniq_id") String uniqId) {
        log.info("get product request by id");
        return productService.getProductByUniqId(uniqId);
    }

    @GetMapping(value = "/products")
    @HystrixCommand
    public List<Product> getAvailableProductsByUniqIdList(@RequestBody List<String> uniqIds) {
        log.info("get products request by id list");
        return productService.getProductsByUniqIdList(uniqIds);
    }

    @GetMapping(value = "/products/sku/{sku}")
    @HystrixCommand
    public List<Product> getAvailableProductsBySku(@PathVariable("sku") String sku) {
        log.info("get products request by sku");
        return productService.getProductsBySku(sku);
    }

    @ExceptionHandler(value = {NoSuchElementException.class})
    @ResponseStatus(value= HttpStatus.NOT_FOUND, reason="No such element!")
    public RuntimeException handleNoSuchElement(RuntimeException ex) {
        return ex;
    }
}
