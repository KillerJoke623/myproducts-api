package com.romashkaco.myproducts_api;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/products")
public class ProductController {
    private final List<Product> products = new ArrayList<>();
    private int nextId = 0;

    @GetMapping
    public List<Product> getAllProducts() {
        return products;
    }

    @GetMapping("/{id}")
    public ResponseEntity<Product> getProductById(@PathVariable int id) {
        Optional<Product> product = products.stream().filter(p -> p.getId() == id).findFirst();
        return product.map(ResponseEntity::ok).orElseThrow(() -> new EntityNotFoundException("Product with id " + id + " not found"));
    }

    @PostMapping
    public ResponseEntity<Product> createProduct(@Valid @RequestBody Product product) {
        product.setId(nextId++);
        products.add(product);
        return ResponseEntity.status(HttpStatus.CREATED).body(product);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Product> updateProduct(@PathVariable int id, @Valid @RequestBody Product updatedProduct) throws Exception {
        Optional<Product> existingProduct = products.stream().filter(p -> p.getId() == id).findFirst();

        if (existingProduct.isPresent()) {
            Product product = existingProduct.get();
            product.setName(updatedProduct.getName());
            product.setDescription(updatedProduct.getDescription());
            product.setPrice(updatedProduct.getPrice());
            product.setAvailable(updatedProduct.isAvailable());
            return ResponseEntity.ok(product);
        } else {
            throw  new EntityNotFoundException("Product with id" + id + " not found");
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable int id) {
        boolean removed = products.removeIf(p -> p.getId() == id);
        return removed ? ResponseEntity.noContent().build() : ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }
}
