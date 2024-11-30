package com.romashkaco.myproducts_api;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;


import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class ProductControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ProductRepository productRepository;

    @BeforeEach
    void setUp() {
        productRepository.deleteAll();

        Product productA = new Product();
        productA.setName("Product A");
        productA.setPrice(10.50);
        productA.setAvailable(true);

        Product productB = new Product();
        productB.setName("Product B");
        productB.setPrice(20.75);
        productB.setAvailable(false);

        Product anotherProduct = new Product();
        anotherProduct.setName("Another Product");
        anotherProduct.setPrice(15.30);
        anotherProduct.setAvailable(true);

        productRepository.saveAll(Arrays.asList(productA, productB, anotherProduct));

    }

    @Test
    void shouldFilterByName() throws Exception {
        mockMvc.perform(get("/api/products")
                        .param("name", "another")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].name", containsString("Another")));
    }

    @Test
    void shouldFilterByPriceGreaterThan() throws Exception {
        mockMvc.perform(get("/api/products")
                        .param("minPrice", "15.00")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].price", greaterThan(14.99)));
    }

    @Test
    void shouldSortByName() throws Exception {
        mockMvc.perform(get("/api/products")
                        .param("sortBy", "name")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name", is("Another Product")));
    }

    @Test
    void shouldLimitResults() throws Exception {
        mockMvc.perform(get("/api/products")
                        .param("limit", "2")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)));
    }

    @Test
    void shouldHandleInvalidPriceParameter() throws Exception {
        mockMvc.perform(get("/api/products")
                        .param("minPrice", "invalid_number")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }





    @Test
    void shouldApplyMultipleFiltersAndSortByName() throws Exception {
        mockMvc.perform(get("/api/products")
                        .param("name", "Product")
                        .param("minPrice", "10")
                        .param("maxPrice", "20")
                        .param("available", "true")
                        .param("sortBy", "name")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].name", is("Another Product")));
    }

    @Test
    void shouldApplyFiltersAndLimitResults() throws Exception {
        mockMvc.perform(get("/api/products")
                        .param("minPrice", "10")
                        .param("sortBy", "price")
                        .param("limit", "2")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].price", is(10.50)))
                .andExpect(jsonPath("$[1].price", is(15.30)));
    }

}
