package com.msrv2.productservice.testUtils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.msrv2.productservice.model.Product;
import com.msrv2.productservice.model.ProductAvailability;
import com.msrv2.productservice.service.CatalogServiceClient;
import com.msrv2.productservice.service.InventoryServiceClient;
import lombok.SneakyThrows;
import org.mockito.Mockito;
import org.springframework.core.io.ClassPathResource;

import java.io.File;
import java.util.Optional;

public class TestUtils {
    @SneakyThrows
    public static <T> T getMockData(String resourceFileName, Class<T> valueType) {
        ObjectMapper mapper = new ObjectMapper();
        File productAvailabilityFile = new ClassPathResource(resourceFileName).getFile();
        return mapper.readValue(productAvailabilityFile, valueType);
    }

    public static void mockInventoryAndCatalogServicesProductReturn(ProductAvailability productAvailability,
                                                                    Product product,
                                                                    InventoryServiceClient inventoryServiceClient,
                                                                    CatalogServiceClient catalogServiceClient
    ) {
        Mockito.doReturn(Optional.of(productAvailability))
               .when(inventoryServiceClient)
               .getProductAvailabilityByUniqId(Mockito.eq(productAvailability.getUniqId()));

        Mockito.doReturn(Optional.of(product))
               .when(catalogServiceClient)
               .getProductByUniqId(Mockito.eq(product.getUniqId()));
    }

}
