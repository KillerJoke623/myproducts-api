package com.romashkaco.myproducts_api.controllers;

import com.romashkaco.myproducts_api.models.Sale;
import com.romashkaco.myproducts_api.repositories.SaleRepository;
import com.romashkaco.myproducts_api.services.SaleService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/sales")
public class SaleController {
    @Autowired
    private SaleService saleService;

    @Autowired
    private SaleRepository saleRepository;

    @PostMapping
    public ResponseEntity<Sale> createSale(@RequestBody @Valid Sale sale) {
        Sale createdSale = saleService.addSale(sale);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdSale);
    }

    // Получение всех продаж
    @GetMapping
    public ResponseEntity<List<Sale>> getAllSales() {
        List<Sale> sales = saleRepository.findAll();
        return ResponseEntity.ok(sales);
    }

    // Получение продажи по ID
    @GetMapping("/{id}")
    public ResponseEntity<Sale> getSaleById(@PathVariable Long id) {
        return saleRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    // Обновление поставки
    @PutMapping("/{id}")
    public ResponseEntity<Sale> updateSale(@PathVariable Long id, @RequestBody @Valid Sale sale) {
        Sale updatedSale = saleService.updateSale(id, sale);
        return ResponseEntity.ok(updatedSale);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSale(@PathVariable Long id) {
        saleService.deleteSale(id);
        return ResponseEntity.noContent().build();
    }
}
