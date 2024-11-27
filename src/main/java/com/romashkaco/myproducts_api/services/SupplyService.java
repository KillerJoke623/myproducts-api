package com.romashkaco.myproducts_api.services;

import com.romashkaco.myproducts_api.models.Product;
import com.romashkaco.myproducts_api.models.Supply;
import com.romashkaco.myproducts_api.repositories.ProductRepository;
import com.romashkaco.myproducts_api.repositories.SupplyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SupplyService {
    @Autowired
    private SupplyRepository supplyRepository;

    @Autowired
    private ProductRepository productRepository;

    public Supply addSupply(Supply supply) {
        Product product = productRepository.findById(supply.getProduct().getId())
                .orElseThrow(() -> new RuntimeException("Product not found"));

        //Увеличиваем количество товара
        if (product.getQuantity() == 0) {
            product.setAvailable(true);
        }
        product.setQuantity(product.getQuantity() + supply.getQuantity());
        productRepository.save(product);

        return supplyRepository.save(supply);
    }

    public Supply updateSupply(Long id, Supply supply) {
        Supply existingSupply = supplyRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Supply not found"));

        Product product = productRepository.findById(supply.getProduct().getId())
                .orElseThrow(() -> new RuntimeException("Product not found"));

        // Корректируем количество товара: удаляем старое значение, добавляем новое
        int quantityDelta = supply.getQuantity() - existingSupply.getQuantity();
        product.setQuantity(product.getQuantity() + quantityDelta);
        productRepository.save(product);

        // Обновляем запись Supply
        existingSupply.setDocumentName(supply.getDocumentName());
        existingSupply.setQuantity(supply.getQuantity());
        existingSupply.setProduct(product);

        return supplyRepository.save(existingSupply);
    }

    public void deleteSupply(Long id) {
        Supply existingSupply = supplyRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Supply not found"));

        Product product = productRepository.findById(existingSupply.getProduct().getId())
                .orElseThrow(() -> new RuntimeException("Product not found"));

        // Уменьшаем количество товара при удалении Supply
        product.setQuantity(product.getQuantity() - existingSupply.getQuantity());
        productRepository.save(product);

        supplyRepository.delete(existingSupply);
    }
}
