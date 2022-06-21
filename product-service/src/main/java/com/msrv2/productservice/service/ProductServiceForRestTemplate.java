package com.msrv2.productservice.service;

import com.msrv2.productservice.exception.InternalException;
import com.msrv2.productservice.model.Product;
import com.msrv2.productservice.model.ProductAvailability;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service("ProductServiceRestTemplate")
@Slf4j
public class ProductServiceForRestTemplate implements ProductService {
    @Value("${inventory-service.url}")
    private String inventoryAppUrl;
    @Value("${catalog-service.url}")
    private String catalogAppUrl;

    @Autowired
    RestTemplate restTemplate;

    @Override
    @HystrixCommand(
            //fallbackMethod = "getDefaultProduct",
            ignoreExceptions = {InternalException.class, NoSuchElementException.class},
            commandProperties = {
                    @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "1000"),
                    @HystrixProperty(name = "circuitBreaker.requestVolumeThreshold", value = "3"),
                    @HystrixProperty(name = "metrics.rollingStats.timeInMilliseconds", value = "5000"),
                    //@HystrixProperty(name = "circuitBreaker.errorThresholdPercentage", value = "50"),
                    @HystrixProperty(name = "circuitBreaker.sleepWindowInMilliseconds", value = "5000")
            }
    )
    public Product getProductByUniqId(String uniqId) {
        ResponseEntity<ProductAvailability> inventoryResponse = null;
        ResponseEntity<Product> catalogResponse = null;
        try {
            log.info("getting inventoryResponse");
            inventoryResponse = restTemplate
                    .getForEntity(inventoryAppUrl + uniqId, ProductAvailability.class);
            log.info("getting catalogResponse");
            catalogResponse = restTemplate
                    .getForEntity(catalogAppUrl + uniqId, Product.class);
        } catch (HttpStatusCodeException e) {
            log.error("Error in HTTP request: getProductByUniqId");
            if (e.getStatusCode() == HttpStatus.NOT_FOUND) throw new NoSuchElementException("No element!");
            throw new InternalException("Error in HTTP request: getProductByUniqId");
        }
        boolean isAvailable = Optional.ofNullable(inventoryResponse.getBody())
                                      .orElseThrow(() -> new NoSuchElementException("No availability info!"))
                                      .isAvailable();
        Product product = Optional.ofNullable(catalogResponse.getBody())
                                  .orElseThrow(() -> new NoSuchElementException("No product body!"));
        //if (!isAvailable) throw new ProductNotAvailableException(uniqId); //TODO: clarify task
        log.info("returning product");
        return product.toBuilder().available(isAvailable).build();
    }

    @Override
    @HystrixCommand(
            /*fallbackMethod = "getDefaultProducts",*/
            ignoreExceptions = {InternalException.class}
    )
    public List<Product> getProductsBySku(String sku) {
        ResponseEntity<ProductAvailability[]> inventoryResponse = null;
        ResponseEntity<Product[]> catalogResponse = null;
        try {
            inventoryResponse = restTemplate
                    .getForEntity("http://inventory-app/api/products/sku/" + sku, ProductAvailability[].class);
            catalogResponse = restTemplate
                    .getForEntity("http://catalog-app/api/products/sku/" + sku, Product[].class);
        } catch (HttpStatusCodeException e) {
            log.error("Error in HTTP request: getProductsBySku");
            throw new InternalException("Error in HTTP request: getProductsBySku, " + e.getMessage());
        }
        List<ProductAvailability> availability = Arrays.asList(
                Optional.ofNullable(inventoryResponse.getBody())
                        .orElseThrow(() -> new InternalException("Error in inventory app response")));
        List<Product> products = Stream.of(Optional.ofNullable(catalogResponse.getBody())
                                                   .orElseThrow(() -> new InternalException(
                                                           "Error in catalog app response")))
                                       .map(catalogProduct -> {
                                           boolean isAvailable = availability
                                                   .stream()
                                                   .filter(productAvailability ->
                                                                   productAvailability.getUniqId().equals(
                                                                           catalogProduct.getUniqId()))
                                                   .findFirst()
                                                   .orElseThrow(() -> new InternalException(
                                                           "Inconsistent input  from inventory and catalog apps"))
                                                   .isAvailable();
                                           return catalogProduct.toBuilder().available(isAvailable).build();
                                       })
                                       .collect(Collectors.toList());
        return products;
    }

    @Override
    @SuppressWarnings("unused")
    public Product getDefaultProduct(String uniqId, Throwable t) {
        System.out.println("getDefaultProduct");
        System.out.println(t.getMessage());
        return new Product();
    }

    @Override
    @SuppressWarnings("unused")
    public List<Product> getDefaultProducts(String sku, Throwable t) {
        System.out.println("getDefaultProducts");
        System.out.println(t.getMessage());
        return new ArrayList<>();
    }
}
