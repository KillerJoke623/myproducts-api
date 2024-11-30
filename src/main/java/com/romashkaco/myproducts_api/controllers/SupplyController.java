package com.romashkaco.myproducts_api.controllers;

import com.romashkaco.myproducts_api.models.Supply;
import com.romashkaco.myproducts_api.repositories.SupplyRepository;
import com.romashkaco.myproducts_api.services.SupplyService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/supplies")
public class SupplyController {
    @Autowired
    private SupplyService supplyService;

    @Autowired
    private SupplyRepository supplyRepository;

    @PostMapping
    public ResponseEntity<Supply> createSupply(@RequestBody @Valid Supply supply) {
        Supply createdSupply = supplyService.addSupply(supply);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdSupply);
    }

    // Получение всех поставок
    @GetMapping
    public ResponseEntity<List<Supply>> getAllSupplies() {
        List<Supply> supplies = supplyRepository.findAll();
        return ResponseEntity.ok(supplies);
    }

    // Получение поставки по ID
    @GetMapping("/{id}")
    public ResponseEntity<Supply> getSupplyById(@PathVariable Long id) {
        return supplyRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    // Обновление поставки
    @PutMapping("/{id}")
    public ResponseEntity<Supply> updateSupply(@PathVariable Long id, @RequestBody @Valid Supply supply) {
        Supply updatedSupply = supplyService.updateSupply(id, supply);
        return ResponseEntity.ok(updatedSupply);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSupply(@PathVariable Long id) {
        supplyService.deleteSupply(id);
        return ResponseEntity.noContent().build();
    }
}
