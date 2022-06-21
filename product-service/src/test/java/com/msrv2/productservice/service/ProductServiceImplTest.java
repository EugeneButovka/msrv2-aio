package com.msrv2.productservice.service;

import com.msrv2.productservice.model.Product;
import com.msrv2.productservice.model.ProductAvailability;
import com.msrv2.productservice.testUtils.TestUtils;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.times;

@ActiveProfiles("test")
@ExtendWith(SpringExtension.class)
@ExtendWith(MockitoExtension.class)
class ProductServiceImplTest {
    @MockBean
    InventoryServiceClient inventoryServiceClient;

    @MockBean
    CatalogServiceClient catalogServiceClient;

    @InjectMocks
    ProductService productService = Mockito.spy(new ProductServiceImpl());


    @Test
    @SneakyThrows
    void testGetProductByUniqId_GivenFileMockProductData() {
        ProductAvailability productAvailability =
                TestUtils.getMockData("mockInventoryProduct.json", ProductAvailability.class);
        Product product = TestUtils.getMockData("mockCatalogProduct.json", Product.class);

        TestUtils.mockInventoryAndCatalogServicesProductReturn(
                productAvailability, product, inventoryServiceClient, catalogServiceClient);

        Product productExpected = product.toBuilder().available(productAvailability.isAvailable()).build();
        final String uniqId = product.getUniqId();

        Product productActual = productService.getProductByUniqId(uniqId);

        Mockito.verify(inventoryServiceClient, times(1)).getProductAvailabilityByUniqId(Mockito.eq(uniqId));
        Mockito.verify(catalogServiceClient, times(1)).getProductByUniqId(Mockito.eq(uniqId));
        Mockito.verify(productService, times(1)).getProductByUniqId(Mockito.eq(uniqId));

        assertEquals(productExpected, productActual);
    }

    @ParameterizedTest
    @ValueSource(booleans = {true, false})
    void testGetProductByUniqId_GivenParametrisedProductData(boolean isAvailable) {
        final String uniqId = "123";

        ProductAvailability productAvailability =
                ProductAvailability.builder().uniqId(uniqId).available(isAvailable).build();
        Product product = Product.builder().uniqId(uniqId).build();

        TestUtils.mockInventoryAndCatalogServicesProductReturn(
                productAvailability, product, inventoryServiceClient, catalogServiceClient);

        Product productExpected = product.toBuilder().available(isAvailable).build();

        Product productActual = productService.getProductByUniqId(uniqId);

        Mockito.verify(inventoryServiceClient, times(1)).getProductAvailabilityByUniqId(Mockito.eq(uniqId));
        Mockito.verify(catalogServiceClient, times(1)).getProductByUniqId(Mockito.eq(uniqId));
        Mockito.verify(productService, times(1)).getProductByUniqId(Mockito.eq(uniqId));

        assertEquals(productExpected, productActual);
    }


    @Test
    void getProductsBySku() {
        final Map<String, Boolean> input = Stream.of(
                                                         new AbstractMap.SimpleEntry<>("123", true),
                                                         new AbstractMap.SimpleEntry<>("124", false),
                                                         new AbstractMap.SimpleEntry<>("125", true))
                                                 .collect(Collectors.toMap(AbstractMap.SimpleEntry::getKey,
                                                                           AbstractMap.SimpleEntry::getValue));
        List<ProductAvailability> productsAvailability = new ArrayList<>();
        List<Product> products = new ArrayList<>();
        input.forEach((uniqId, isAvailable) -> {
            productsAvailability.add(ProductAvailability.builder()
                                                        .uniqId(uniqId)
                                                        .available(isAvailable)
                                                        .build());
            products.add(Product.builder()
                                .uniqId(uniqId)
                                //.available(isAvailable)
                                .build());
        });

        TestUtils.mockInventoryAndCatalogServicesProductsReturn(productsAvailability, products, inventoryServiceClient,
                                                                catalogServiceClient);

        List<Product> productsExpected = products
                .stream()
                .filter(product -> productsAvailability
                        .stream()
                        .filter(productAvailability -> productAvailability.getUniqId()
                                                                          .equals(product.getUniqId()))
                        .findFirst()
                        .get()
                        .isAvailable())
                .peek(product -> product.setAvailable(true))
                .collect(Collectors.toList());

        String sku = "555";
        List<Product> productsActual = productService.getProductsBySku(sku);

        Mockito.verify(inventoryServiceClient, times(1)).getProductsAvailabilityBySku(Mockito.eq(sku));
        Mockito.verify(catalogServiceClient, times(1)).getProductsBySku(Mockito.eq(sku));
        Mockito.verify(productService, times(1)).getProductsBySku(Mockito.eq(sku));

        assertEquals(productsExpected, productsActual);

    }
}
