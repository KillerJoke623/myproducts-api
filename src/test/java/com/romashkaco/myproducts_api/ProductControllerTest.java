package com.romashkaco.myproducts_api;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class ProductControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Test
    public void shouldCreateProduct() throws Exception {
        String productJson = """
            {
                "name": "Test Product",
                "description": "A sample product",
                "price": 10.5,
                "available": true
            }
        """;

        mockMvc.perform(post("/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(productJson))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Test Product"));
    }

    @Test
    public void shouldFailValidationForInvalidProduct() throws Exception {
        String invalidProductJson = """
            {
                "name": "",
                "description": "A sample product",
                "price": -10.5
            }
        """;

        mockMvc.perform(post("/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidProductJson))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldReturnAllProducts() throws Exception {
        mockMvc.perform(get("/products"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").isNotEmpty());
    }

    @Test
    public void shouldReturnProductById() throws Exception {
        mockMvc.perform(get("/products/0"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(0));
    }

    @Test
    public void shouldReturnNotFoundForNonExistentProduct() throws Exception {
        mockMvc.perform(get("/products/999"))
                .andExpect(status().isNotFound());
    }
}
