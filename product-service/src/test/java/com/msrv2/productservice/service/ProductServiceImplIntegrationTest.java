package com.msrv2.productservice.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.msrv2.productservice.model.Product;
import com.msrv2.productservice.model.ProductAvailability;
import com.msrv2.productservice.testUtils.TestUtils;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.times;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

//@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
@ActiveProfiles("testintegration")
@ExtendWith(MockitoExtension.class)
@ExtendWith(SpringExtension.class)
class ProductServiceImplIntegrationTest {
    @Autowired
    private MockMvc mockMvc;

    @SpyBean
    private InventoryServiceClient inventoryServiceClient;

    @SpyBean
    private CatalogServiceClient catalogServiceClient;

    //@SpyBean(name = "ProductServiceImpl")
    //@Autowired
    //@Qualifier("ProductServiceImpl")
    //private ProductService productService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    @SneakyThrows
    void getProductByUniqId() {
        //ProductService productServiceSpy = Mockito.spy(productService);

        ProductAvailability productAvailability =
                TestUtils.getMockData("mockInventoryProduct.json", ProductAvailability.class);
        Product product = TestUtils.getMockData("mockCatalogProduct.json", Product.class);

        TestUtils.mockInventoryAndCatalogServicesProductReturn(
                productAvailability, product, inventoryServiceClient, catalogServiceClient);

        Product productExpected = product.toBuilder().available(productAvailability.isAvailable()).build();
        final String uniqId = product.getUniqId();

        MvcResult mvcResult = mockMvc
                .perform(MockMvcRequestBuilders.get("/api/products/" + uniqId)
                                               .contentType("application/json"))
                .andExpect(status().isOk())
                .andReturn();

        ObjectMapper mapper = new ObjectMapper();
        Product productActual = mapper.readValue(mvcResult.getResponse().getContentAsString(), Product.class);

        Mockito.verify(inventoryServiceClient, times(1)).getProductAvailabilityByUniqId(Mockito.eq(uniqId));
        Mockito.verify(catalogServiceClient, times(1)).getProductByUniqId(Mockito.eq(uniqId));
        //Mockito.verify(productServiceSpy, times(1)).getProductByUniqId(Mockito.eq(uniqId));

        assertEquals(productExpected, productActual);
    }
}
