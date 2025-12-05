package com.yourorg.yourapp.adapter.persistence.repository;

import com.yourorg.yourapp.adapter.annotation.AdapterComponent;
import com.yourorg.yourapp.adapter.persistence.entity.ProductDataMapper;
import com.yourorg.yourapp.adapter.persistence.entity.ProductEntity;
import com.yourorg.yourapp.domain.model.Product;
import com.yourorg.yourapp.usecase.outputboundary.ProductDsGateway;
import java.util.Optional;
import org.springframework.transaction.annotation.Transactional;

@AdapterComponent
public class ProductRepositoryAdapter implements ProductDsGateway {

    private final JpaProductRepository repository;

    public ProductRepositoryAdapter(JpaProductRepository repository) {
        this.repository = repository;
    }

    @Override
    public boolean existsBySku(String sku) {
        return repository.existsBySkuIgnoreCase(sku);
    }

    @Override
    @Transactional
    public Product save(Product product) {
        ProductEntity saved = repository.save(ProductDataMapper.toEntity(product));
        return ProductDataMapper.toDomain(saved);
    }

    @Override
    public Optional<Product> findBySku(String sku) {
        return repository.findBySkuIgnoreCase(sku).map(ProductDataMapper::toDomain);
    }
}

