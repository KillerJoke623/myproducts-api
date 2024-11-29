package com.romashkaco.myproducts_api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.romashkaco.myproducts_api.models.Product;
import com.romashkaco.myproducts_api.models.Sale;
import com.romashkaco.myproducts_api.models.Supply;
import com.romashkaco.myproducts_api.repositories.ProductRepository;
import com.romashkaco.myproducts_api.repositories.SaleRepository;
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
public class SaleControllerTests {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private SaleRepository saleRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private Product testProduct;

    @BeforeEach
    void setUp() {
        saleRepository.deleteAll();
        productRepository.deleteAll();

        testProduct = new Product();
        testProduct.setName("Test Product");
        testProduct.setQuantity(100);
        testProduct.setPrice(20.0);
        productRepository.save(testProduct);
    }

    @Test
    void testCreateSaleSuccess() throws Exception {
        Sale sale = new Sale();
        sale.setDocumentName("Sale Test");
        sale.setProduct(testProduct);
        sale.setQuantity(10);


        mockMvc.perform(post("/sales")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(sale)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.documentName").value("Sale Test"))
                .andExpect(jsonPath("$.quantity").value(10))
                .andExpect(jsonPath("$.totalPrice").value(200.0));

        // Проверка уменьшения количества продукта
        Product updatedProduct = productRepository.findById(testProduct.getId()).orElseThrow();
        assertThat(updatedProduct.getQuantity()).isEqualTo(90);


    }



    @Test
    void testDeleteSale() throws Exception {
        Sale sale = new Sale();
        sale.setDocumentName("Sale Test");
        sale.setProduct(testProduct);
        sale.setQuantity(20);
        Sale savedSale = saleRepository.save(sale);

        mockMvc.perform(delete("/sales/" + savedSale.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        Optional<Sale> deletedSale = saleRepository.findById(savedSale.getId());
        assert (deletedSale.isEmpty());

        //Saving through repository, not the api, so expect to get higher than should be in real world
        Product updatedProduct = productRepository.findById(testProduct.getId()).orElseThrow();
        assertThat(updatedProduct.getQuantity()).isEqualTo(120);
    }

    @Test
    void testGetAllSales() throws Exception {
        Sale sale = new Sale();
        sale.setDocumentName("Sale Test");
        sale.setProduct(testProduct);
        sale.setQuantity(20);
        Sale savedSale = saleRepository.save(sale);

        mockMvc.perform(get("/sales")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1));
    }
}
