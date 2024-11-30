package com.romashkaco.myproducts_api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.romashkaco.myproducts_api.models.Product;
import com.romashkaco.myproducts_api.models.Supply;
import com.romashkaco.myproducts_api.repositories.ProductRepository;
import com.romashkaco.myproducts_api.repositories.SupplyRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class SupplyControllerTests {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private SupplyRepository supplyRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private Product testProduct;

    @BeforeEach
    void setUp() {
        supplyRepository.deleteAll();
        productRepository.deleteAll();

        testProduct = new Product();
        testProduct.setName("Test Product");
        testProduct.setQuantity(100);
        productRepository.save(testProduct);
    }

    @Test
    void testCreateSupplySuccess() throws Exception {
        Supply supply = new Supply();
        supply.setDocumentName("Supply Test");
        supply.setProduct(testProduct);
        supply.setQuantity(50);

        mockMvc.perform(post("/api/supplies")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(supply)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.documentName").value("Supply Test"))
                .andExpect(jsonPath("$.quantity").value(50));


        Product updatedProduct = productRepository.findById(testProduct.getId()).orElseThrow();
        assertThat(updatedProduct.getQuantity()).isEqualTo(150);
    }

    @Test
    void testCreateSupplyInvalidQuantity() throws Exception {
        Supply supply = new Supply();
        supply.setDocumentName("Invalid Supply");
        supply.setProduct(testProduct);
        supply.setQuantity(0);

        mockMvc.perform(post("/api/supplies")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(supply)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testGetAllSupplies() throws Exception {
        Supply supply1 = new Supply();
        supply1.setDocumentName("Supply 1");
        supply1.setProduct(testProduct);
        supply1.setQuantity(30);
        supplyRepository.save(supply1);

        mockMvc.perform(get("/api/supplies")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1));
    }

    @Test
    void testDeleteSupply() throws Exception {
        Supply supply = new Supply();
        supply.setDocumentName("Supply Delete");
        supply.setProduct(testProduct);
        supply.setQuantity(20);
        Supply savedSupply = supplyRepository.save(supply);

        mockMvc.perform(delete("/api/supplies/" + savedSupply.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        Optional<Supply> deletedSupply = supplyRepository.findById(savedSupply.getId());
        assert (!deletedSupply.isPresent());

        //Saving through repository, not the api, so expect to get lower than should be in real world
        Product updatedProduct = productRepository.findById(testProduct.getId()).orElseThrow();
        assertThat(updatedProduct.getQuantity()).isEqualTo(80);
    }
}
