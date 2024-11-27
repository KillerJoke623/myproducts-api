package com.romashkaco.myproducts_api.repositories;

import com.romashkaco.myproducts_api.models.Supply;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SupplyRepository extends JpaRepository<Supply, Long> {
}