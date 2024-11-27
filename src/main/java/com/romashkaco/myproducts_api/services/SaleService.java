package com.romashkaco.myproducts_api.services;

import com.romashkaco.myproducts_api.models.Product;
import com.romashkaco.myproducts_api.models.Sale;
import com.romashkaco.myproducts_api.repositories.ProductRepository;
import com.romashkaco.myproducts_api.repositories.SaleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SaleService {
    @Autowired
    private SaleRepository saleRepository;

    @Autowired
    private ProductRepository productRepository;

    public Sale addSale(Sale sale) {
        Product product = productRepository.findById(sale.getProduct().getId())
                .orElseThrow(() -> new RuntimeException("Product not found"));

        if (product.getQuantity() < sale.getQuantity()) {
            throw new RuntimeException("Not enough products in stock");
        }

        // Уменьшаем количество товара
        product.setQuantity(product.getQuantity() - sale.getQuantity());
        if (product.getQuantity() == 0) {
            product.setAvailable(false);
        }
        productRepository.save(product);



        // Рассчитываем стоимость
        sale.setTotalPrice(sale.getQuantity() * product.getPrice());

        return saleRepository.save(sale);
    }

    public Sale updateSale(Long id, Sale sale) {
        Sale existingSale = saleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Sale not found"));

        Product product = productRepository.findById(sale.getProduct().getId())
                .orElseThrow(() -> new RuntimeException("Product not found"));

        // Корректируем количество товара
        int quantityDelta = sale.getQuantity() - existingSale.getQuantity();
        if (product.getQuantity() - quantityDelta < 0) {
            throw new RuntimeException("Not enough products in stock");
        }
        product.setQuantity(product.getQuantity() - quantityDelta);
        if (product.getQuantity() == 0) {
            product.setAvailable(false);
        }
        productRepository.save(product);

        // Обновляем запись Sale
        existingSale.setDocumentName(sale.getDocumentName());
        existingSale.setQuantity(sale.getQuantity());
        existingSale.setTotalPrice(sale.getQuantity() * product.getPrice());
        existingSale.setProduct(product);

        return saleRepository.save(existingSale);
    }

    public void deleteSale(Long id) {
        Sale existingSale = saleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Sale not found"));

        Product product = productRepository.findById(existingSale.getProduct().getId())
                .orElseThrow(() -> new RuntimeException("Product not found"));

        // Возвращаем товар при удалении Sale
        product.setQuantity(product.getQuantity() + existingSale.getQuantity());
        productRepository.save(product);

        saleRepository.delete(existingSale);
    }
}
