package com.romashkaco.myproducts_api.repositories;

import com.romashkaco.myproducts_api.models.Sale;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SaleRepository extends JpaRepository<Sale, Long> {
}