package com.yourorg.yourapp.adapter.persistence.repository;

import com.yourorg.yourapp.adapter.persistence.entity.ProductEntity;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaProductRepository extends JpaRepository<ProductEntity, UUID> {
    boolean existsBySkuIgnoreCase(String sku);
    Optional<ProductEntity> findBySkuIgnoreCase(String sku);
}

