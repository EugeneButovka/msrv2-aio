package com.msrv2.productservice.service;


import com.msrv2.productservice.model.Product;

import java.util.List;

public interface ProductService {
    Product getProductByUniqId(String uniqId);

    List<Product> getProductsBySku(String sku);

    @SuppressWarnings("unused")
    Product getDefaultProduct(String uniqId, Throwable t);

    @SuppressWarnings("unused")
    List<Product> getDefaultProducts(String sku, Throwable t);
}
