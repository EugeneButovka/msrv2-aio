package com.msrv2.productservice;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@ActiveProfiles("testintegration")
class ProductServiceApplicationTest {
    @Test
    public void contextLoads() {
        System.out.println("context loads");
        assertTrue(true);
    }
}
