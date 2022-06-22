package com.msrv2.productservice.service;

import com.msrv2.productservice.exception.InternalException;
import com.msrv2.productservice.exception.ProductNotFoundException;
import com.msrv2.productservice.model.Product;
import com.msrv2.productservice.model.ProductAvailability;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpStatusCodeException;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import java.util.stream.Collectors;

@Service("ProductServiceImpl")
@Slf4j
public class ProductServiceImpl implements ProductService {
    @Autowired
    protected InventoryServiceClient inventoryServiceClient;

    @Autowired
    protected CatalogServiceClient catalogServiceClient;


    @SneakyThrows
    @HystrixCommand(
            /*fallbackMethod = "getDefaultProduct",*/
            ignoreExceptions = {NoSuchElementException.class, ProductNotFoundException.class},
            commandProperties = {
                    @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "1000"),
                    @HystrixProperty(name = "circuitBreaker.requestVolumeThreshold", value = "2"),
                    @HystrixProperty(name = "metrics.rollingStats.timeInMilliseconds", value = "5000"),
                    @HystrixProperty(name = "circuitBreaker.errorThresholdPercentage", value = "75"),
                    @HystrixProperty(name = "circuitBreaker.sleepWindowInMilliseconds", value = "5000")
            }
    )
    public Product getProductByUniqId(String uniqId) {
        ProductAvailability productAvailability = null;
        Product product = null;

        //try {
            log.info("getting inventoryResponse");
            productAvailability = inventoryServiceClient
                    .getProductAvailabilityByUniqId(uniqId)
                    .orElseThrow(() -> new ProductNotFoundException(uniqId));

            log.info("getting catalogResponse");
            product = catalogServiceClient
                    .getProductByUniqId(uniqId)
                    .orElseThrow(() -> new ProductNotFoundException(uniqId));

            /*CompletableFuture<ProductAvailability> productAvailabilityCf =
                    CompletableFuture.supplyAsync(() -> {
                        log.info("getting catalogResponse");
                        return inventoryServiceClient
                                .getProductAvailabilityByUniqId(uniqId)
                                .orElseThrow(
                                        () -> new ProductNotFoundException(uniqId));
                    });
            CompletableFuture<Product> productCf =
                    CompletableFuture.supplyAsync(() -> {
                        log.info("getting inventoryResponse");
                        return catalogServiceClient
                                .getProductByUniqId(uniqId)
                                .orElseThrow(
                                        () -> new ProductNotFoundException(uniqId));
                    });

            productAvailability = productAvailabilityCf.join();
            product = productCf.join();
        } catch (CompletionException e) {
            throw e.getCause();
        }*/

        boolean isAvailable = Objects.requireNonNull(productAvailability).isAvailable();
        //if (!isAvailable) throw new ProductNotAvailableException(uniqId); //TODO: clarify task
        log.info("returning product");
        return Objects.requireNonNull(product).toBuilder().available(isAvailable).build();
    }

    @SneakyThrows
    @HystrixCommand(
            //fallbackMethod = "getDefaultProducts",
            //ignoreExceptions = {InternalException.class}
    )
    public List<Product> getProductsBySku(String sku) {
        List<ProductAvailability> productsAvailability;
        List<Product> products;
        //try {
            log.info("getting inventoryResponse");
            productsAvailability = inventoryServiceClient.getProductsAvailabilityBySku(sku);

            log.info("getting catalogResponse");
            products = catalogServiceClient.getProductsBySku(sku);

            /*CompletableFuture<List<ProductAvailability>> productsAvailabilityCf =
                    CompletableFuture.supplyAsync(() -> {
                        log.info("getting catalogResponse");
                        return inventoryServiceClient.getProductsAvailabilityBySku(sku);
                    });
            CompletableFuture<List<Product>> productsCf =
                    CompletableFuture.supplyAsync(() -> {
                        log.info("getting inventoryResponse");
                        return catalogServiceClient.getProductsBySku(sku);
                    });

            productsAvailability = productsAvailabilityCf.join();
            products = productsCf.join();
        } catch (CompletionException e) {
            log.error("Error in getProductsBySku: " + e);
            throw e.getCause();
        }*/

        return products.stream()
                       .map(catalogProduct -> {
                           boolean isAvailable = productsAvailability
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
                       .filter(Product::isAvailable)
                       .collect(Collectors.toList());
    }

    @SuppressWarnings("unused")
    public Product getDefaultProduct(String uniqId, Throwable t) {
        log.error("getDefaultProduct:" + t.getMessage());
        return new Product();
    }

    @SuppressWarnings("unused")
    public List<Product> getDefaultProducts(String sku, Throwable t) {
        log.error("getDefaultProducts:" + t.getMessage());
        return new ArrayList<>();
    }
}
